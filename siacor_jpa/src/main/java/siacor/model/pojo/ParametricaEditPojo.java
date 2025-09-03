package siacor.model.pojo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ParametricaEditPojo {
    private Long idParametrica;

    private String codigo;
	
	private String nombre;
	
	private String tipo;
	
	private String valorTexto;
	
	private Double valorNumerico;
	
	private Boolean valorBooleano;
	
	private LocalDate valorFecha;
	
	private Boolean activo;
	
	//private LocalDateTime fechaCreacion;
	
	//private LocalDateTime fechaModificacion;
}
