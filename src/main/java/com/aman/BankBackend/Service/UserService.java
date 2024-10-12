package com.aman.BankBackend.Service;


import com.aman.BankBackend.DTO.BankResponse;
import com.aman.BankBackend.DTO.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
