package me.dragonappear.springsecuritycustom.security.api.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import me.dragonappear.springsecuritycustom.repository.AccountRepository;
import me.dragonappear.springsecuritycustom.security.exception.JwtInvalidException;
import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class TokenProvider implements InitializingBean {
    private final ObjectMapper objectMapper;
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final AccountRepository accountRepository;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds,
                         ObjectMapper objectMapper,
                         AccountRepository accountRepository) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
        this.objectMapper = objectMapper;
        this.accountRepository = accountRepository;
    }

    public void createToken(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Account account = ((AccountContext) (authentication.getPrincipal())).getAccount();

        String jwt = JWT
                .create()
                .withSubject(account.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .withClaim("email", account.getEmail())
                .withClaim("username", account.getUsername())
                .sign(Algorithm.HMAC512(secret));

        response.addHeader(AUTHORIZATION, "Bearer "+jwt);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        HashMap<String,String> map = new HashMap<>();
        map.put("code", HttpStatus.OK.toString());
        objectMapper.writeValue(response.getWriter(),map);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public String validateToken(String token) {
        try {
            String email = JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
                    .getClaim("email").asString();
            return email;
        } catch (TokenExpiredException e) {
            throw new JwtInvalidException("Jwt is expired");
        } catch (Exception e) {
            throw new JwtInvalidException("Jwt not valid");
        }
    }
}
