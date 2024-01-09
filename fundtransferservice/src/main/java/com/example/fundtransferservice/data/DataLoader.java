package com.example.fundtransferservice.data;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TransactionRepository transactionRepository;
    private final FundTransferRestClient fundTransferRestClient;

    @Override
    public void run(String... args) throws Exception {
        fundTransferRestClient.loadData();
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setTransactionReference("TXN" + String.format("%06d", i));
            transaction.setStatus(TransactionStatus.values()[random.nextInt(TransactionStatus.values().length)]);
            transaction.setPaymentType(TransactionType.values()[random.nextInt(TransactionType.values().length)]);
            transaction.setAmount(random.nextInt(1000) + 100); // Random amount between 100 and 1100
            transaction.setIssueDate(new Date());
            transaction.setExpiryDate(new Date()); // You can adjust this
            transaction.setDonorId(random.nextInt(4) + 1L);
            transaction.setAgentId(random.nextInt(4) + 1L);
            transaction.setBeneficiaryId(random.nextInt(4) + 1L);
            transaction.setNotificationFees(random.nextBoolean());
            transaction.setFraisTransfert(random.nextDouble() * 15); // Random fees up to 15
            transaction.setWhoPayFees(random.nextBoolean() ? "Donor" : "Beneficiary");

            Date currentDate = new Date();
            int randomDays = random.nextInt(5) + 1;
            long expiryMillis = currentDate.getTime() + (randomDays * 24 * 60 * 60 * 1000);
            transaction.setExpiryDate(new Date(expiryMillis));

            transactionRepository.save(transaction);
        }
    }
}