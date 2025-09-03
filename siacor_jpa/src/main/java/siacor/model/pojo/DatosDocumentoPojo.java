package siacor.model.pojo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import siacor.model.Dto.buscaDatosDto;

@Data
public class DatosDocumentoPojo {
    private String codigo;
	
	private String citeOriginal;
	
	private String destinatarioTitulo;
	
	private String destinatarioNombres;
	
	private String destinatarioCargo;
	
	private String destinatarioInstitucion;
	
	private String remitenteNombres;
	
	private String remitenteCargo;
	
	private String remitenteInstitucion;
	
	private String mosca;
	
	private String referencia;
	
	private String contenido;
	
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "d 'de' MMMM 'del' yyyy", locale = "es")
	private LocalDateTime fecha;
	
	private String autor;
	
	private String adjuntos;
	
	private String copias;
	
	private Integer nroHojas;
	
	private Long tipoDocumento;
	
	// private Long idVias;
	
	//private Integer habilitado;
	
	//private LocalDateTime fechaCreacion;
	
	private String creadoPor;
	
	//private LocalDateTime fechaModificacion;
	
	//private String modificadoPor;
	
	private String direccionIp;
	
	private String accion;

	private List<buscaDatosDto> vias;

	private String tipoNombreDocumento;

	private String nuri;
}
