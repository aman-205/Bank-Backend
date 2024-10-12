package com.aman.BankBackend.Controller;

import com.aman.BankBackend.DTO.BankResponse;
import com.aman.BankBackend.DTO.UserRequest;
import com.aman.BankBackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")  // Base URL mapping
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String getUser(){
        return "hello";
    }

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
}
