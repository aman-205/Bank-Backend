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
public class Transfer {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
}
