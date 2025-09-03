package siacor.service;

import java.time.LocalDate;
import java.util.List;

import siacor.model.Dto.SesionesEstadoDto;
import siacor.model.Dto.SesionesEstadoUsuariosDto;
import siacor.model.pojo.SesionUsuariosDetallePojo;

public interface SessionesService {
    //List<SesionesEstadoUsuariosDto> listarUsuario(Long oficina) throws Exception;

    List<SesionesEstadoUsuariosDto> listarEstadoUsuarios(Long oficina, LocalDate fecha) throws Exception;

    List<SesionesEstadoDto> listaEstadoUsuarios(Long oficina, LocalDate fecha) throws Exception;

    List<SesionUsuariosDetallePojo> listaUsuarioDetalle(String usuario, LocalDate fecha) throws Exception;

}
