package com.aman.BankBackend.DTO;

import jakarta.transaction.Transaction;

public interface TransactionService {
    void  saveTransaction(TransactionDto transaction);
}
