package siacor.model.pojo;

import java.util.List;

import lombok.Data;
import siacor.model.Dto.DatosUsuariosDto;

@Data
public class reporteSeguimientosPojo {
    private Long nro;
    private DatosUsuariosDto datosusuario;
    private List<String> seguimiento;
    private String permanencia;
}
