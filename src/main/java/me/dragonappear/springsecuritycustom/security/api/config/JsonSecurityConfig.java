package me.dragonappear.springsecuritycustom.security.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@Order(1)
public class JsonSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final JsonUserDetailsService jsonUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jsonAuthenticationProvider());
        auth.authenticationProvider(oAuthJsonAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .addFilter(customCorsFilter())
                .addFilterBefore(oAuthJsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(jsonAccessDeniedHandler())
                .authenticationEntryPoint(jsonAuthenticationEntryPoint())

                .and()
                .formLogin().disable().oauth2Client().disable()
                .csrf().disable().rememberMe().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
        jsonAuthenticationFilter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler());
        return jsonAuthenticationFilter;
    }

    @Bean
    public OAuthJsonAuthenticationFilter oAuthJsonAuthenticationFilter() throws Exception {
        OAuthJsonAuthenticationFilter oAuthJsonAuthenticationFilter = new OAuthJsonAuthenticationFilter();
        oAuthJsonAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        oAuthJsonAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
        oAuthJsonAuthenticationFilter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler());
        return oAuthJsonAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider jsonAuthenticationProvider() {
        return new JsonAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationSuccessHandler jsonAuthenticationSuccessHandler() {
        return new JsonAuthenticationSuccessHandler(tokenProvider);
    }

    @Bean
    public JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler() {
        return new JsonAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler jsonAccessDeniedHandler() {
        return new JsonAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint jsonAuthenticationEntryPoint() {
        return new JsonAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public CorsFilter customCorsFilter() {
        return new CustomCorsFilter(new UrlBasedCorsConfigurationSource());
    }

    @Bean
    public OAuthJsonAuthenticationProvider oAuthJsonAuthenticationProvider() {
        return new OAuthJsonAuthenticationProvider(jsonUserDetailsService);
    }

    @Bean
    public JwtValidationFilter jwtValidationFilter() throws Exception {
        return new JwtValidationFilter(
                tokenProvider, jsonUserDetailsService, jsonAuthenticationFailureHandler());
    }
}

