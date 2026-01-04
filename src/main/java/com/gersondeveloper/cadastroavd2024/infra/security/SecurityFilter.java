package com.gersondeveloper.cadastroavd2024.infra.security;

import com.gersondeveloper.cadastroavd2024.domain.dtos.response.UserResponseDto;
import com.gersondeveloper.cadastroavd2024.domain.interfaces.UserRepository;
import com.gersondeveloper.cadastroavd2024.infra.services.AuthorizationService;
import com.gersondeveloper.cadastroavd2024.infra.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {


    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationService authorizationService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        // 1) Libera preflight e endpoints públicos
        if ("OPTIONS".equalsIgnoreCase(method) || isPublic(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Extrai Bearer token (seu recoveryToken pode já fazer isso)
        String token = recoveryToken(request);
        if (token == null || token.isBlank()) {
            // Sem token → segue sem autenticar (endpoints protegidos darão 401 pelo Security)
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 3) Valida token e autentica
            String username = tokenService.validateToken(token); // retorne null/throw se inválido
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = authorizationService.loadUserByUsername(username);
                if (user != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ex) {
            // Token inválido/expirado → não autentica e segue o fluxo
            // (Opcional: logar em nível debug)
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublic(String uri) {
        // ajuste os padrões conforme sua API pública
        return pathMatcher.match("/api/auth/**", uri) ||
               pathMatcher.match("/api/v1/auth/**", uri) ||
               pathMatcher.match("/api/user/register", uri) ||
               pathMatcher.match("/api/v1/user/register", uri);
    }

    // Exemplo de extractor; use o seu se já existir
    private String recoveryToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}