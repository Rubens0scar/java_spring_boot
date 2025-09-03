package siacor.model.pojo;

import java.util.List;

import lombok.Data;

@Data
public class ResultadoSeguimientoPojo {
    private List<String> seguimiento;
    private String permanencia;

    // Constructor
    public ResultadoSeguimientoPojo(List<String> seguimiento, String permanencia) {
        this.seguimiento = seguimiento;
        this.permanencia = permanencia;
    }
}
