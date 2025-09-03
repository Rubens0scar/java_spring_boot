package siacor.service;

import java.util.List;

import siacor.model.Dto.ListarDerivacionDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.ListasDerivacion;
import siacor.model.pojo.DataActualizaListaPojo;

public interface ListasDerivacionService {
    buscaDatosDto buscarJefeUsuario(String usuario) throws Exception;

    List<buscaDatosDto> listaDerivacion(String usuario) throws Exception;
    
    List<ListarDerivacionDto> listaDerivacionUsuario(String usuario) throws Exception;

    ListasDerivacion actualizaOpcionLista(DataActualizaListaPojo pojo) throws Exception;
}
