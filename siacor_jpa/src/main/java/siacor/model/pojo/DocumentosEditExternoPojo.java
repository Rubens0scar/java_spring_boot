package siacor.model.pojo;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class DocumentosEditExternoPojo {	
	private String citeOriginal;
	
	private String remitenteNombres;
	
	private String remitenteCargo;
	
	private String remitenteInstitucion;
	
	private String referencia;
	
	private OffsetDateTime fecha;
	
}