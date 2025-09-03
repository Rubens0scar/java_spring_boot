package siacor.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="documentos", schema="siacor", catalog="BD_Mumanal")
public class Documentos {
    @Id
   	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "cite_original")
	private String citeOriginal;
	
	@Column(name = "destinatario_titulo")
	private String destinatarioTitulo;
	
	@Column(name = "destinatario_nombres")
	private String destinatarioNombres;
	
	@Column(name = "destinatario_cargo")
	private String destinatarioCargo;
	
	@Column(name = "destinatario_institucion")
	private String destinatarioInstitucion;
	
	@Column(name = "remitente_nombres")
	private String remitenteNombres;
	
	@Column(name = "remitente_cargo")
	private String remitenteCargo;
	
	@Column(name = "remitente_institucion")
	private String remitenteInstitucion;
	
	@Column(name = "mosca")
	private String mosca;
	
	@Column(name = "referencia")
	private String referencia;
	
	@Column(name = "contenido")
	private String contenido;
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@Column(name = "autor")
	private String autor;
	
	@Column(name = "adjuntos")
	private String adjuntos;
	
	@Column(name = "copias")
	private String copias;
	
	@Column(name = "nrohojas")
	private Integer nroHojas;
	
	@Column(name = "tipo_documento")
	private Long tipoDocumento;
	
	@Column(name = "habilitado")
	private Integer habilitado;
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;
	
	@Column(name = "creado_por")
	private String creadoPor;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "modificado_por")
	private String modificadoPor;
	
	@Column(name = "direccion_ip")
	private String direccionIp;
	
	@Column(name = "accion")
	private String accion;

    public Documentos(){
        this.habilitado = 1;
        this.fechaCreacion = LocalDateTime.now();
    }
}
