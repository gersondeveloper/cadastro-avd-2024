package com.gersondeveloper.cadastroavd2024.configuration.opentelemetry;

import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class FilterConfiguration {

    @Bean
    HeaderLoggerFilter headerLoggerFilter() {
        return new HeaderLoggerFilter();
    }

    @Bean
    TraceIdFilter addTraceIdFilter(Tracer tracer) {
        return new TraceIdFilter(tracer);
    }

}
