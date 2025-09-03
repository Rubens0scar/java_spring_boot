package siacor.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import siacor.model.utilitarios.ListasDerivacionID;

@Getter
@Setter
@Entity
@IdClass(ListasDerivacionID.class)
@Table(name="listas_derivacion", schema="siacor", catalog="BD_Mumanal")
public class ListasDerivacion {
    @Id
    @Column(name = "usr_origen")
	private String usrOrigen;
	
    @Id
	@Column(name = "usr_destino")
	private String usrDestino;
	
	@Column(name = "opcion")
	private Integer opcion;
	
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

    public ListasDerivacion(){
        this.fechaCreacion = LocalDateTime.now();
        this.habilitado = 1;
    }
}