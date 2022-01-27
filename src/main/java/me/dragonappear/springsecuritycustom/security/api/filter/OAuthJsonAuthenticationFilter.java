package me.dragonappear.springsecuritycustom.security.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dragonappear.springsecuritycustom.domain.dto.AccountDto;
import me.dragonappear.springsecuritycustom.security.api.token.OAuthJsonAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static me.dragonappear.springsecuritycustom.security.util.WebUtil.isJwt;

public class OAuthJsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper objectMapper = new ObjectMapper();

    public OAuthJsonAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/login/oauth","POST"));;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isJwt(request)) {
            throw new IllegalArgumentException("Authentication is no supported");
        }
        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if (StringUtils.isEmpty(accountDto.getEmail())) {
            throw new IllegalArgumentException("Username is Empty");
        }

        OAuthJsonAuthenticationToken oAuthJsonAuthenticationToken = new OAuthJsonAuthenticationToken(accountDto.getEmail(), accountDto.getPassword());
        return getAuthenticationManager().authenticate(oAuthJsonAuthenticationToken);
    }
}
