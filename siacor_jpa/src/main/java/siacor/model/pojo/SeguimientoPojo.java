package siacor.model.pojo;

import java.time.LocalDateTime;


import lombok.Data;

@Data
public class SeguimientoPojo {
    private Long idSeguimiento;
	
	private String codigo;   //NURI 
	
	private String derivadoPor;
	
	private String derivadoA;
	
	private LocalDateTime fechaDerivacion;
	
	private LocalDateTime fechaRecepcion;
	
	private Long estado;
	
	private Long idAccion;
	
	private String observaciones;
	
	private Long padre;
	
	private Integer oficial;
	
	private Long hijo;
	
	private String escusa;
	
	//private Integer habilitado;
	
	//private LocalDateTime fechaCreacion;
	
	private String creadoPor;
	
	//private LocalDateTime fechaModificacion;
	
	private String modificadoPor;
	
	private String direccionIp;
	
	//private String accion;

	private String usuario;
}
