package me.dragonappear.springsecuritycustom.security.web.service;

import lombok.Data;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
public class AccountContext extends User {
    private Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }
}