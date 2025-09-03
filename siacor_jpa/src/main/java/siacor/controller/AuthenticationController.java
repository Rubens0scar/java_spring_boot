package siacor.controller;

// import com.example.demo.model.JwtRequest;
// import com.example.demo.model.JwtResponse;
// import com.example.demo.service.JwtUserDetailsService;
// import com.example.demo.service.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import siacor.comun.response.JwtRequest;
import siacor.comun.response.JwtResponse;
import siacor.service.JwtTokenUtil;
import siacor.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        // Autenticar al usuario
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Cargar los detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // Generar el token JWT
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Devolver el token en la respuesta
        return ResponseEntity.ok(new JwtResponse(token));
        
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales inválidas", e);
        }
    }
}
