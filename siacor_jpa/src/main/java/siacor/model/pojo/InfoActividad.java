package siacor.model.pojo;

import java.time.LocalDateTime;

public class InfoActividad {
    //private LocalDateTime fechaUltimaActividad;
    private LocalDateTime fechaLogin;
    private Long idSesion;

    // Constructor
    public InfoActividad(/*LocalDateTime fechaUltimaActividad,*/ LocalDateTime fechaLogin, Long idSesion) {
        //this.fechaUltimaActividad = fechaUltimaActividad;
        this.fechaLogin = fechaLogin;
        this.idSesion = idSesion;
    }

    // Getters y Setters
    // public LocalDateTime getFechaUltimaActividad() {
    //     return fechaUltimaActividad;
    // }

    // public void setFechaUltimaActividad(LocalDateTime fechaUltimaActividad) {
    //     this.fechaUltimaActividad = fechaUltimaActividad;
    // }

    public LocalDateTime getFechaLogin() {
        return fechaLogin;
    }

    public void setFechaLogin(LocalDateTime fechaLogin) {
        this.fechaLogin = fechaLogin;
    }

    public Long getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(Long idSesion) {
        this.idSesion = idSesion;
    }

    @Override
    public String toString() {
        return "InfoActividad{fechaLogin=" + fechaLogin + ", idSesion=" + idSesion + '}';
    }
}
