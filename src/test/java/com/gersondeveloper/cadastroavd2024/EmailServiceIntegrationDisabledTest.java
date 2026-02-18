package com.gersondeveloper.cadastroavd2024;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.gersondeveloper.cadastroavd2024.infra.services.EmailService;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@TestPropertySource(properties = {"mail.enabled=false", "mail.from=test-from@example.com"})
public class EmailServiceIntegrationDisabledTest {

  @Autowired private EmailService emailService;

  @MockitoBean private JavaMailSender mailSender;

  @Test
  void shouldNotSendTokenEmail_whenMailDisabled() {
    emailService.sendTokenEmail("nobody@test.com", "token");
    verifyNoInteractions(mailSender);
  }

  @Test
  void shouldNotSendGenericEmail_whenMailDisabled() {
    emailService.sendEmail("nobody@test.com", "Subject", "Body");
    verifyNoInteractions(mailSender);
  }
}
