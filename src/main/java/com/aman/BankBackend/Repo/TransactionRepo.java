package com.aman.BankBackend.Repo;

import com.aman.BankBackend.DTO.TransactionDto;
import com.aman.BankBackend.entity.Transactions;
import jakarta.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transactions,String> {
}
