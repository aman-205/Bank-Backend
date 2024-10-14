package com.aman.BankBackend.Controller;

import com.aman.BankBackend.DTO.*;
import com.aman.BankBackend.Service.UserService;
import com.aman.BankBackend.entity.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Create new user account",
            description = "Creating a new user for the bank"

    )
    @ApiResponse(
            responseCode = "201",
            description = "http status 201 created"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
    @Operation(
            summary = "check balance",
            description = "check balance of user if exist"

    )
    @GetMapping("/balanceEnquiry")
    public  BankResponse balanceEnquiry(@RequestBody EnquiryRequest userRequest){
        return userService.balanceEnquiry(userRequest);
    }
    @Operation(
            summary = "Check whether user exists",
            description = "check if user exist or not"

    )
    @GetMapping("/nameEnquiry")
    public  String nameEnquiry(@RequestBody EnquiryRequest userRequest){
        return userService.nameEnquiry(userRequest);
    }

    @Operation(
            summary = "Credit Amount",
            description = "To credit a certain amount to account"

    )
    @PostMapping("/credit")
    public BankResponse credit(@RequestBody CreditDebitRequest request){
        return  userService.creditAccount(request);
    }
    @Operation(
            summary = "Debit Amount",
            description = "To debit a certain amount from account"

    )
    @PostMapping("/debit")
    public BankResponse debit(@RequestBody CreditDebitRequest request){
        return  userService.debitAccount(request);
    }
    @Operation(
            summary = "Get all user",
            description = "get details of all users "

    )

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }
    @Operation(
            summary = "transfer Amount",
            description = "transferring amount from one user to another"

    )
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody Transfer transfer){
        return userService.transferAmount(transfer);
    }

}
