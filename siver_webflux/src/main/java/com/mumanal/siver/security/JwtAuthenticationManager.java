package com.mumanal.siver.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager{
    private final JwtService jwtService;

    public JwtAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        if (jwtService.validate(token)) {
            String codigo = jwtService.extractCodigo(token);
            return Mono.just(
                    new UsernamePasswordAuthenticationToken(
                            codigo, null,
                            java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    )
            );
        }
        return Mono.empty();
    }
}
