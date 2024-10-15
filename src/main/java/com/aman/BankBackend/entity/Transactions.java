package com.aman.BankBackend.entity;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="transaction")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionsID;

    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private  String status;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

}
