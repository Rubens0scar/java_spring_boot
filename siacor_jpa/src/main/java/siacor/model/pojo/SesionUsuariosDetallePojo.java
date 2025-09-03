package siacor.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import javax.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@SqlResultSetMapping(
    name = "SesionUsuariosDetalleMapping",
    classes = @ConstructorResult(
        targetClass = SesionUsuariosDetallePojo.class,
        columns = {
            @ColumnResult(name = "idSesion", type = Long.class),
            @ColumnResult(name = "codigo", type = String.class),
            @ColumnResult(name = "nombreCompleto", type = String.class),
            @ColumnResult(name = "cargo", type = String.class),
            @ColumnResult(name = "estado", type = String.class),
            @ColumnResult(name = "fechaActivo", type = LocalDateTime.class),
            @ColumnResult(name = "fechaCerrar", type = LocalDateTime.class),
            @ColumnResult(name = "duracion", type = String.class),
            @ColumnResult(name = "sesionCerrada", type = String.class)
        }
    )
)
public class SesionUsuariosDetallePojo {
    @Id
    private Long idSesion;
    private String codigo;
    private String nombreCompleto;
    private String cargo;
    private String estado;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaActivo;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaCerrar;
    private String duracion;
    private String sesionCerrada;

    // Constructor necesario para @ConstructorResult
    public SesionUsuariosDetallePojo(Long idSesion, String codigo, String nombreCompleto, String cargo,
                                     String estado, LocalDateTime fechaActivo, LocalDateTime fechaCerrar,
                                     String duracion, String sesionCerrada) {
        this.idSesion = idSesion;
        this.codigo = codigo;
        this.nombreCompleto = nombreCompleto;
        this.cargo = cargo;
        this.estado = estado;
        this.fechaActivo = fechaActivo;
        this.fechaCerrar = fechaCerrar;
        this.duracion = duracion;
        this.sesionCerrada = sesionCerrada;
    }

    // Constructor vacío requerido por JPA
    public SesionUsuariosDetallePojo() {
    }
}
