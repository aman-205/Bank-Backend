package com.aman.BankBackend.Service;


import com.aman.BankBackend.DTO.BankResponse;
import com.aman.BankBackend.DTO.CreditDebitRequest;
import com.aman.BankBackend.DTO.EnquiryRequest;
import com.aman.BankBackend.DTO.UserRequest;
import com.aman.BankBackend.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAll();
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
}
