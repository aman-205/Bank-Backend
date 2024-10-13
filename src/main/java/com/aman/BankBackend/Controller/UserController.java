package com.aman.BankBackend.Controller;

import com.aman.BankBackend.DTO.BankResponse;
import com.aman.BankBackend.DTO.CreditDebitRequest;
import com.aman.BankBackend.DTO.EnquiryRequest;
import com.aman.BankBackend.DTO.UserRequest;
import com.aman.BankBackend.Service.UserService;
import com.aman.BankBackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/balanceEnquiry")
    public  BankResponse balanceEnquiry(@RequestBody EnquiryRequest userRequest){
        return userService.balanceEnquiry(userRequest);
    }

    @GetMapping("/nameEnquiry")
    public  String nameEnquiry(@RequestBody EnquiryRequest userRequest){
        return userService.nameEnquiry(userRequest);
    }

    @PostMapping("/credit")
    public BankResponse credit(@RequestBody CreditDebitRequest request){
        return  userService.creditAccount(request);
    }

    @PostMapping("/debit")
    public BankResponse debit(@RequestBody CreditDebitRequest request){
        return  userService.debitAccount(request);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

}
