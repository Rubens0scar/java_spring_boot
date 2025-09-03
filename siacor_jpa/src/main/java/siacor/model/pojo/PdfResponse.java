package siacor.model.pojo;

import lombok.Data;

@Data
public class PdfResponse {
    private String nuri;
    private String nombreArchivo;
    private String extension;
    private long tamañoOriginalKB;
    private long tamañoComprimidoKB;
    private String rutaGuardado;

    public PdfResponse(String nuri, String nombreArchivo, String extension, long tamañoOriginalKB, long tamañoComprimidoKB, String rutaGuardado) {
        this.nuri = nuri;
        this.nombreArchivo = nombreArchivo;
        this.extension = extension;
        this.tamañoOriginalKB = tamañoOriginalKB;
        this.tamañoComprimidoKB = tamañoComprimidoKB;
        this.rutaGuardado = rutaGuardado;
    }
}
