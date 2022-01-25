package me.dragonappear.springsecuritycustom.security.web.oauth2;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import me.dragonappear.springsecuritycustom.domain.entity.Role;
import me.dragonappear.springsecuritycustom.repository.AccountRepository;
import me.dragonappear.springsecuritycustom.repository.RoleRepository;
import me.dragonappear.springsecuritycustom.security.web.oauth2.principal.OAuth2Principal;
import me.dragonappear.springsecuritycustom.security.web.oauth2.provider.GoogleUserInfo;
import me.dragonappear.springsecuritycustom.security.web.oauth2.provider.OAuth2UserInfo;
import me.dragonappear.springsecuritycustom.security.web.service.AccountContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class OAuth2AccountService extends DefaultOAuth2UserService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest,oAuth2User);
    }

    public OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        Optional<Account> search = accountRepository
                .findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        Account account;
        if (search.isPresent()) {
            account = search.get();
            account.setPassword(passwordEncoder.encode(userRequest.getAccessToken().getTokenValue()));
            account.setEmail(oAuth2UserInfo.getEmail());
        } else {
            Role role = roleRepository.findByRoleName("ROLE_USER");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            account = Account.builder()
                    .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            accountRepository.save(account);
            account.setAccountRoles(roles);
            account.setPassword(passwordEncoder.encode(userRequest.getAccessToken().getTokenValue()));
        }

        List<SimpleGrantedAuthority> authorities = account.getAccountRoles()
                .stream().map(role -> role.getRoleName()).collect(Collectors.toSet())
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new OAuth2Principal(account, authorities, oAuth2User.getAttributes());
    }
}
