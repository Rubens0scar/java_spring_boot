package siacor.model.pojo;

import lombok.Data;

@Data
public class BuscadorDataPojo {
    private String usuario;
    private String cite;
    private String nur;
    private String adjuntos;
    private String referencia; 
    private String remitente;
    private String remCargo;
    private String remInstitucion;
    private String destinatario;
    private String desCargo;
    private String desInstitucion;
    private String fechaInicio;
    private String fechaFin;
    // private String fechaInicioSalida;
    // private String fechaFinSalida;
}
