package siacor.service;

import java.util.List;

import siacor.model.Dto.ModuloDto;
import siacor.model.entity.Parametrica;
import siacor.model.pojo.ParametricaEditPojo;
import siacor.model.pojo.ParametricaPojo;

public interface ParametricaService {

    List<ModuloDto> listarModulos(String tipo);

    Parametrica registraParametrica(ParametricaPojo parametricaPojo) throws Exception;

    Parametrica actualizarParametrcia(ParametricaEditPojo parametricaEditPojo) throws Exception;

    Parametrica buscarTipoDocumento(String codigo) throws Exception;

    Parametrica buscarDatosID(Long idParametrica) throws Exception;
    
}
