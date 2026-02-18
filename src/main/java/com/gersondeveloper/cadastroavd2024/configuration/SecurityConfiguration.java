package com.gersondeveloper.cadastroavd2024.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gersondeveloper.cadastroavd2024.infra.security.SecurityFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

  @Autowired private SecurityFilter securityFilter;

  @Autowired private Environment env;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("dev");
    boolean isTest = Arrays.asList(env.getActiveProfiles()).contains("test");

    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable);

    if (isTest || isDev) {
      http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    } else {
      http.authorizeHttpRequests(
              auth ->
                  auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                      .permitAll()
                      // retirei o swagger de prod, mas se quiser deixar, é só descomentar a linha
                      // abaixo
                      // .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                      // "/api/auth/**").permitAll()
                      .anyRequest()
                      .authenticated())
          .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
