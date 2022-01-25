package me.dragonappear.springsecuritycustom.listener;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Account;
import me.dragonappear.springsecuritycustom.domain.entity.Role;
import me.dragonappear.springsecuritycustom.repository.AccountRepository;
import me.dragonappear.springsecuritycustom.repository.ResourceRepository;
import me.dragonappear.springsecuritycustom.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final PasswordEncoder passwordEncoder;
    private static AtomicInteger count = new AtomicInteger(0);

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        setupSecurityResources();
    }

    @Transactional
    public void setupSecurityResources() {
        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        Role userRole = createRoleIfNotFound("ROLE_USER", "유저");
        createUserIfNotFound("admin", "admin@admin.com", "pass", Set.of(adminRole));
        createUserIfNotFound("manager", "manager@manager.com", "pass", Set.of(managerRole));
        createUserIfNotFound("user", "user@user.com", "pass", Set.of(userRole));
    }

    @Transactional
    public Account createUserIfNotFound(String username, String email, String password, Set<Role> roles) {
        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            account = Account.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .accountRoles(roles)
                    .build();
        }
        return accountRepository.save(account);
    }

    @Transactional
    public Role createRoleIfNotFound(String roleName,String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }
        return roleRepository.save(role);
    }
}
