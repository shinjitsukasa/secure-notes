package com.secure.notes.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetUrl) {
        jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Password Reset Request");

            // HTML content with inline CSS
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "  .email-container {" +
                    "    font-family: Arial, sans-serif;" +
                    "    line-height: 1.6;" +
                    "    color: #333;" +
                    "    max-width: 600px;" +
                    "    margin: 0 auto;" +
                    "    padding: 20px;" +
                    "    border: 1px solid #ddd;" +
                    "    border-radius: 8px;" +
                    "    background-color: #f9f9f9;" +
                    "  }" +
                    "  .email-header {" +
                    "    text-align: center;" +
                    "    font-size: 24px;" +
                    "    color: #4CAF50;" +
                    "    margin-bottom: 20px;" +
                    "  }" +
                    "  .email-body {" +
                    "    font-size: 16px;" +
                    "    margin-bottom: 20px;" +
                    "  }" +
                    "  .reset-button {" +
                    "    display: inline-block;" +
                    "    padding: 10px 20px;" +
                    "    font-size: 16px;" +
                    "    color: #fff;" +
                    "    background-color: #4CAF50;" +
                    "    text-decoration: none;" +
                    "    border-radius: 5px;" +
                    "  }" +
                    "  .reset-button:hover {" +
                    "    background-color: #45a049;" +
                    "  }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='email-container'>" +
                    "  <div class='email-header'>Password Reset Request</div>" +
                    "  <div class='email-body'>" +
                    "    <p>Hello,</p>" +
                    "    <p>We received a request to reset your password. Click the button below to reset it:</p>" +
                    "    <p><a href='" + resetUrl + "' class='reset-button'>Reset Password</a></p>" +
                    "    <p>If you did not request this, please ignore this email.</p>" +
                    "    <p>Thank you,<br>Secure Notes Team</p>" +
                    "  </div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); // Set the email content as HTML
            mailSender.send(mimeMessage);

        } catch (MailException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Failed to create email message", e);
        }
    }
}