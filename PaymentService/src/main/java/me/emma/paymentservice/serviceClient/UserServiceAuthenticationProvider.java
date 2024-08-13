package me.emma.paymentservice.serviceClient;

import lombok.extern.slf4j.Slf4j;
import me.emma.paymentservice.pojo.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class UserServiceAuthenticationProvider implements AuthenticationProvider {
    private final RestTemplate restTemplate;
    public UserServiceAuthenticationProvider(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    @Value("${userService.userUrl}")
    private String userUrl;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        ResponseEntity<UserDetails> response = restTemplate.postForEntity(
                userUrl + "/authenticate",
                new AuthRequest(username, password),
                UserDetails.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            UserDetails userDetails = response.getBody();
            return new UsernamePasswordAuthenticationToken(
                    username, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
