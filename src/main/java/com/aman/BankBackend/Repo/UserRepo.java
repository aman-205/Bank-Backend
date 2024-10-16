package com.aman.BankBackend.Repo;


import com.aman.BankBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    Optional<User> findByEmail(String  email);
    User findByAccountNumber(String accountNumber);
}

