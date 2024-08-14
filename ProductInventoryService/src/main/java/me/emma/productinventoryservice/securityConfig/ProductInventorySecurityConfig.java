package me.emma.productinventoryservice.securityConfig;

import me.emma.productinventoryservice.serviceClient.UserServiceAuthenticationProvider;
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
public class ProductInventorySecurityConfig {

    @Autowired
    private UserServiceAuthenticationProvider userServiceAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authenticationProvider(userServiceAuthenticationProvider)
                .authorizeRequests()
                .requestMatchers("swagger-ui/index.html#/").permitAll()
                .and()
                .formLogin();
        return httpSecurity.build();
    }
}
