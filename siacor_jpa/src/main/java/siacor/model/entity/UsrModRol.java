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
@Table(name="usr_mod_rol", schema="public", catalog="BD_Mumanal")
public class UsrModRol {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_usr_mod_rol")
	private Long idUsrModRol;
	
	@Column(name = "codigo_usuario")
	private String codigoUsuario;
	
	@Column(name = "cod_rol")
	private Long codRol;
	
	@Column(name = "cod_modulo")
	private Long codModulo;
	
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

    public UsrModRol(){
        this.habilitado = 1;
        this.fechaCreacion = LocalDateTime.now();
		this.accion = "insert";
    }
}
