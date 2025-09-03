package siacor.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;
import siacor.model.utilitarios.HojasRutaID;

@Getter
@Setter
@Entity
@IdClass(HojasRutaID.class)
@Table(name="hojas_ruta", schema="siacor", catalog="BD_Mumanal")
@Where(clause = "habilitado=1")
public class HojasRuta {
    @Id
    @Column(name = "nur")
	private String nur;
	
    @Id
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "nro")
	private Long nro;
	
	@Column(name = "estado")
	private Long estado;
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@Column(name = "usuario")
	private String usuario;
	
	@Column(name = "proceso")
	private Long proceso;
	
	@Column(name = "viascorr")
	private Integer viasCorr;
	
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

    public HojasRuta(){
        this.fechaCreacion = LocalDateTime.now();
        this.habilitado = 1;
        this.accion = "insert";
        this.fecha = LocalDateTime.now();
		this.nro = -1L;
    }
}
