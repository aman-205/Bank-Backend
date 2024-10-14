package com.aman.BankBackend.DTO;

import com.aman.BankBackend.Repo.TransactionRepo;
import com.aman.BankBackend.entity.Transactions;
import jakarta.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionImp implements TransactionService{



    @Autowired
    TransactionRepo transactionRepo;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
            Transactions transaction= Transactions.builder()
                    .transactionType(transactionDto.getTransactionType())
                    .accountNumber(transactionDto.getAccountNumber())
                    .amount(transactionDto.getAmount())
                    .status("Success")
                    .build();
            transactionRepo.save(transaction);

    }
}
