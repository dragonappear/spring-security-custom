package me.dragonappear.springsecuritycustom.security.listener;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.*;
import me.dragonappear.springsecuritycustom.repository.*;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ResourceRepository resourceRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;
    private final AccessIpRepository accessIpRepository;

    private static AtomicInteger count = new AtomicInteger(0);

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        setupSecurityResources();
        setupAccessIpData();
        alreadySetup = true;
    }

    public void setupSecurityResources() {
        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);
        createResourceIfNotFound("/admin/**", "", roles, "url");
        createAccountIfNotFound("admin", "admin@admin.com", "pass", roles);
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저권한");
        Role userRole = createRoleIfNotFound("ROLE_USER", "사용자권한");
        createResourceIfNotFound("/mypage", "",  Set.of(userRole), "url");
        createResourceIfNotFound("/messages", "",  Set.of(managerRole), "url");
        createResourceIfNotFound("/config", "",  roles, "url");
        createAccountIfNotFound("user", "user@admin.com", "pass", Set.of(userRole));
        createAccountIfNotFound("manager", "manager@admin.com", "pass", Set.of(managerRole));
        createRoleHierarchyIfNotFound(managerRole, adminRole);
        createRoleHierarchyIfNotFound(userRole, managerRole);
    }

    public void createAccountIfNotFound(final String userName, final String email, final String password, Set<Role> roleSet) {
        Account account = accountRepository.findByUsername(userName);
        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
            account = accountRepository.save(account);
        }
        account.setAccountRoles(roleSet);

    }
    public void createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resource resource = resourceRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resource == null) {
            resource = Resource.builder()
                    .resourceName(resourceName)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(Long.valueOf(count.incrementAndGet()))
                    .build();
            resource = resourceRepository.save(resource);
        }
        resource.setRoleSet(roleSet);

    }

    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
            role = roleRepository.save(role);
        }
        return roleRepository.save(role);
    }

    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {
        RoleHierarchy parent = roleHierarchyRepository.findByName(parentRole.getRoleName());
        if (parent == null) {
            parent = RoleHierarchy.builder()
                    .name(parentRole.getRoleName())
                    .build();
            parent = roleHierarchyRepository.save(parent);
        }

        RoleHierarchy child = roleHierarchyRepository.findByName(childRole.getRoleName());
        if (child == null) {
            child = RoleHierarchy.builder()
                    .name(childRole.getRoleName())
                    .build();
            roleHierarchyRepository.save(child);
        }

        child.setParentName(parent);
    }

    public void setupAccessIpData() {
        AccessIp byIpAddress = accessIpRepository.findByIpAddress("0:0:0:0:0:0:0:1");
        if (byIpAddress == null) {
            AccessIp accessIp = AccessIp.builder()
                    .ipAddress("0:0:0:0:0:0:0:1")
                    .build();
            accessIpRepository.save(accessIp);
        }
    }

}
