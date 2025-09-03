package com.mumanal.siver.web;

import com.mumanal.siver.domain.Usuario;
import com.mumanal.siver.repository.UsuarioRepository;
import com.mumanal.siver.security.JwtService;
import com.mumanal.siver.web.dto.LoginDto;
import com.mumanal.siver.web.dto.TokenDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository repo, PasswordEncoder encoder, JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public Mono<TokenDto> register(@RequestBody Usuario usuario) {
        usuario = new Usuario(
                usuario.codigo(),
                encoder.encode(usuario.password()),
                usuario.nombreCompleto(),
                usuario.paginaInicio(),
                usuario.email(),
                usuario.oficina(),
                usuario.unidad(),
                usuario.permisos(),
                usuario.cargo(),
                usuario.mosca(),
                usuario.nivel(),
                usuario.genero(),
                usuario.habilitado(),
                usuario.fechaCreacion(),
                usuario.creadoPor(),
                usuario.fechaModificacion(),
                usuario.modificadoPor(),
                usuario.direccionIp(),
                usuario.accion(),
                usuario.validaPassword()
        );
        return repo.save(usuario)
                .map(u -> new TokenDto(jwtService.generateToken(u.codigo())));
    }

    @PostMapping("/login")
    public Mono<TokenDto> login(@RequestBody LoginDto dto) {
        return repo.findByEmail(dto.email())
                .filter(u -> encoder.matches(dto.password(), u.password()))
                .map(u -> new TokenDto(jwtService.generateToken(u.codigo())))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Credenciales inválidas")));
    }
}
