package com.example.fundtransferservice.data;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransactionRepository transactionRepository;

    public DataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setTransactionReference("TXN" + String.format("%06d", i));
            transaction.setStatus(TransactionStatus.values()[random.nextInt(TransactionStatus.values().length)]);
            transaction.setPaymentType(TransactionType.values()[random.nextInt(TransactionType.values().length)]);
            transaction.setAmount(random.nextInt(1000) + 100); // Random amount between 100 and 1100
            transaction.setIssueDate(new Date());
            transaction.setExpiryDate(new Date()); // You can adjust this
            transaction.setDonorId((long) random.nextInt(1000)); // Random Long for donorId
            transaction.setBeneficiaryId((long) random.nextInt(1000)); // Random Long for beneficiaryId
            transaction.setAgentId((long) random.nextInt(1000));
            transaction.setNotificationFees(random.nextBoolean());
            transaction.setFraisTransfert(random.nextDouble() * 15); // Random fees up to 15
            transaction.setWhoPayFees(random.nextBoolean() ? "Donor" : "Beneficiary");

            transactionRepository.save(transaction);
        }
    }
}