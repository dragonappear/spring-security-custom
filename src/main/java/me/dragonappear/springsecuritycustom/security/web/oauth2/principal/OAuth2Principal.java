package me.dragonappear.springsecuritycustom.security.web.oauth2.principal;

import lombok.Data;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class OAuth2Principal extends AccountContext implements OAuth2User {
    private Map<String, Object> attributes;

    public OAuth2Principal(Account account, Collection<? extends GrantedAuthority> authorities,Map<String, Object> attributes) {
        super(account, authorities);
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return this.getAccount().getId()+"";
    }
}
