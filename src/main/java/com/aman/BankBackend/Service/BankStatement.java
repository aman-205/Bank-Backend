package com.aman.BankBackend.Service;

import com.aman.BankBackend.DTO.EmailDetails;
import com.aman.BankBackend.Repo.TransactionRepo;
import com.aman.BankBackend.Repo.UserRepo;
import com.aman.BankBackend.entity.Transactions;


import com.aman.BankBackend.entity.User;
import com.itextpdf.text.*;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class BankStatement {

    @Autowired
    private TransactionRepo transactionRepo;

    private UserRepo userRepo;
    private  EmailService emailService;
    private static final String FILE="C:\\Users\\AMAN BISHT\\Desktop\\college\\Backend_project\\myStatement.pdf";

    public List<Transactions> generate(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        // Trim the date strings to remove any extra spaces
        LocalDate start = LocalDate.parse(startDate.trim(), DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate.trim(), DateTimeFormatter.ISO_DATE);

        // Filter transactions based on account number and date range
        List<Transactions> list= transactionRepo.findAll().stream()
                .filter(transactions -> transactions.getAccountNumber().equals(accountNumber))
                .filter(transactions -> {
                    LocalDate transactionDate = transactions.getCreatedAt().toLocalDate();  // Assuming getCreatedAt() returns LocalDateTime
                    return (transactionDate.isEqual(start) || transactionDate.isAfter(start)) &&
                            (transactionDate.isEqual(end) || transactionDate.isBefore(end));
                })
                .toList();

        User user= userRepo.findByAccountNumber(accountNumber);
        String customerName= user.getFirstName()+" "+ user.getLastName();
        Rectangle size= new Rectangle(PageSize.A4);
        Document doc= new Document(size);
        log.info("Setting size of doc");
        OutputStream outputStream= new FileOutputStream(FILE);
        PdfWriter.getInstance(doc,outputStream);
        doc.open();
        PdfPTable bankInfoTable=new PdfPTable(1);
        PdfPCell bankName= new PdfPCell(new Phrase("Custom Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.LIGHT_GRAY);
        bankName.setPadding(20f);

        PdfPCell bankAddress= new PdfPCell(new Phrase("Somewhere in the universe"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo= new PdfPTable(2);
        PdfPCell customerInfo= new PdfPCell(new Phrase("Start Date: "+startDate));
        customerInfo.setBorder(0);
        PdfPCell statement=new PdfPCell((new Phrase("Statement of account")));
        statement.setBorder(0);
        PdfPCell stopDate= new PdfPCell(new Phrase("End Date: "+endDate));
        stopDate.setBorder(0);

        PdfPCell name= new PdfPCell(new Phrase("Customer Name: "+customerName));
        name.setBorder(0);
        PdfPCell space= new PdfPCell();
        PdfPCell address= new PdfPCell(new Phrase("Customer Address: "+user.getAddress()));
        address.setBorder(0);


        PdfPTable transactionTable= new PdfPTable(4);
        PdfPCell date= new PdfPCell(new Phrase("Date"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.LIGHT_GRAY);


        PdfPCell transactionType= new PdfPCell(new Phrase("Transaction Type"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.LIGHT_GRAY);


        PdfPCell transactionAmount= new PdfPCell(new Phrase("Transaction Amount"));
        transactionAmount.setBorder(0);
        transactionAmount.setBackgroundColor(BaseColor.LIGHT_GRAY);


        PdfPCell status= new PdfPCell(new Phrase("Status"));
        status.setBorder(0);
        status.setBackgroundColor(BaseColor.LIGHT_GRAY);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        list.forEach(transactions -> {
            transactionTable.addCell(new Phrase(transactions.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transactions.getTransactionType()));
            transactionTable.addCell(new Phrase(transactions.getAmount().toString()));
            transactionTable.addCell(new Phrase(transactions.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        doc.add(bankInfoTable);
        doc.add(statementInfo);
        doc.add(transactionTable);

        doc.close();

        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Account statement")
                .messageBody("This the statement of your Account ")
                .attachment(FILE)
                .build();

        emailService.sendMail(emailDetails);
        return list;
    }

    private void design(List<Transactions> transactions) throws FileNotFoundException, DocumentException {




    }
}
