package me.dragonappear.springsecuritycustom.security.api.filter;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.security.api.handler.JsonAuthenticationFailureHandler;
import me.dragonappear.springsecuritycustom.security.api.provider.TokenProvider;
import me.dragonappear.springsecuritycustom.security.api.service.JsonUserDetailsService;
import me.dragonappear.springsecuritycustom.security.exception.JwtNotFoundException;
import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtValidationFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final JsonUserDetailsService jsonUserDetailsService;
    private final JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
