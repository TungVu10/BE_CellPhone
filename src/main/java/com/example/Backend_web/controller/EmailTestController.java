//package com.example.Backend_web.controller;
//
//import com.example.Backend_web.service.EmailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class EmailTestController {
//
//    private final EmailService emailService;
//
//    @GetMapping("/send-test-email")
//    public String sendTestEmail(
//            @RequestParam String to,
//            @RequestParam String subject,
//            @RequestParam String content
//    ) {
//        emailService.sendEmail(to, subject, content);
//        return "Da gui email test den: " + to;
//    }
//}
