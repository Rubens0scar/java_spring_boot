package siacor.service;

import java.util.List;

import siacor.model.Dto.AgrupacionDto;
import siacor.model.entity.Agrupacion;
import siacor.model.entity.Seguimientos;
import siacor.model.pojo.AgrupacionEditPojo;
import siacor.model.pojo.AgrupacionPojo;

public interface AgrupacionService {
    List<Seguimientos> listaAgrupacion(String nuri, String usuario) throws Exception;

    Agrupacion registraAgrupacion(AgrupacionPojo inputAgrupacionPojo) throws Exception;

    List<AgrupacionPojo> listarAgrupados(String nuri) throws Exception;

    int actualizarEstados(AgrupacionEditPojo pojo) throws Exception;

    String eliminarAgrupacion(Long id) throws Exception;

    List<AgrupacionDto> reporteAgrupacion(String nuri) throws Exception;
    
}
