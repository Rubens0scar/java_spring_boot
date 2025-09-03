package siacor.model.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SeguimientoDataPojo {
    private Long idSeguimiento;
	
	private String codigo;
	
	private String derivadoPor;
	
	private String derivadoA;

    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy HH:mm", locale = "es")
	private LocalDateTime fechaDerivacion;
	
    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy HH:mm", locale = "es")
	private LocalDateTime fechaRecepcion;
	
	private Long estado;
	
	private Long idAccion;
	
	private String observaciones;
	
	private Long padre;
	
	private Integer oficial;
	
	private Long hijo;
	
	private String escusa;
	
	private Integer habilitado;
	
    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy HH:mm", locale = "es")
	private LocalDateTime fechaCreacion;
	
	private String creadoPor;
	
    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy HH:mm", locale = "es")
	private LocalDateTime fechaModificacion;
	
	private String modificadoPor;
	
	private String direccionIp;
	
	private String accion;
}
