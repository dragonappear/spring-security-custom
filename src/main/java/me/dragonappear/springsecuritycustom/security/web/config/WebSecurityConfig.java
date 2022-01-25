package me.dragonappear.springsecuritycustom.security.web.config;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.security.web.handler.FormAccessDeniedHandler;
import me.dragonappear.springsecuritycustom.security.web.handler.FormAuthenticationFailureHandler;
import me.dragonappear.springsecuritycustom.security.web.handler.FormAuthenticationSuccessHandler;
import me.dragonappear.springsecuritycustom.security.web.provider.FormAuthenticationProvider;
import me.dragonappear.springsecuritycustom.security.web.oauth2.OAuth2AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationDetailsSource formAuthenticationDetailsSource;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final OAuth2AccountService oAuth2AccountService;
    private final AuthenticationSuccessHandler formAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler formAuthenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(formAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login**", "/users").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/denied")
                .accessDeniedHandler(formAccessDeniedHandler())

                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
                .authenticationDetailsSource(formAuthenticationDetailsSource);

        http
                .oauth2Login()
                .loginPage("/login")
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
                .userInfoEndpoint()
                .userService(oAuth2AccountService);
    }

    @Bean
    public AuthenticationProvider formAuthenticationProvider() {
        return new FormAuthenticationProvider(passwordEncoder, userDetailsService);
    }

    @Bean
    public AccessDeniedHandler formAccessDeniedHandler() {
        return new FormAccessDeniedHandler("/denied");
    }
}
