package siacor.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="correlativos", schema="siacor", catalog="BD_Mumanal")
public class Correlativos {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_cor")
	private Long idCor;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "correlativo")
	private Integer correlativo;
	
	@Column(name = "gestion")
	private Integer gestion;
	
	@Column(name = "oficina")
	private String oficina;
	
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

    public Correlativos(){
        this.habilitado = 1;
        this.fechaCreacion = LocalDateTime.now();
    }
}
