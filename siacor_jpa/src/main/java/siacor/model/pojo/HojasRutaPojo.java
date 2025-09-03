package siacor.model.pojo;

import lombok.Data;

@Data
public class HojasRutaPojo {
    private String nur;
	
	private String codigo;          //cite enviado por front

	//private Long nro;               //enviado por front default -1
	
	private Long estado;            //enviado por front
	
	//private LocalDateTime fecha;
	
	private String usuario;         //enviado por front
	
	private Long proceso;           //enviado por front
	
	//private Integer viasCorr;
	
	//private Integer habilitado;
	
	//private LocalDateTime fechaCreacion;
	
	//private String creadoPor;       //enviado por front
	
	//private LocalDateTime fechaModificacion;
	
	//private String modificadoPor;
	
	private String direccionIp;     //enviado por front
	
	//private String accion;
}
