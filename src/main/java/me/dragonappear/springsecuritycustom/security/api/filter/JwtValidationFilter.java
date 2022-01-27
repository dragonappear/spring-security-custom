package me.dragonappear.springsecuritycustom.security.api.filter;

import me.dragonappear.springsecuritycustom.security.api.handler.JsonAuthenticationFailureHandler;
import me.dragonappear.springsecuritycustom.security.api.provider.TokenProvider;
import me.dragonappear.springsecuritycustom.security.api.service.JsonUserDetailsService;
import me.dragonappear.springsecuritycustom.security.exception.JwtNotFoundException;
import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtValidationFilter extends OncePerRequestFilter {
    private TokenProvider tokenProvider;
    private JsonUserDetailsService jsonUserDetailsService;
    private JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api")) {
            return false;
        }
        return true;
    }

    public JwtValidationFilter(TokenProvider tokenProvider, JsonUserDetailsService jsonUserDetailsService,
                               JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler) {
        super();
        this.tokenProvider = tokenProvider;
        this.jsonAuthenticationFailureHandler = jsonAuthenticationFailureHandler;
        this.jsonUserDetailsService = jsonUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String token = resolveToken((HttpServletRequest) request);
            String email = tokenProvider.validateToken(token);
            if (StringUtils.hasText(token) || StringUtils.hasText(email)) {
                AccountContext accountContext = (AccountContext) jsonUserDetailsService.loadUserByUsername(email);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(accountContext, null, accountContext.getAuthorities()));
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            this.jsonAuthenticationFailureHandler
                    .onAuthenticationFailure((HttpServletRequest) request, (HttpServletResponse) response, (AuthenticationException) e);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token)  && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new JwtNotFoundException("Jwt must start with Bearer ");
    }
}
