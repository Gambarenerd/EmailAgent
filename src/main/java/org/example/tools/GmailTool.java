package org.example.tools;

import org.example.service.GmailService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class GmailTool {

    private final GmailService gmailService;

    public GmailTool(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @Tool(name = "check_latest_email", description = "Controlla l'ultima email ricevuta da un determinato indirizzo")
    public String checkEmail(@ToolParam String emailSender){
        System.out.println("[GmailTool:checkEmail] emailSender: " + emailSender);
        return gmailService.getLatestEmailFromSender(emailSender);
    }

    @Tool(name = "reply_latest_email", description = "Rispondi all'ultima email ricevuta da un determinato indirizzo")
    public String sendEmail(@ToolParam String to, @ToolParam String subject, @ToolParam String bodyText){
        System.out.println("[GmailTool:sendEmail] Sending email to" + to + " with subject: " + subject + " and body: " + bodyText);
        return gmailService.sendEmail(to, subject, bodyText);
    }
}