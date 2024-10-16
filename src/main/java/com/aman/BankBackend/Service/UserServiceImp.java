package com.aman.BankBackend.Service;


import com.aman.BankBackend.Config.JwtTokenProvider;
import com.aman.BankBackend.DTO.*;
import com.aman.BankBackend.Repo.UserRepo;
import com.aman.BankBackend.entity.Role;
import com.aman.BankBackend.entity.User;
import com.aman.BankBackend.utils.Account;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements  UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .status("Active")
                .role(Role.valueOf("ROLE_ADMIN"))
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


    public  BankResponse login(LoginDto loginDto){
        Authentication authentication=null;
        authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        EmailDetails loginAlert= EmailDetails.builder()
                .subject("Yor are logged in")
                .recipient(loginDto.getEmail())
                .messageBody("You logged into your account. If you didn't initiate this request, please inform")
                .build();
        emailService.emailMessageAlert(loginAlert);
        return  BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generate(authentication))
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

        TransactionDto transactionDto= TransactionDto.builder()
                .accountNumber(foundUser.getAccountNumber())
                .transactionType("Credit")
                .amount(creditDebitRequest.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        TransferDetails transferDetails=TransferDetails.builder()
                .recipient(foundUser.getEmail())
                .messageBody(creditDebitRequest.getAmount()+"is credited to your account by cash")
                .subject("Amount Credit")
                .build();
        emailService.transferDetails(transferDetails);
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

        TransactionDto transactionDto= TransactionDto.builder()
                .accountNumber(foundUser.getAccountNumber())
                .transactionType("Debit")
                .amount(creditDebitRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        TransferDetails transferDetails=TransferDetails.builder()
                .recipient(foundUser.getEmail())
                .messageBody(creditDebitRequest.getAmount()+"is debited from your account by cash")
                .subject("Amount debit")
                .build();
        emailService.transferDetails(transferDetails);
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

    @Override
    public BankResponse transferAmount(Transfer transfer) {
        boolean accountExists= userRepo.existsByAccountNumber(transfer.getSenderAccountNumber());
        boolean receiverAccountExists=userRepo.existsByAccountNumber(transfer.getReceiverAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .responseCode(Account.Account_notExist_code)
                    .responseMessage(Account.Account_notExist_message)
                    .accountInfo(null)
                    .build();
        }
        else if(!receiverAccountExists){
            return BankResponse.builder()
                    .responseCode(Account.Account_noReceiver_code)
                    .responseMessage(Account.Account_noReceiver_message)
                    .accountInfo(null)
                    .build();
        }
        User sender= userRepo.findByAccountNumber(transfer.getSenderAccountNumber());
        if(sender.getAccountBalance().compareTo(transfer.getAmount())<0){
            return BankResponse.builder()
                    .responseCode(Account.Account_insufficient_code)
                    .responseMessage(Account.Account_insufficient_message)
                    .accountInfo(AccountInfo.builder()
                            .accountName(sender.getFirstName()+" "+sender.getLastName())
                            .accountBalance(sender.getAccountBalance())
                            .accountNumber(sender.getAccountNumber())
                            .build())
                    .build();

        }
        User receiver= userRepo.findByAccountNumber(transfer.getReceiverAccountNumber());
        receiver.setAccountBalance(receiver.getAccountBalance().add(transfer.getAmount()));
        sender.setAccountBalance(sender.getAccountBalance().subtract(transfer.getAmount()));
        userRepo.save(receiver);
        userRepo.save(sender);
        TransferDetails transferDetails=TransferDetails.builder()
                .recipient(sender.getEmail())
                .messageBody(transfer.getAmount()+" is transferred from your account to "+receiver.getFirstName()+" "+receiver.getLastName()+" with account number "+"******"+transfer.getReceiverAccountNumber().substring(6,10))
                .subject("Amount transferred")
                .build();
        emailService.transferDetails(transferDetails);

        TransferDetails transferDetails1=TransferDetails.builder()
                .recipient(receiver.getEmail())
                .messageBody(transfer.getAmount()+" is credited to your account from "+sender.getFirstName()+" "+sender.getLastName()+" with account number "+"******"+transfer.getSenderAccountNumber().substring(6,10))
                .subject("Amount credited")
                .build();
        emailService.transferDetails(transferDetails1);

        TransactionDto transactionDto= TransactionDto.builder()
                .accountNumber(receiver.getAccountNumber())
                .transactionType("Credit")
                .amount(transfer.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        TransactionDto transactionDto1= TransactionDto.builder()
                .accountNumber(sender.getAccountNumber())
                .transactionType("debit")
                .amount(transfer.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto1);
        return BankResponse.builder()
                .responseMessage(Account.Account_transferred_message)
                .responseCode(Account.Account_transferred_code)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(sender.getAccountNumber())
                        .accountBalance(sender.getAccountBalance())
                        .accountName(sender.getFirstName()+" "+sender.getLastName())
                        .build())
                .build();


    }

}
