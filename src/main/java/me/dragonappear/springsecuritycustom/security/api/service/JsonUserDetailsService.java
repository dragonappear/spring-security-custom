package me.dragonappear.springsecuritycustom.security.api.service;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import me.dragonappear.springsecuritycustom.repository.AccountRepository;
import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class JsonUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("No user found with email : " + email);
        }

        Set<String> roleSet = account
                .getAccountRoles().stream().map(role -> role.getRoleName()).collect(Collectors.toSet());

        List<SimpleGrantedAuthority> authorities =
                roleSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new AccountContext(account, authorities);

    }
}
