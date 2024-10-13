package com.aman.BankBackend.Service;


import com.aman.BankBackend.DTO.*;
import com.aman.BankBackend.Repo.UserRepo;
import com.aman.BankBackend.entity.User;
import com.aman.BankBackend.utils.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImp implements  UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExits= userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExits){
            return BankResponse.builder()
                    .responseCode(Account.Account_notExist_code)
                    .responseMessage(Account.Account_notExist_message)
                    .accountInfo(null)
                    .build();

        }
        User foundUser=userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(Account.Account_Found_code)
                .responseMessage(Account.Account_Found_message)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .build())

                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExits= userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExits){
            return Account.Account_notExist_message;
        }
        User foundUser= userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName()+" "+ foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExits= userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExits){
            return BankResponse.builder()
                    .responseCode(Account.Account_notExist_code)
                    .responseMessage(Account.Account_notExist_message)
                    .accountInfo(null)
                    .build();
        }
        User foundUser= userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber());
        foundUser.setAccountBalance(foundUser.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepo.save(foundUser);
        return BankResponse.builder()
                .responseCode(Account.Account_credit_code)
                .responseMessage(Account.Account_credit_message)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .build())

                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        boolean isAccountExits= userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!isAccountExits){
            return BankResponse.builder()
                    .responseCode(Account.Account_notExist_code)
                    .responseMessage(Account.Account_notExist_message)
                    .accountInfo(null)
                    .build();
        }
        User foundUser= userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber());
        if(foundUser.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0){
            return BankResponse.builder()
                    .responseCode(Account.Account_insufficient_code)
                    .responseMessage(Account.Account_insufficient_message)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                            .build())
                    .build();
        }
        foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        userRepo.save(foundUser);
        return BankResponse.builder()
                .responseCode(Account.Account_debit_code)
                .responseMessage(Account.Account_debit_message)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .build())

                .build();
    }
}
