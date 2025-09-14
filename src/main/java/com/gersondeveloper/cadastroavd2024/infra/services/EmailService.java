package com.gersondeveloper.cadastroavd2024.infra.services;

import com.gersondeveloper.cadastroavd2024.configuration.MailConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailConfiguration mailConfiguration;

    public EmailService(JavaMailSender mailSender, MailConfiguration mailConfiguration) {
        this.mailSender = mailSender;
        this.mailConfiguration = mailConfiguration;
    }

    public void sendTokenEmail(String to, String token) {
        if (!mailConfiguration.isEnabled()) {
            return;
        }
        try{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfiguration.getFrom());
        message.setTo(to);
        message.setSubject("Confirmação de cadastro");
            message.setText(
                    "Olá,\n\n" +
                            "Obrigado por se cadastrar. " +
                            "Para confirmar seu registro, faça login usando o seu email\n\n" +
                            "e a senha temporária 'change_the_password'\n\n" +
                            "Se não foi você, ignore esta mensagem.\n"
            );

            mailSender.send(message);
        System.out.println("Token enviado para " + to);
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendEmail(String to, String subject, String body) {
        if (!mailConfiguration.isEnabled()) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfiguration.getFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
