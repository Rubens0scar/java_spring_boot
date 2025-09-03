package siacor.model.Dto;

import java.time.LocalDateTime;

public interface CabeceraSeguimientoDto {
    String getNur();
    String getCite();
    String getReferencia();
    String getRemitenteNombre();
    String getRemitenteCargo();
    String getProveido();
    String getDerivadoPor();
    String getDerivadoA();
    String getAccion();
    String getProceso();
    String getDestinatarioCargo();
    String getDestinatarioNombre();
    String getAdjuntos();
    String getTipoDocumento();
    LocalDateTime getFechaCreacion();
    Integer getOficial();
//    ListaSeguimientosDto getListaSeguimiento();
}
