package me.emma.paymentservice.serviceClient;

import lombok.extern.slf4j.Slf4j;
import me.emma.paymentservice.pojo.dto.AuthRequest;
import me.emma.paymentservice.pojo.dto.AuthenticateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

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
        log.info("Authenticating user {}", authentication);
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        log.info(username);
        log.info("Authorize is being called");

        AuthRequest authRequest = new AuthRequest(username, password);

        // Make the request to authenticate
        ResponseEntity<AuthenticateDTO> responseEntity = restTemplate.postForEntity(
                userUrl + "/authenticate",
                authRequest,
                AuthenticateDTO.class
        );

        AuthenticateDTO response = responseEntity.getBody();
        log.info(String.valueOf(response));
        // Check the response
        if (response != null && response.getUserName() != null) {
            // Create a list of granted authorities based on roles
            List<GrantedAuthority> authorities = response.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Create and return the UsernamePasswordAuthenticationToken
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    authorities
            );
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
