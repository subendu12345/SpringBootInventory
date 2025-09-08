package com.prod.GreenValley.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("subendumal94@gmail.com"); // sender's email
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("Email sent successfully to " + toEmail);
    }

    public void sendProductStockReport(List<ProductStockDTO> products, String toEmail)
            throws MessagingException, IOException {
        byte[] excelData = ExcelService.generateProductStockExcel(products);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("surya.bakra@gmail.com"); // Sender (must match your mail.username if using Gmail)
        helper.setTo(toEmail);
        helper.setSubject("Product Stock Report");
        helper.setText("Hi,\n\nPlease find attached the product stock report.\n\nRegards,\nYour Application");
        helper.addAttachment("product_stock.xlsx", new ByteArrayResource(excelData));

        mailSender.send(message);
        
    }


    public void sendBackUpFileTOAdmin(String to, String subject, String body, String backupFilePath) throws MessagingException{
        System.out.println("sendBackUpFileTOAdmin calling..................");
         MimeMessage message = mailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("surya.bakra@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates HTML format

            // Attach the file to the email
            FileSystemResource file = new FileSystemResource(new File(backupFilePath));
            helper.addAttachment(file.getFilename(), file);
            mailSender.send(message);
    }

}