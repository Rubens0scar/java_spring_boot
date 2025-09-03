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
@Table(name="correlativos_asignados", schema="siacor", catalog="BD_Mumanal")
public class CorrelativosAsignados {
    @Id
    @Column(name = "codigo")
    private String codigo;

    @Column(name = "persona")
    private String persona;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    public CorrelativosAsignados(){
        this.fecha = LocalDateTime.now();
    }
}
