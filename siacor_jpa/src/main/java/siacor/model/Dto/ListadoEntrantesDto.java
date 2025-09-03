package siacor.model.Dto;

import java.time.LocalDateTime;

public interface ListadoEntrantesDto {
    Long getIdSeguimiento();
    String getNur();
    String getCodigo();
    String getReferencia();
    String getRemitenteNombre();
    String getRemitenteCargo();
    LocalDateTime getFechaDerivacion();
    String getProveido();
    String getDerivadoPor();
    String getDerivadoA();
    String getAccion();
    String getProceso();
    String getCreadoPor();
    Integer getOficial();
}
