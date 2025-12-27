package com.gersondeveloper.cadastroavd2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CadastroAvd2024Application {

	public static void main(String[] args) {
		SpringApplication.run(CadastroAvd2024Application.class, args);
	}

}
