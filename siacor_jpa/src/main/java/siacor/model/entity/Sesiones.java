package siacor.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="sesiones", schema="public", catalog="BD_Mumanal")
public class Sesiones {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
	private Long idSesion;
	
	@Column(name = "usuario")
	private String usuario;
	
	@Column(name = "estado")
	private Boolean estado;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "fecha_activo")
	private LocalDateTime fechaActivo;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "fecha_no_activo")
	private LocalDateTime fechaNoActivo;
	
	@Column(name = "ip_usuario")
	private String ipUsuario;
	
	@Column(name = "user_agent")
	private String userAgent;
}
