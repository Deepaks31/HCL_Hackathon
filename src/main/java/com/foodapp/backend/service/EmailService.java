package com.foodapp.backend.service;

import com.foodapp.backend.entity.Order;
import com.foodapp.backend.entity.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("deepaksuresh3105@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            System.out.println("Email successfully dispatched to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String url = "http://localhost:8080/auth/verify?token=" + token;
        System.out.println("VERIFICATION LINK: " + url);
        
        String html = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333; max-width: 600px; margin: auto; border: 1px solid #ddd; border-top: 5px solid #6c5ce7; border-radius: 8px;'>" +
                      "<h2 style='color: #6c5ce7;'>Welcome to FoodApp! 🍔</h2>" +
                      "<p>Thank you for partnering with us. We're thrilled to have your restaurant on board.</p>" +
                      "<p>To complete your registration and submit your application to the Admin, please verify your email address by clicking the button below:</p>" +
                      "<div style='text-align: center; margin: 30px 0;'>" +
                      "<a href='" + url + "' style='background-color: #6c5ce7; color: white; padding: 12px 25px; text-decoration: none; font-weight: bold; border-radius: 5px; font-size: 16px;'>Verify My Email</a>" +
                      "</div>" +
                      "<p style='font-size: 0.9em; color: #777;'>If the button doesn't work, copy and paste this link into your browser:<br/>" +
                      "<a href='" + url + "' style='color: #6c5ce7;'>" + url + "</a></p>" +
                      "<hr style='border: none; border-top: 1px solid #eee; margin-top: 30px;'/>" +
                      "<p style='font-size: 0.8em; color: #aaa; text-align: center;'>FoodApp Inc. &copy; 2026</p>" +
                      "</div>";
        sendHtmlEmail(toEmail, "Verify your email for FoodApp", html);
    }

    public void sendOrderPlacedEmail(User user, Order order) {
        String html = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333; max-width: 600px; margin: auto; border: 1px solid #ddd; border-top: 5px solid #10b981; border-radius: 8px;'>" +
                      "<h2 style='color: #10b981;'>Order Confirmed! 🎉</h2>" +
                      "<p>Hi " + user.getName() + ",</p>" +
                      "<p>Your order <strong>#" + order.getId() + "</strong> has been successfully placed at <strong>" + order.getRestaurant().getName() + "</strong>.</p>" +
                      "<div style='background-color: #f9fafb; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                      "<h3 style='margin-top: 0;'>Order Summary</h3>" +
                      "<p style='font-size: 1.2em; font-weight: bold; color: #10b981;'>Total: $" + order.getTotalAmount() + "</p>" +
                      "</div>" +
                      "<p>The restaurant is currently preparing your food! You can track the status in your order history.</p>" +
                      "<hr style='border: none; border-top: 1px solid #eee; margin-top: 30px;'/>" +
                      "<p style='font-size: 0.8em; color: #aaa; text-align: center;'>FoodApp Inc. &copy; 2026</p>" +
                      "</div>";
        sendHtmlEmail(user.getEmail(), "Your FoodApp Order #" + order.getId() + " is Confirmed!", html);
    }

    public void sendOrderCancelledEmail(User user, Order order) {
        String html = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333; max-width: 600px; margin: auto; border: 1px solid #ddd; border-top: 5px solid #ef4444; border-radius: 8px;'>" +
                      "<h2 style='color: #ef4444;'>Order Cancelled</h2>" +
                      "<p>Hi " + user.getName() + ",</p>" +
                      "<p>Your order <strong>#" + order.getId() + "</strong> at <strong>" + order.getRestaurant().getName() + "</strong> has been cancelled.</p>" +
                      "<p>If you have already been charged, a refund will be issued to your original payment method automatically within 3-5 business days.</p>" +
                      "<p>We hope to serve you again soon!</p>" +
                      "<hr style='border: none; border-top: 1px solid #eee; margin-top: 30px;'/>" +
                      "<p style='font-size: 0.8em; color: #aaa; text-align: center;'>FoodApp Inc. &copy; 2026</p>" +
                      "</div>";
        sendHtmlEmail(user.getEmail(), "Update on your FoodApp Order #" + order.getId(), html);
    }
}
