package org.example.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;


@Service
public class GmailService {

    private final Gmail gmailService;

    public GmailService(Gmail gmailService) {
        this.gmailService = gmailService;
    }

    public String getLatestEmailFromSender(String senderEmail) {
        try {
            System.out.println("[GmailService] controllo email da " + senderEmail);

            ListMessagesResponse response = gmailService.users().messages().list("me")
                    .setQ("from:" + senderEmail)
                    .setMaxResults(1L)
                    .execute();
            List<Message> messages = response.getMessages();

            if (messages == null || messages.isEmpty()) {
                return "Nessuna email trovata da " + senderEmail + ".";
            }

            Message fullMessage = gmailService.users().messages().get("me", messages.get(0).getId()).execute();
            return fullMessage.getSnippet();

        } catch (IOException e) {
            return "Errore nel recupero dell'email: " + e.getMessage();
        }
    }

    public String sendEmail(String to, String subject, String bodyText) {
        try {
            MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
            mimeMessage.setFrom(new InternetAddress("me"));
            mimeMessage.setRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(bodyText);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            mimeMessage.writeTo(buffer);
            String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());

            Message message = new Message();
            message.setRaw(encodedEmail);

            gmailService.users().messages().send("me", message).execute();

            return "Email inviata correttamente a " + to;

        } catch (Exception e) {
            return "Errore invio email: " + e.getMessage();
        }
    }
}