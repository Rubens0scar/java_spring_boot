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
@Table(name="agrupacion", schema="siacor", catalog="BD_Mumanal")
public class Agrupacion {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_agru")
	private Long idAgru;
	
	@Column(name = "nur_p")
	private String nurP;
	
	@Column(name = "nur_s")
	private String nurS;
	
	@Column(name = "id_seguimiento")
	private Long idSeguimiento;
	
	@Column(name = "oficial")
	private Integer oficial;
	
	@Column(name = "esta_gru")
	private Integer estaGru;
	
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

    public Agrupacion(){
        this.estaGru = 0;
        this.habilitado = 1;
        this.fechaCreacion = LocalDateTime.now();
        this.accion = "insert"; 
    }

}
