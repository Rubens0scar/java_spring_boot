package siacor.model.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import siacor.model.utilitarios.CustomDateSerializer;

@Data
public class AgrupacionPojo {
    private Long idAgru;
	
	private String nurP;
	
	private String nurS;
	
	private Long idSeguimiento;
	
	private Integer oficial;
	
	private Integer estaGru;
	
	private Integer habilitado;
	
	@JsonSerialize(using = CustomDateSerializer.class)
	private LocalDateTime fechaCreacion;
	
	private String creadoPor;
	
	@JsonSerialize(using = CustomDateSerializer.class)
	private LocalDateTime fechaModificacion;
	
	private String modificadoPor;
	
	private String direccionIp;
	
	private String accion;

	private String usuarioAutor;

}
