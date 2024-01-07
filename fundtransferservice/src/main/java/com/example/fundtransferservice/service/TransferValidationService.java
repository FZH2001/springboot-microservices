package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.dto.Utils;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferValidationService {
    private final TransactionRepository transactionRepository;
    private final IntegrationService integrationService;
    private final FundTransferRestClient fundTransferRestClient;
    private final Utils utils;
    private final FeesCalculationService feesCalculationService;
    private final OTPService otpService;

    public TransactionResponse validatePayment(String reference, String otp){
        if(!otpService.validateOTP(otp)){
            return utils.buildFailedTransactionResponse(reference,"OTP is not valid");
        }

        TransactionEntity transactionEntity =transactionRepository.findByTransactionReference(reference);
        BeneficiaryResponse beneficiaryResponse = fundTransferRestClient.getBeneficiaryInfo(transactionEntity.getDonorId());
        TransactionResponse transactionResponse = new TransactionResponse();
        //noinspection ConstantValue
        if(transactionEntity!=null){
            // check for blocking process
            if(integrationService.isBeneficiaryBlackListed(transactionEntity.getBeneficiaryId())){
                log.info("Payment is blocked");
                transactionEntity.setStatus(TransactionStatus.BLOCKED);
                transactionRepository.save(transactionEntity);
                transactionResponse=utils.buildFailedTransactionResponse(reference,"Payment is blocked");
            }
            else if(transactionEntity.getStatus().equals(TransactionStatus.PAID) || transactionEntity.getStatus().equals(TransactionStatus.BLOCKED)){
                transactionResponse=utils.buildFailedTransactionResponse(reference,"Payment is already paid or blocked");
            }
            // check for type of payment
            else {
                switch (transactionEntity.getPaymentType()){
                    case WALLET:
                        if(!integrationService.beneficiaryHasWallet(beneficiaryResponse.getId())){
                            // TODO : create wallet
                            ClientResponse wallet = integrationService.createWalletClient(beneficiaryResponse);
                            beneficiaryResponse.setWalletClient(wallet.getId());
                            fundTransferRestClient.saveOrUpdateClient(wallet);
                        }
                            double amountToBeAdded = transactionEntity.getWhoPayFees().equals("Donor") ? feesCalculationService.calculFraisDonneurOrdre(transactionEntity.getAmount(),
                                    transactionEntity.getFraisTransfert(),
                                    transactionEntity.isNotificationFees()).get("montantTransferer"):
                                    (transactionEntity.getWhoPayFees().equals("Beneficiary") ?
                                            feesCalculationService.calculFraisBeneficiaire(transactionEntity.getAmount(),
                                                    transactionEntity.getFraisTransfert(),
                                                    transactionEntity.isNotificationFees()).get("montantTransferer")
                                            :feesCalculationService.calculFraisPartages(transactionEntity.getAmount(),
                                            transactionEntity.getFraisTransfert(),
                                            transactionEntity.isNotificationFees()).get("montantTransferer"));
                            integrationService.updateClientCredits(beneficiaryResponse.getWalletClient(),amountToBeAdded, "increment");
                        break;
                    case CASH:
                        if(!integrationService.updateAgentCredits(transactionEntity.getAgentId(),transactionEntity.getAmount(),"decrement")){
                            return utils.buildFailedTransactionResponse(reference,"Agent went broke");
                        }
                        break;
                    case GAB:
                        if(integrationService.isGabEmpty()){
                            return utils.buildFailedTransactionResponse(reference,"GAB is Empty");
                        }
                        break;
                    default:
                        return utils.buildFailedTransactionResponse(reference,"Something really fishy going on here");
                }
            }

                    transactionEntity.setStatus(TransactionStatus.PAID);
                    transactionRepository.save(transactionEntity);
                    return utils.buildSuccessfulTransactionResponse(transactionEntity);

            }
        else {
            return utils.buildFailedTransactionResponse(reference,"Transaction not found");
        }
    }


}
