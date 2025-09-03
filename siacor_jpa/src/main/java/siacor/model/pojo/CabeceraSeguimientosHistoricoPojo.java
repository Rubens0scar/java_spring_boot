package siacor.model.pojo;

import java.util.List;

import lombok.Data;
import siacor.model.Dto.SalidaDataDto;

@Data
public class CabeceraSeguimientosHistoricoPojo {
    private List<CabeceraSeguimientoPojo> cabecera;
    private List<SalidaDataDto> citesAdjuntos;
    private List<ListaSeguimientosPojo> listaSeguimiento;
}
