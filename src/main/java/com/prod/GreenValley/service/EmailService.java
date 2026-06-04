package com.prod.GreenValley.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;

import com.prod.GreenValley.DTO.ProductStockDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${backup.toAddress}")
    private String[] toAddresses;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress); // sender's email
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendProductStockReport(List<ProductStockDTO> products, String toEmail)
            throws MessagingException, IOException {
        byte[] excelData = ExcelService.generateProductStockExcel(products);

        MimeMessage message = mailSender.createMimeMessage();
        System.out.println("toEmail---------------->>>> "+toEmail);
        String[] toAddresses = toEmail.split(",");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromAddress); // Sender (must match your mail.username if using Gmail)
        helper.setTo(toAddresses);
        helper.setSubject("Product Stock Report");
        helper.setText("Hi,\n\nPlease find attached the product stock report.\n\nRegards,\nYour Application");
        helper.addAttachment("product_stock.xlsx", new ByteArrayResource(excelData));

        mailSender.send(message);
        
    }


    public void sendBackUpFileTOAdmin(String to, String subject, String body, String backupFilePath) throws MessagingException{
         MimeMessage message = mailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message, true);
            System.out.println("toEmail---------------->>>> "+fromAddress);
            helper.setFrom(fromAddress);
            helper.setTo(toAddresses);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates HTML format

            // Attach the file to the email
            FileSystemResource file = new FileSystemResource(new File(backupFilePath));
            helper.addAttachment(file.getFilename(), file);
            mailSender.send(message);
    }

}