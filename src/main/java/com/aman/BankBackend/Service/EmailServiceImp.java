package com.aman.BankBackend.Service;

import com.aman.BankBackend.DTO.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class EmailServiceImp implements  EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void emailMessageAlert(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mailMessage= new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("mail send");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
