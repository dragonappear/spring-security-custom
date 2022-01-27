package me.dragonappear.springsecuritycustom.security.api.provider;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.security.api.service.JsonUserDetailsService;
import me.dragonappear.springsecuritycustom.security.api.token.OAuthJsonAuthenticationToken;
import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


@RequiredArgsConstructor
public class OAuthJsonAuthenticationProvider implements AuthenticationProvider {
    private final JsonUserDetailsService jsonUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        AccountContext accountContext = (AccountContext) jsonUserDetailsService.loadUserByUsername(email);
        return new OAuthJsonAuthenticationToken(accountContext, null, accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(OAuthJsonAuthenticationToken.class);
    }
}
