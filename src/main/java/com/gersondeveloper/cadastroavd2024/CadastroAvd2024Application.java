package com.gersondeveloper.cadastroavd2024;

import com.gersondeveloper.cadastroavd2024.configuration.opentelemetry.ContextPropagationConfiguration;
import com.gersondeveloper.cadastroavd2024.configuration.opentelemetry.FilterConfiguration;
import com.gersondeveloper.cadastroavd2024.configuration.opentelemetry.OpenTelemetryConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({OpenTelemetryConfiguration.class, ContextPropagationConfiguration.class, FilterConfiguration.class})
public class CadastroAvd2024Application {

	public static void main(String[] args) {
		SpringApplication.run(CadastroAvd2024Application.class, args);
	}

}
