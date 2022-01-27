package me.dragonappear.springsecuritycustom.security.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dragonappear.springsecuritycustom.security.exception.JwtInvalidException;
import me.dragonappear.springsecuritycustom.security.exception.JwtNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid Username or Password";
        if(exception instanceof BadCredentialsException) {
            errorMessage = "Invalid Username or Password";
        } else if(exception instanceof DisabledException) {
            errorMessage = "Locked";
        } else if(exception instanceof CredentialsExpiredException) {
            errorMessage = "Expired password";
        } else if (exception instanceof JwtInvalidException) {
            errorMessage = exception.getMessage();
        }else if (exception instanceof JwtNotFoundException) {
            errorMessage = exception.getMessage();
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        HashMap<String,String> map = new HashMap<>();
        map.put("message", errorMessage);
        map.put("code", HttpStatus.UNAUTHORIZED.toString());
        objectMapper.writeValue(response.getWriter(),map);
    }
}
