package siacor.model.pojo;

import lombok.Data;

@Data
public class DatosHojaRutaPojo {
    private Long idParametrica;
    private String codigo;
    private String nombre;
    
    public DatosHojaRutaPojo(Long idParametrica,String nombre,String codigo){
        this.idParametrica = idParametrica;
        this.codigo = codigo;
        this.nombre = nombre;
    }
}
