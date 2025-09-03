package siacor.scheduler;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import siacor.interceptor.ActivityInterceptor;
import siacor.model.pojo.InfoActividad;

@Component
public class SesionCleaner {
    @Value("${jwt.minutosExpiracion}")
    private Integer minutosExpiracion;

    @Autowired
    private final ActivityInterceptor activityInterceptor;

    public SesionCleaner(ActivityInterceptor activityInterceptor){
        this.activityInterceptor = activityInterceptor;
    } 

    @Scheduled(fixedRate = 60000) // Cada 60 segundos
    public void limpiarSesionesInactivas() throws Exception{
        Map<String, InfoActividad> actividad = ActivityInterceptor.getActividadUsuarios();
        LocalDateTime ahora = LocalDateTime.now();

        for (Map.Entry<String, InfoActividad> entry : new HashMap<>(actividad).entrySet()) {
            String usuario = entry.getKey();
            InfoActividad infoActividad = entry.getValue();

            //Long idSesion = infoActividad.getIdSesion();
            LocalDateTime ultimaActividad = infoActividad.getFechaLogin();
            long minutosInactivo = Duration.between(ultimaActividad, ahora).toMinutes();

            if (minutosInactivo > minutosExpiracion) {
                activityInterceptor.cerrarSesion(usuario);
                System.out.println("Se cerró sesión por inactividad: " + usuario);
            }
        }
    }
}
// public class SesionCleaner {
//     @Scheduled(fixedRate = 60000) // Cada 60 segundos
//     public void limpiarSesionesInactivas() {
//         Map<String, LocalDateTime> actividad = ActivityInterceptor.getActividadUsuarios();
//         LocalDateTime ahora = LocalDateTime.now();

//         for (Map.Entry<String, LocalDateTime> entry : new HashMap<>(actividad).entrySet()) {
//             String idUsuario = entry.getKey();
//             LocalDateTime ultimaAccion = entry.getValue();

//             long minutosInactivo = Duration.between(ultimaAccion, ahora).toMinutes();

//             if (minutosInactivo > 59) {
//                 ActivityInterceptor.cerrarSesion(idUsuario);
//                 System.out.println("Se cerró sesión por inactividad: " + idUsuario);
//                 //System.out.println("MINUTOS INACTIVOS: " + minutosInactivo);

//             }
//         }
//     }
// }
