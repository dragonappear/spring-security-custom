package me.dragonappear.springsecuritycustom.security.web.config;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.security.web.factory.MethodResourceMapFactoryBean;
import me.dragonappear.springsecuritycustom.service.SecurityResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled= true,securedEnabled= true)
@RequiredArgsConstructor
@Configuration
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final SecurityResourceService securityResourceService;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource();
    }

    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
        return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
    }

    @Bean
    public  MethodResourceMapFactoryBean methodResourcesMapFactoryBean() {
        return new MethodResourceMapFactoryBean(securityResourceService,"method");
    }

}
