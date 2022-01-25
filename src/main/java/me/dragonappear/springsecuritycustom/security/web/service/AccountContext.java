package me.dragonappear.springsecuritycustom.security.web.service;

import lombok.Data;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class AccountContext extends User implements OAuth2User {
    private Account account;
    private Map<String, Object> attributes;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities,Map<String, Object> attributes) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return this.account.getId()+"";
    }
}
