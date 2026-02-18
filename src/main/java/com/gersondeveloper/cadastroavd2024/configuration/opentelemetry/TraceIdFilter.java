package com.gersondeveloper.cadastroavd2024.configuration.opentelemetry;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
class TraceIdFilter extends OncePerRequestFilter {

  private final Tracer tracer;

  TraceIdFilter(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String traceId = getTraceId();
    if (traceId != null) {
      response.setHeader("X-Trace-Id", traceId);
    }
    filterChain.doFilter(request, response);
  }

  private @Nullable String getTraceId() {
    TraceContext context = this.tracer.currentTraceContext().context();
    return context != null ? context.traceId() : null;
  }
}
