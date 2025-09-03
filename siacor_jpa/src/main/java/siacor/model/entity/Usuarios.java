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
@Table(name="usuario", schema="public", catalog="BD_Mumanal")
public class Usuarios {
    @Id
    @Column(name = "codigo")
    private String codigo;

    // @Transient
    // private String usuario;
    
	@Column(name = "password")
    private String password;
    
	@Column(name = "nombre_completo")
    private String nombreCompleto;
    
	@Column(name = "pagina_inicio")
    private Long paginaInicio;
    
	@Column(name = "email")
    private String email;
    
	@Column(name = "oficina")
    private Long oficina;
    
	@Column(name = "unidad")
    private Long unidad;
    
	@Column(name = "permisos")
    private Long permisos;
    
	@Column(name = "cargo")
    private String cargo;
    
	@Column(name = "mosca")
    private String mosca;
    
	@Column(name = "nivel")
    private Integer nivel;
    
    @Column(name = "genero")
    private Long genero;

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

    @Column(name = "valida_password")
    private Integer validaPassword;

    public Usuarios(){
        this.habilitado = 1;
        this.fechaCreacion = LocalDateTime.now();
        this.accion = "insert";
        this.validaPassword = 0;
    }
}
