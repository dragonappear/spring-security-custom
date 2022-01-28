package me.dragonappear.springsecuritycustom.security.web.config;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.security.web.factory.UrlResourcesMapFactoryBean;
import me.dragonappear.springsecuritycustom.security.web.filter.PermitAllFilter;
import me.dragonappear.springsecuritycustom.security.web.handler.FormAccessDeniedHandler;
import me.dragonappear.springsecuritycustom.security.web.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import me.dragonappear.springsecuritycustom.security.web.oauth2.OAuth2AccountService;
import me.dragonappear.springsecuritycustom.security.web.provider.FormAuthenticationProvider;
import me.dragonappear.springsecuritycustom.security.web.voter.IpAddressVoter;
import me.dragonappear.springsecuritycustom.service.SecurityResourceService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationDetailsSource formAuthenticationDetailsSource;
    private final AuthenticationSuccessHandler formAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler formAuthenticationFailureHandler;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final OAuth2AccountService oAuth2AccountService;
    private final SecurityResourceService securityResourceService;

    private static List<String> permitAllResources = Arrays.asList("/", "/login**","/home");

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(formAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/denied")
                .accessDeniedHandler(formAccessDeniedHandler());

        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
                .authenticationDetailsSource(formAuthenticationDetailsSource)
                .defaultSuccessUrl("/");

        http
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oAuth2AccountService);

        http
                .addFilterAt(urlPermitAllFilter(), FilterSecurityInterceptor.class);
    }
    @Bean
    public FilterSecurityInterceptor urlPermitAllFilter() throws Exception {
        FilterSecurityInterceptor interceptor = new PermitAllFilter(permitAllResources);
        interceptor.setAuthenticationManager(authenticationManagerBean());
        interceptor.setAccessDecisionManager(affirmativeBased());
        interceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        return interceptor;
    }

    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    public List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(ipAddressVoter());
        accessDecisionVoters.add(roleVoter());
        return accessDecisionVoters;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> ipAddressVoter() {
        return new IpAddressVoter(securityResourceService);
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new RoleHierarchyImpl();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(),securityResourceService);
    }

    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        UrlResourcesMapFactoryBean bean = new UrlResourcesMapFactoryBean();
        bean.setSecurityResourceService(securityResourceService);
        return bean;
    }

    @Bean
    public AuthenticationProvider formAuthenticationProvider() {
        return new FormAuthenticationProvider(passwordEncoder, userDetailsService);
    }

    @Bean
    public AccessDeniedHandler formAccessDeniedHandler() {
        return new FormAccessDeniedHandler("/denied");
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}