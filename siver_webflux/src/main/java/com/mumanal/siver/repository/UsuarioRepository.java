package com.mumanal.siver.repository;

import com.mumanal.siver.domain.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, String> {
    Mono<Usuario> findByEmail(String email);
}
