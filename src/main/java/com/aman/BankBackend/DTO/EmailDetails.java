package com.aman.BankBackend.DTO;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {
    private  String recipient;
    private  String subject;
    private String messageBody;
    private String attachment;
}
