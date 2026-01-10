package com.gersondeveloper.cadastroavd2024.configuration;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Getter
    @Value("${mail.enabled:true}")
    private boolean enabled;

    @Value("${api.mail.host:localhost}")
    private String host;

    @Value("${api.mail.port:25}")
    private int port;

    @Value("${api.mail.username:}")
    private String username;

    @Value("${api.mail.password:}")
    private String password;

    @Value("${api.mail.protocol:smtp}")
    private String protocol;

    @Getter
    @Value("${api.mail.from:no-reply@example.com}")
    private String from;

    @Value("${api.mail.properties.mail.smtp.auth:false}")
    private boolean smtpAuth;

    @Value("${api.mail.properties.mail.smtp.starttls.enable:false}")
    private boolean starttls;

    @Value("${api.mail.properties.mail.debug:false}")
    private boolean debug;

    @Value("${api.mail.properties.mail.smtp.ssl.trust:smtp.gmail.com}")
    private String sslTrust;

    @Value("${api.mail.properties.mail.smtp.connectiontimeout:10000}")
    private int connectionTimeoutMs;

    @Value("${api.mail.properties.mail.smtp.timeout:10000}")
    private int readTimeoutMs;

    @Value("${api.mail.properties.mail.smtp.writetimeout:10000}")
    private int writeTimeoutMs;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);
        if (username != null && !username.isBlank()) {
            mailSender.setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            mailSender.setPassword(password);
        }
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", String.valueOf(smtpAuth));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        props.put("mail.debug", String.valueOf(debug));
        // Helpful defaults for Gmail and network stability
        if (sslTrust != null && !sslTrust.isBlank()) {
            props.put("mail.smtp.ssl.trust", sslTrust);
        }
        props.put("mail.smtp.connectiontimeout", String.valueOf(connectionTimeoutMs));
        props.put("mail.smtp.timeout", String.valueOf(readTimeoutMs));
        props.put("mail.smtp.writetimeout", String.valueOf(writeTimeoutMs));

        // Startup hints for misconfiguration (printed once when bean is created)
        if (enabled && (username == null || username.isBlank() || password == null || password.isBlank())) {
            System.err.println("[MAIL WARNING] Mail is enabled but username/password are missing. Configure MAIL_USER and MAIL_PASSWORD (Google App Password recommended).");
        }
        if (enabled && starttls && port == 465) {
            System.err.println("[MAIL INFO] You are using port 465 (implicit SSL). Consider setting starttls.enable=false and using 'mail.smtp.ssl.enable=true' if needed, or switch to port 587 for STARTTLS.");
        }
        return mailSender;
    }

    @Profile("ci")
    @Bean
    public JavaMailSender mailSender() {
        return new JavaMailSenderImpl() {
            @Override
            public void send(@NonNull SimpleMailMessage message) {
                System.out.println("Sending teste email");
            }
        };
    }

}
