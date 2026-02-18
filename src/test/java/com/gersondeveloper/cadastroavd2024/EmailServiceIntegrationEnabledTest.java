package com.gersondeveloper.cadastroavd2024;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.gersondeveloper.cadastroavd2024.domain.entities.User;
import com.gersondeveloper.cadastroavd2024.infra.services.EmailService;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@TestPropertySource(
    properties = {
      "mail.enabled=true",
      "MAIL_FROM=test-from@example.com",
      "api.mail.host=localhost",
      "api.mail.port=2525"
    })
public class EmailServiceIntegrationEnabledTest {

  @Autowired private EmailService emailService;

  @MockitoBean private JavaMailSender mailSender;

  private ArgumentCaptor<SimpleMailMessage> messageCaptor;

  @BeforeEach
  void setUp() {
    messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
  }

  @Test
  void sendTokenEmail_shouldComposeAndSendMessage_whenMailEnabled() {
    String to = "user@test.com";
    String token = "ignored-token"; // token currently not interpolated in body

    emailService.sendTokenEmail(to, token);

    verify(mailSender, times(1)).send(messageCaptor.capture());
    SimpleMailMessage msg = messageCaptor.getValue();
    assertThat(msg.getFrom(), equalTo("test-from@example.com"));
    assertThat(msg.getTo(), arrayContaining(to));
    assertThat(msg.getSubject(), equalTo("Confirmação de cadastro"));
    assertThat(
        msg.getText(),
        allOf(containsString("Obrigado por se cadastrar"), containsString("change_the_password")));
  }

  @Test
  void sendEmail_shouldUseProvidedSubjectAndBody_whenMailEnabled() {
    String to = "plain@test.com";
    String subject = "Hello";
    String body = "This is a test body";

    emailService.sendEmail(to, subject, body);

    verify(mailSender, times(1)).send(messageCaptor.capture());
    SimpleMailMessage msg = messageCaptor.getValue();
    assertThat(msg.getFrom(), equalTo("test-from@example.com"));
    assertThat(msg.getTo(), arrayContaining(to));
    assertThat(msg.getSubject(), equalTo(subject));
    assertThat(msg.getText(), equalTo(body));
  }

  @Test
  void sendConfirmationEmail_shouldDelegateToSendTokenEmail_whenMailEnabled() {
    User user =
        User.builder()
            .email("confirm@test.com")
            .name("Confirm User")
            .password("change_the_password")
            .build();

    emailService.sendConfirmationEmail(user, "any-token");

    verify(mailSender, times(1)).send(messageCaptor.capture());
    SimpleMailMessage msg = messageCaptor.getValue();
    assertThat(msg.getTo(), arrayContaining("confirm@test.com"));
    assertThat(msg.getSubject(), equalTo("Confirmação de cadastro"));
  }
}
