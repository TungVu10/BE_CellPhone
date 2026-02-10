package com.example.Backend_web.service;


import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
//Gửi về Email sau khi Khách hàng thanh toán thành công
public class GmailApiService {

    private static final String APPLICATION_NAME = "Spring Boot Gmail API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES =
            List.of("https://www.googleapis.com/auth/gmail.send");

    private Gmail getGmailService() throws Exception {

        InputStream in = getClass().getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new RuntimeException("Khong tim thay credentials.json");
        }

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JSON_FACTORY,
                        clientSecrets,
                        SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                        .setAccessType("offline")
                        .build();

        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder().setPort(8888).build();

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
        ).setApplicationName(APPLICATION_NAME).build();
    }

    //Gửi hóa đơn về Email Khách hàng
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws Exception {

        String rawMessage =
                "To: " + to + "\r\n" +
                        "Subject: " + subject + "\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n\r\n" +
                        htmlContent;

        Message message = new Message();
        message.setRaw(Base64.getUrlEncoder()
                .encodeToString(rawMessage.getBytes(StandardCharsets.UTF_8)));

        Gmail service = getGmailService();
        service.users().messages().send("me", message).execute();

        System.out.println("Gui email thanh cong qua Gmail API");
    }

    //Gửi gmail xác nhận đơn hàng
    public void sendOrderSuccessEmail(
            String customerEmail,
            String customerName,
            Long orderId,
            String totalPrice
    ) throws Exception {

        String html = """
        <h2>Dat hang thanh cong</h2>
        <p>Xin chao <b>%s</b>,</p>
        <p>Don hang <b>#%d</b> cua ban da duoc tao thanh cong.</p>

        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <td><b>Ma don hang</b></td>
                <td>#%d</td>
            </tr>
            <tr>
                <td><b>Tong tien</b></td>
                <td>%s VND</td>
            </tr>
        </table>

        <p>Cam on ban da mua hang!</p>
    """.formatted(customerName, orderId, orderId, totalPrice);

        sendHtmlEmail(
                customerEmail,
                "Xac nhan don hang #" + orderId,
                html
        );
    }

}

