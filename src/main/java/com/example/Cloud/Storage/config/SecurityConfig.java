package com.example.Cloud.Storage.config;

import com.example.Cloud.Storage.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.sql.PreparedStatement;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/signup", "/css/**", "/js/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .formLogin(formLogin -> {
                    formLogin.permitAll();
                    formLogin.loginPage("/login");
                    formLogin.defaultSuccessUrl("/home", true);
                    formLogin.failureUrl("/login?invalid");
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.clearAuthentication(true);
                    logout.logoutSuccessUrl("/login?logout");
                    logout.invalidateHttpSession(true); // Invalidate session
                    logout.deleteCookies("JSESSIONID"); // Delete cookies
                    logout.permitAll();
                })
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return (web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**")));
    }
}
