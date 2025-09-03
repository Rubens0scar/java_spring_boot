package siacor.model.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CabeceraSeguimientoPojo {
    private String nur;
    private String cite;
    //private List<SalidaDataDto> citesAdjuntos;
    private String referencia;
    private String remitenteNombre;
    private String remitenteCargo;
    private String proveido;
    private String derivadoPor;
    private String derivadoA;
    private String accion;
    private String proceso;
    private String destinatarioCargo;
    private String destinatarioNombre;
    private String adjuntos;
    private String tipoDocumento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MMM/yyyy HH:mm", locale = "es")
    private LocalDateTime fechaCreacion;
    private Integer oficial;
    //private List<ListaSeguimientosPojo> listaSeguimiento;
}
