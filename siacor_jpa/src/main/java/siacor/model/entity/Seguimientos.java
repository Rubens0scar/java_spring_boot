package siacor.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="seguimientos", schema="siacor", catalog="BD_Mumanal")
public class Seguimientos {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguimiento")
	private Long idSeguimiento;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "derivado_por")
	private String derivadoPor;
	
	@Column(name = "derivado_a")
	private String derivadoA;
	
	@Column(name = "fecha_derivacion")
	private LocalDateTime fechaDerivacion;
	
	@Column(name = "fecha_recepcion")
	private LocalDateTime fechaRecepcion;
	
	@Column(name = "estado")
	private Long estado;
	
	@Column(name = "id_accion")
	private Long idAccion;
	
	@Column(name = "observaciones")
	private String observaciones;
	
	@Column(name = "padre")
	private Long padre;
	
	@Column(name = "oficial")
	private Integer oficial;
	
	@Column(name = "hijo")
	private Long hijo;
	
	@Column(name = "escusa")
	private String escusa;
	
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

    public Seguimientos(){
        this.fechaCreacion = LocalDateTime.now();
        this.habilitado = 1;
        this.accion = "insert";
    }

}
