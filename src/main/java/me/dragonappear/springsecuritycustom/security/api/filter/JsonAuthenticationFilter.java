package me.dragonappear.springsecuritycustom.security.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dragonappear.springsecuritycustom.domain.dto.AccountDto;
import me.dragonappear.springsecuritycustom.security.api.token.JsonAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static me.dragonappear.springsecuritycustom.security.util.WebUtil.isAjax;

public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper objectMapper = new ObjectMapper();

    public JsonAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isAjax(request)) {
            throw new IllegalArgumentException("Authentication is no supported");
        }
        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if (StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is Empty");
        }

        JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(accountDto.getUsername(),accountDto.getPassword());
        return getAuthenticationManager().authenticate(jsonAuthenticationToken);
    }
}
