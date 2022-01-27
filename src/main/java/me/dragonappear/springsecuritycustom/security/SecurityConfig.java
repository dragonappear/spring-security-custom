package me.dragonappear.springsecuritycustom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.repository.ResourceRepository;
import me.dragonappear.springsecuritycustom.security.api.filter.CustomCorsFilter;
import me.dragonappear.springsecuritycustom.security.api.filter.JsonAuthenticationFilter;
import me.dragonappear.springsecuritycustom.security.api.filter.JwtValidationFilter;
import me.dragonappear.springsecuritycustom.security.api.filter.OAuthJsonAuthenticationFilter;
import me.dragonappear.springsecuritycustom.security.api.handler.JsonAccessDeniedHandler;
import me.dragonappear.springsecuritycustom.security.api.handler.JsonAuthenticationEntryPoint;
import me.dragonappear.springsecuritycustom.security.api.handler.JsonAuthenticationFailureHandler;
import me.dragonappear.springsecuritycustom.security.api.handler.JsonAuthenticationSuccessHandler;
import me.dragonappear.springsecuritycustom.security.api.provider.JsonAuthenticationProvider;
import me.dragonappear.springsecuritycustom.security.api.provider.OAuthJsonAuthenticationProvider;
import me.dragonappear.springsecuritycustom.security.api.provider.TokenProvider;
import me.dragonappear.springsecuritycustom.security.api.service.JsonUserDetailsService;
import me.dragonappear.springsecuritycustom.security.factory.UrlResourceMapFactoryBean;
import me.dragonappear.springsecuritycustom.security.web.handler.FormAccessDeniedHandler;
import me.dragonappear.springsecuritycustom.security.web.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import me.dragonappear.springsecuritycustom.security.web.oauth2.OAuth2AccountService;
import me.dragonappear.springsecuritycustom.security.web.provider.FormAuthenticationProvider;
import me.dragonappear.springsecuritycustom.security.web.service.SecurityResourceService;
import me.dragonappear.springsecuritycustom.service.MethodSecurityService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final ResourceRepository resourceRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityResourceService securityResourceService() {
        return new SecurityResourceService(resourceRepository);
    }

}
