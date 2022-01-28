package me.dragonappear.springsecuritycustom.security.web.config;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.security.web.factory.MethodResourceMapFactoryBean;
import me.dragonappear.springsecuritycustom.security.web.interceptor.CustomMethodSecurityInterceptor;
import me.dragonappear.springsecuritycustom.security.web.processor.ProtectPointcutPostProcessor;
import me.dragonappear.springsecuritycustom.service.SecurityResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled= true,securedEnabled= true)
@RequiredArgsConstructor
@Configuration
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private final SecurityResourceService securityResourceService;

    @Bean
    public CustomMethodSecurityInterceptor customMethodSecurityInterceptor(MapBasedMethodSecurityMetadataSource methodSecurityMetadataSource) {
        CustomMethodSecurityInterceptor customMethodSecurityInterceptor =  new CustomMethodSecurityInterceptor();
        customMethodSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        customMethodSecurityInterceptor.setAfterInvocationManager(afterInvocationManager());
        customMethodSecurityInterceptor.setSecurityMetadataSource(methodSecurityMetadataSource);
        RunAsManager runAsManager = runAsManager();
        if (runAsManager != null) {
            customMethodSecurityInterceptor.setRunAsManager(runAsManager);
        }

        return customMethodSecurityInterceptor;
    }

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

    @Bean
    public ProtectPointcutPostProcessor protectPointcutPostProcessor(){
        ProtectPointcutPostProcessor protectPointcutPostProcessor = new ProtectPointcutPostProcessor(mapBasedMethodSecurityMetadataSource());
        protectPointcutPostProcessor.setPointcutMap(pointcutResourceMapFactoryBean().getObject());
        return protectPointcutPostProcessor;
    }

    @Bean
    public  MethodResourceMapFactoryBean pointcutResourceMapFactoryBean() {
        return new MethodResourceMapFactoryBean(securityResourceService,"pointcut");
    }


}
