package siacor.config;

// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.MalformedJwtException;
// import siacor.service.JwtTokenUtil;
// import siacor.service.JwtUserDetailsService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequest;

// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import siacor.service.JwtTokenUtil;
import siacor.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Lista de rutas que no requieren autenticación
        String path = request.getRequestURI();

        if (path.startsWith("/autentificacion") || path.startsWith("/listarParametrica/")) {
            // Si la ruta está en la lista de permitidas, continuar sin autenticación
            chain.doFilter(request, response);
            return;
        }

        String username = null;
        String jwtToken = null;

        try {
            // Extraer el token del encabezado "Authorization"
            final String requestTokenHeader = request.getHeader("Authorization");

            // Verificar si el token comienza con "Bearer "
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7); // Eliminar "Bearer " para obtener el token

                // Extraer el nombre de usuario del token
                username = jwtTokenUtil.extractUsername(jwtToken);
            } else {
                logger.warn("El token JWT no comienza con 'Bearer'");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token JWT no válido: debe comenzar con 'Bearer'");
                return;
            }

            // Validar el token y establecer la autenticación en el contexto de seguridad
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargar los detalles del usuario desde la base de datos
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

                // Verificar si el token es válido
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                    // Crear un objeto de autenticación
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    // Establecer detalles adicionales (como la dirección IP)
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (ExpiredJwtException e) {
            logger.error("El token JWT ha expirado", e);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token JWT ha expirado");
            return;
        } catch (MalformedJwtException e) {
            logger.error("El token JWT está mal formado", e);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token JWT no válido: formato incorrecto");
            return;
        } catch (SignatureException e) {
            logger.error("La firma del token JWT no es válida", e);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token JWT no válido: firma incorrecta");
            return;
        } catch (IllegalArgumentException e) {
            logger.error("No se pudo obtener el token JWT", e);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token JWT no proporcionado");
            return;
        } catch (Exception e) {
            logger.error("Error durante la validación del token JWT", e);
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
            return;
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}

// public class JwtRequestFilter extends OncePerRequestFilter {
//     @Autowired
//     private JwtUserDetailsService jwtUserDetailsService;

//     @Autowired
//     private JwtTokenUtil jwtTokenUtil;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//             throws ServletException, IOException {

//         final String requestTokenHeader = request.getHeader("Authorization");

//         String username = null;
//         String jwtToken = null;

//         if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//             jwtToken = requestTokenHeader.substring(7);
//             try {
//                 username = jwtTokenUtil.extractUsername(jwtToken);
//             } catch (IllegalArgumentException e) {
//                 System.out.println("No se puede obtener el token JWT");
//             } catch (ExpiredJwtException e) {
//                 System.out.println("El token JWT ha caducado");
//             }
//         } else {
//             logger.warn("El token JWT no comienza con 'Bearer'");
//         }

//         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//             UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

//             if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

//                 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                         userDetails, null, userDetails.getAuthorities());
//                 usernamePasswordAuthenticationToken
//                         .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//             }
//         }
//         chain.doFilter(request, response);
//     }
// }
