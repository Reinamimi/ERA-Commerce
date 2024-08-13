package me.emma.orderservice.sercurityConfig;

import me.emma.orderservice.serviceClient.UserServiceAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class OrderServiceSecurityConfig {
    @Autowired
    private UserServiceAuthenticationProvider userServiceAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authenticationProvider(userServiceAuthenticationProvider)
                .authorizeRequests()
                    .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ROLE_ADMIN")
                .and()
                .formLogin();
        return httpSecurity.build();
    }
}
