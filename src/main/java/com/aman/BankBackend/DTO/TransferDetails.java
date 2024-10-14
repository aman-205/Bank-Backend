package com.aman.BankBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDetails {
    private String recipient;
    private  String messageBody;
    private String subject;
   // private BigDecimal amount;
}
