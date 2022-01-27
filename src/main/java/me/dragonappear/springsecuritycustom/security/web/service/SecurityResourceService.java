package me.dragonappear.springsecuritycustom.security.web.service;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.domain.entity.Resource;
import me.dragonappear.springsecuritycustom.repository.ResourceRepository;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SecurityResourceService {
    private ResourceRepository resourceRepository;

    public SecurityResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> all = new LinkedHashMap<>();
        List<Resource> resources = resourceRepository.findAllResource();

        resources
                .forEach(resource -> {
                    List<ConfigAttribute> configAttributes = new ArrayList<>();
                    resource.getRoleSet().forEach(role -> {
                        configAttributes.add(new SecurityConfig(role.getRoleName()));
                    });
                    all.put(new AntPathRequestMatcher(resource.getResourceName()), configAttributes);
                });

        return all;
    }
}
