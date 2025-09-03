package siacor.model.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsrModRolPojo {
    private Long idUsrModRol;
	private String codigoUsuario;
	private Long codRol;
	private Long codModulo;
	private Integer habilitado;
	private LocalDateTime fechaCreacion;
	private String creadoPor;
	private LocalDateTime fechaModificacion;
	private String modificadoPor;
	private String direccionIp;
	private String accion;
}
