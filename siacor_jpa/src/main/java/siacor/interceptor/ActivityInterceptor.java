package siacor.interceptor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import siacor.model.entity.Sesiones;
import siacor.model.pojo.InfoActividad;
import siacor.service.JwtTokenUtil;
import siacor.service.UsuarioService;

@Component
public class ActivityInterceptor implements HandlerInterceptor {

    private static final Map<String, InfoActividad> actividadUsuarios = new ConcurrentHashMap<>();

    private final JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private final UsuarioService usuarioService;

    public ActivityInterceptor(JwtTokenUtil jwtTokenUtil,UsuarioService usuarioService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.usuarioService = usuarioService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
    //    System.out.println("En el INTERCEPTOR");
    
        // Permitir rutas públicas sin verificación
        if (path.equals("/autentificacion") || path.equals("/listarParametrica/MODULO")) {
            return true;
        }
    
        // Extraer y validar token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token faltante o inválido.");
            return false;
        }
    
        token = token.replace("Bearer ", "");
        String user;
        try {
            user = jwtTokenUtil.extractUsername(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token no válido.");
            return false;
        }
    
        // Validar si el usuario tiene sesión activa
        InfoActividad actividad = actividadUsuarios.get(user);
        if (actividad == null) {
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // response.getWriter().write("Sesión expirada. Por favor inicie sesión nuevamente.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = """
                {
                    "message": "Sesión expirada. Por favor inicie sesión nuevamente",
                    "status": 401
                }
            """;

            response.getWriter().write(json);
            return false;
        }
    
        // Actualizar la última fecha de actividad
        actividad.setFechaLogin(LocalDateTime.now());

        return true;
    }
    // @Override
    // public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //     String path = request.getRequestURI();
    //     System.out.println("En el INTERCEPTOR");

    //     if (path.equals("/autentificacion") || path.equals("/listarParametrica/MODULO")) {
    //         return true;
    //     }

    //     String token = request.getHeader("Authorization");
    //     token = token.replace("Bearer ", "");
    //     String user = jwtTokenUtil.extractUsername(token);

    //     cargarActividad(user);

    //     return true;
    // }

    public static Map<String, InfoActividad> getActividadUsuarios() {
        return actividadUsuarios;
    }

    public void cerrarSesion(String usuario) throws Exception {
        InfoActividad info = actividadUsuarios.get(usuario);

        if (info != null) {
            Long id = info.getIdSesion();
            this.cerrarSesionBD(id); // ✅ Ahora sí puedes usar this
            actividadUsuarios.remove(usuario);
        }
    }

    public void cerrarSesionBD(Long id) throws Exception {
        System.out.println("ANTES DE CERRAR");
        Sesiones ses = usuarioService.cerrarSesiones(id,"Por Sistema");
        System.out.println("CERRO SESION: " + ses.getIpUsuario());
    }

    public static void cargarActividad(String user, Long idSesion) {
        InfoActividad info = actividadUsuarios.get(user);

        if (info != null) {
            info.setIdSesion(idSesion);
        } else {
            // No existe: crearlo con fecha ahora y un idSesion generado
            InfoActividad nuevaInfo = new InfoActividad(LocalDateTime.now(), idSesion);
            actividadUsuarios.put(user, nuevaInfo);
        }
        System.out.println("ACTIVIDAD USUARIOS CON SESION: " + actividadUsuarios);
    }

    public static void cargarActividad(String user) {
        InfoActividad info = actividadUsuarios.get(user);

        if (info != null) {
            // Ya existe: actualizar solo la fechaLogin a ahora
            info.setFechaLogin(LocalDateTime.now());
        } 
        System.out.println("ACTIVIDAD USUARIOS SIN SESION: " + actividadUsuarios);
    }

}

// public class ActivityInterceptor implements HandlerInterceptor {
//     // Map con ID de usuario/IP -> Última vez que hizo una acción
//     private static final Map<String, LocalDateTime> actividadUsuarios = new ConcurrentHashMap<>();
//     //private static final Map<String, InfoActividad> actividadUsuarios = new ConcurrentHashMap<>();

//     @Autowired
//     private final JwtTokenUtil jwtTokenUtil;

//     public ActivityInterceptor(JwtTokenUtil jwtTokenUtil){
//         this.jwtTokenUtil = jwtTokenUtil;
//     }

//     @Override
//     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//         String path = request.getRequestURI();
//         //System.out.println("Request path: " + path);
//         System.out.println("E EL INTERCEPTOR");
//         if(path.equals("/autentificacion") || path.equals("/listarParametrica/MODULO")) return true;

//         //String ip = request.getRemoteAddr();
//         String token = request.getHeader("Authorization");
//         token = token.replace("Bearer ", "");
//         String user = jwtTokenUtil.extractUsername(token);
//         //System.out.println("USER SESION: " + user);

//         // //actividadUsuarios.put(user, LocalDateTime.now());
//         // actividadUsuarios.put(user, LocalDateTime.now());
//         this.cargarActividad(user);

//         System.out.println("ACTIVIDAD USUARIOS: " + actividadUsuarios);
        
//         return true;
//     }
    
//     //public static Map<String, InfoActividad> getActividadUsuarios() {
//     public static Map<String, LocalDateTime> getActividadUsuarios() {
//         return actividadUsuarios;
//     }

//     public static void cerrarSesion(String id) {
//         actividadUsuarios.remove(id);
//     }

//     public void cargarActividad(String user){
//         //actividadUsuarios.put(user, LocalDateTime.now());
//         actividadUsuarios.put(user, LocalDateTime.now());
//     }
// }
