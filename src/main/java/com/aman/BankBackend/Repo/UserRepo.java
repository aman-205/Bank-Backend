package com.aman.BankBackend.Repo;


import com.aman.BankBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
}
