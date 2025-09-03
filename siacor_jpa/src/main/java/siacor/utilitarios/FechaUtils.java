package siacor.utilitarios;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import siacor.model.pojo.DesplosarFechasPojo;

public class FechaUtils {
    public static String calcularDiferencia(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return "0d, 0h, 0m";
        }
        // Asegurar que fechaInicio sea menor o igual a fechaFin
        if (fechaInicio.isAfter(fechaFin)) {
            LocalDateTime temp = fechaInicio;
            fechaInicio = fechaFin;
            fechaFin = temp;
        }

        long totalSegundos = ChronoUnit.SECONDS.between(fechaInicio, fechaFin);
        long totalMinutos = Math.round(totalSegundos / 60.0);

        long dias = totalMinutos / (24 * 60);
        long horas = (totalMinutos % (24 * 60)) / 60;
        long minutos = totalMinutos % 60;

        String dataDias = dias + "d, ";
        String dataHoras = horas + "h, ";
        String dataminutos = minutos + "m";

        return dataDias + dataHoras + dataminutos;
    }

    public static List<DesplosarFechasPojo> datosDiferencia(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<DesplosarFechasPojo> data = new ArrayList<>();
        if (fechaInicio == null || fechaFin == null) {
            return null;
        }
        // Asegurar que fechaInicio sea menor o igual a fechaFin
        if (fechaInicio.isAfter(fechaFin)) {
            LocalDateTime temp = fechaInicio;
            fechaInicio = fechaFin;
            fechaFin = temp;
        }

        long totalSegundos = ChronoUnit.SECONDS.between(fechaInicio, fechaFin);
        long totalMinutos = Math.round(totalSegundos / 60.0);

        long dias = totalMinutos / (24 * 60);
        long horas = (totalMinutos % (24 * 60)) / 60;
        long minutos = totalMinutos % 60;

        data.add(new DesplosarFechasPojo(dias,horas,minutos));

        return data;
    }

    public static String totalDias(List<DesplosarFechasPojo> data){
        long totalDias = 0;
        long totalHoras = 0;
        long totalMinutos = 0;

        // Sumar sin normalizar
        for (DesplosarFechasPojo item : data) {
            totalDias += item.getDias();
            totalHoras += item.getHoras();
            totalMinutos += item.getMinutos();
        }

        // Normalizar minutos a horas
        totalHoras += totalMinutos / 60;
        totalMinutos = totalMinutos % 60;

        // Normalizar horas a días
        totalDias += totalHoras / 24;
        totalHoras = totalHoras % 24;

        return totalDias + "D " + totalHoras + "h " + totalMinutos + "m";
    }
}
