package siacor.model.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DataDocumentosPojo {
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

    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy HH:mm", locale = "es")
	private LocalDateTime fecha;
	
	private String autor;
	
	private String adjuntos;
	
	private String copias;
	
	private Integer nroHojas;
	
	private Long tipoDocumento;
	
	private Integer habilitado;
	
    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy HH:mm", locale = "es")
	private LocalDateTime fechaCreacion;
	
	private String creadoPor;
	
    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy HH:mm", locale = "es")
	private LocalDateTime fechaModificacion;
	
	private String modificadoPor;
	
	private String direccionIp;
	
	private String accion;

    // Constructor con parámetros
    public DataDocumentosPojo(String codigo, String citeOriginal, String destinatarioTitulo, 
                              String destinatarioNombres, String destinatarioCargo, 
                              String destinatarioInstitucion, String remitenteNombres, 
                              String remitenteCargo, String remitenteInstitucion, String mosca,
                              String referencia, String contenido, LocalDateTime fecha, String autor, 
                              String adjuntos, String copias, Integer nroHojas, Long tipoDocumento, 
                              Integer habilitado, LocalDateTime fechaCreacion, String creadoPor, 
                              LocalDateTime fechaModificacion, String modificadoPor, 
                              String direccionIp, String accion) {
        this.codigo = codigo;
        this.citeOriginal = citeOriginal;
        this.destinatarioTitulo = destinatarioTitulo;
        this.destinatarioNombres = destinatarioNombres;
        this.destinatarioCargo = destinatarioCargo;
        this.destinatarioInstitucion = destinatarioInstitucion;
        this.remitenteNombres = remitenteNombres;
        this.remitenteCargo = remitenteCargo;
        this.remitenteInstitucion = remitenteInstitucion;
        this.mosca = mosca;
        this.referencia = referencia;
        this.contenido = contenido;
        this.fecha = fecha;
        this.autor = autor;
        this.adjuntos = adjuntos;
        this.copias = copias;
        this.nroHojas = nroHojas;
        this.tipoDocumento = tipoDocumento;
        this.habilitado = habilitado;
        this.fechaCreacion = fechaCreacion;
        this.creadoPor = creadoPor;
        this.fechaModificacion = fechaModificacion;
        this.modificadoPor = modificadoPor;
        this.direccionIp = direccionIp;
        this.accion = accion;
    }
}
