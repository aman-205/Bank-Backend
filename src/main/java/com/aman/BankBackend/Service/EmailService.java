package com.aman.BankBackend.Service;

import com.aman.BankBackend.DTO.EmailDetails;

public interface EmailService  {
    void emailMessageAlert(EmailDetails emailDetails);
}
