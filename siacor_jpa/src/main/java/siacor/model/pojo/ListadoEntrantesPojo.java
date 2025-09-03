package siacor.model.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ListadoEntrantesPojo {
    private String nur;
    private String codigo;
    private String referencia;
    private String remitenteNombre;
    private String remitenteCargo;
    private LocalDateTime fechaDerivacion;
    private String proveido;
    private String derivadoPor;
    private String derivadoA;
    private String accion;
    private String proceso;
}
