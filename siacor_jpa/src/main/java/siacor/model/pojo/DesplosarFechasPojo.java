package siacor.model.pojo;

import lombok.Data;

@Data
public class DesplosarFechasPojo {
    private Long dias;
    private Long horas;
    private Long minutos;

    // Constructor que falta
    public DesplosarFechasPojo(long dias, long horas, long minutos) {
        this.dias = dias;
        this.horas = horas;
        this.minutos = minutos;
    }
}
