package siacor.model.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioPojo {
    private String codigo;
    private String password;
    private String nombreCompleto;
    private Long paginaInicio;
    private String email;
    private Long oficina;
    private Long unidad;
    private Long permisos;
    private String cargo;
    private String mosca;
    private Integer nivel;
    private Long genero;
    private Integer habilitado;
    private LocalDateTime fechaCreacion;
    private String creadoPor;
    private LocalDateTime fechaModificacion;
    private String modificadoPor;
    private String direccionIp;
    private String accion;

    private Long idUsrModRol;
    private Long rol;
    private Long modulo;
}
