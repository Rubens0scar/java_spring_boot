package siacor.model.Dto;

import java.time.LocalDateTime;

public interface SeguimientosDto {
    Long getIdSeguimiento();
	String getCodigo();
	String getDerivadoPor();
	String getDerivadoA();
	LocalDateTime getFechaDerivacion();
	LocalDateTime getFechaRecepcion();
	Long getEstado();
    Integer getOficial();
}
