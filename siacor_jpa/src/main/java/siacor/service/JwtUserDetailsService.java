package siacor.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import siacor.model.Dto.UsuarioDto;
import siacor.model.repository.UsuariosRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{
     @Autowired
    private final UsuariosRepository usuariosRepository;

    public JwtUserDetailsService(UsuariosRepository usuariosRepository){
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioDto user = usuariosRepository.buscarPassword(username);

        // Asignar un rol por defecto si no tiene roles
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Rol por defecto

        // Convertir el usuario de la base de datos a un objeto UserDetails
        return new org.springframework.security.core.userdetails.User(
            user.getCodigo(),
            user.getPassword(),
            authorities // Colección de autoridades (roles)
        );
    }
}