package me.dragonappear.springsecuritycustom.security.web.common;

import lombok.Data;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

@Data
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {
    private String secretKey;
    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");
    }
}
