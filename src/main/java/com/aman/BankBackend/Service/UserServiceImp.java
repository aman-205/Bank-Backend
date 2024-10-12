package com.aman.BankBackend.Service;


import com.aman.BankBackend.DTO.AccountInfo;
import com.aman.BankBackend.DTO.BankResponse;
import com.aman.BankBackend.DTO.EmailDetails;
import com.aman.BankBackend.DTO.UserRequest;
import com.aman.BankBackend.Repo.UserRepo;
import com.aman.BankBackend.entity.User;
import com.aman.BankBackend.utils.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImp implements  UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if(userRepo.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(Account.Account_exist_code)
                    .responseMessage(Account.Account_exist_message)
                    .accountInfo(null)
                    .build();
        }
        User newUser= User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .state(userRequest.getState())
                .accountNumber(Account.generate())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .status("Active")
                .build();

        User savedUser=userRepo.save(newUser);

        EmailDetails emailDetails=EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations Your Account has been successfully created.\n Your Account details:\n" +
                        "Account name: "+savedUser.getFirstName()+" "+savedUser.getLastName()+"\nAccount number: "+savedUser.getAccountNumber())
                .build();
        emailService.emailMessageAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(Account.Account_creation_code)
                .responseMessage(Account.Account_creation_message)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName())
                        .build())
                .build();
    }
}
