//package com.example.Backend_web.service;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    public void sendEmail(String to, String subject, String htmlContent) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlContent, true); // true = HTML
//            mailSender.send(message);
//            System.out.println("Email da duoc gui thanh cong den: " + to);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Loi gui email: " + e.getMessage(), e);
//        }
//    }
//}
