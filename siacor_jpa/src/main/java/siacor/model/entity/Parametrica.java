package siacor.model.entity;

import java.time.LocalDate;
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
@Table(name="parametrica", schema="public", catalog="BD_Mumanal")
@Where(clause = "activo=true")
public class Parametrica {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametrica")
	private Long idParametrica;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "valor_texto")
	private String valorTexto;
	
	@Column(name = "valor_numerico")
	private Double valorNumerico;
	
	@Column(name = "valor_booleano")
	private Boolean valorBooleano;
	
	@Column(name = "valor_fecha")
	private LocalDate valorFecha;
	
	@Column(name = "activo")
	private Boolean activo;
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;

    public Parametrica(){
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }
}
