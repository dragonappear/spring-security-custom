package me.dragonappear.springsecuritycustom.security.web.init;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.service.RoleHierarchyService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityInitializer implements ApplicationRunner {
    private final RoleHierarchyService roleHierarchyService;
    private final RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String allHierarchy = roleHierarchyService.findAllHierarchy();
        roleHierarchy.setHierarchy(allHierarchy);
    }
}
