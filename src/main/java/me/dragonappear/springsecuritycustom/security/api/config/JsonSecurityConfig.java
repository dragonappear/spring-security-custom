package me.dragonappear.springsecuritycustom.security.api.config;

import lombok.RequiredArgsConstructor;
import me.dragonappear.springsecuritycustom.security.api.filter.JsonAuthenticationFilter;
import me.dragonappear.springsecuritycustom.security.api.provider.JsonAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@Order(2)
public class JsonSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jsonAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return jsonAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider jsonAuthenticationProvider() {
        return new JsonAuthenticationProvider(userDetailsService, passwordEncoder);
    }
}
