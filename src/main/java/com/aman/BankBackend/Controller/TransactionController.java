package com.aman.BankBackend.Controller;

import com.aman.BankBackend.Service.BankStatement;
import com.aman.BankBackend.entity.Transactions;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
public class TransactionController {

    @Autowired
    private BankStatement bankStatement;
    @GetMapping
    public List<Transactions> generate(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generate(accountNumber,startDate,endDate);

    }

}
