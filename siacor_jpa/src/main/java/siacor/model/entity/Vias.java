package siacor.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="vias", schema="siacor", catalog="BD_Mumanal")
@Where(clause = "habilitado=1")
public class Vias {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id_vias")
    private Long idVias;

    @Column(name = "codigo_usuario")
    private String codigoUsuario;

	@Column(name = "codigo")
    private String codigo;

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

    public Vias(){
        this.fechaCreacion = LocalDateTime.now();
        this.habilitado = 1;
    }

}
