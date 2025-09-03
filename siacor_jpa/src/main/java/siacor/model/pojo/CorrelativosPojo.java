package siacor.model.pojo;

import lombok.Data;

@Data
public class CorrelativosPojo {
    private Long idCor;
	
	private String codigo;
	
	private Integer correlativo;
	
	private Integer gestion;
	
    private String oficina;
	
	private Integer habilitado;
	
	//private LocalDateTime fechaCreacion;
	
	//private String creadoPor;
	
	//private LocalDateTime fechaModificacion;
	
	private String modificadoPor;
	
	//private String direccionIp;
	
	private String accion;
}
