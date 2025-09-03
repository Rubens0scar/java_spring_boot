package siacor.model.Dto;

import java.time.LocalDateTime;

public interface DatosHojaRutaDto {
    String getProcedencia();
    String getRemitente();
    String getCite();
    LocalDateTime getFecha();
    String getReferencia();
    String getAdjuntos();
    Integer getNroHojas();
    String getProceso();
    String getPlazo();
    String getProveido();
    String getDestinatario();
    LocalDateTime getFechaDerivacion();
    Long getIdAccion();
    Long getTipoDocumento();
}
