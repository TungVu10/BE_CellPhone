package com.example.Backend_web;

import com.example.Backend_web.service.GmailApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendWebApplication.class, args);
	}


	//Gửi email đến Khách hàng
//	@Bean
//	CommandLineRunner testGmail(GmailApiService gmailApiService) {
//		return args -> {
//			gmailApiService.sendHtmlEmail(
//					//"tungnguyen10onion@gmail.com",
//					"tungvuonion@gmail.com",
//					"Test Gmail API",
//					"<h1>Hello</h1><p>Email gui bang Gmail API</p>"
//			);
//		};
//	}

}


//package com.example.Backend_web;
//
//import com.example.Backend_web.service.EmailService;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationContext;
//
//@SpringBootApplication
//public class BackendWebApplication {
//
//	public static void main(String[] args) {
//		ApplicationContext context = SpringApplication.run(BackendWebApplication.class, args);
//
//		// Lấy bean EmailService
//		EmailService emailService = context.getBean(EmailService.class);
//
//		// Test gửi email trực tiếp
//		emailService.sendEmail(
//				"tungnguyen10onion@gmail.com",         // Email người nhận
//				"TestHTML từ Spring Boot",            // Tiêu đề
//				"<h1>Hello</h1><p>Day la noi dung test email</p>"  // Nội dung HTML
//		);
//
//		System.out.println("Da goi EmailService xong!");
//	}
//}
