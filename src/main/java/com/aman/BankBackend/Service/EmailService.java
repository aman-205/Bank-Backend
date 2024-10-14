package com.aman.BankBackend.Service;

import com.aman.BankBackend.DTO.EmailDetails;
import com.aman.BankBackend.DTO.Transfer;
import com.aman.BankBackend.DTO.TransferDetails;

public interface EmailService  {
    void emailMessageAlert(EmailDetails emailDetails);
    void transferDetails(TransferDetails transferDetails);
}
