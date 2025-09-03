package siacor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.ListarDerivacionDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.ListasDerivacion;
import siacor.model.pojo.DataActualizaListaPojo;
import siacor.model.repository.ListasDerivacionRepository;

@Service
@Transactional
public class ListasDerivacionServiceImpl implements ListasDerivacionService{
    @Autowired
    private final ListasDerivacionRepository listasDerivacionRepository;

    public ListasDerivacionServiceImpl(ListasDerivacionRepository listasDerivacionRepository){
        this.listasDerivacionRepository = listasDerivacionRepository;
    }

    @Override
    public buscaDatosDto buscarJefeUsuario(String usuario) throws Exception {
        try{
            System.out.println("USER: " + usuario);
            return listasDerivacionRepository.buscarJefeUsuario(usuario);
        }catch(Exception e){
            throw new Exception("Error al buscar al jefe del departamento del usuario " + usuario + ", por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<buscaDatosDto> listaDerivacion(String usuario) throws Exception {
        try{
            return listasDerivacionRepository.listaDerivacion(usuario);
        }catch(Exception e){
            throw new Exception("Error al listar la derevivacion que puede ralizar el usuario " + usuario + ", por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<ListarDerivacionDto> listaDerivacionUsuario(String usuario) throws Exception {
        try{
            return listasDerivacionRepository.listaDerivacionUsuario(usuario);
        }catch(Exception e){
            throw new Exception("Error al listar la derevivacion que puede ralizar el usuario " + usuario + "." + e.getMessage());
        }
    }

    @Override
    public ListasDerivacion actualizaOpcionLista(DataActualizaListaPojo pojo) throws Exception {
        try{
            int res = listasDerivacionRepository.actualizaOpcionLista(pojo.getOpcion(),pojo.getUsrOrigen(),pojo.getUsrDestino());
            if(res ==1) return listasDerivacionRepository.findByUsrOrigenAndUsrDestino(pojo.getUsrOrigen(),pojo.getUsrDestino());
            throw new Exception("Error al actulaizar Opcion.");
        }catch(Exception e){
            throw new Exception("Error al actualizar la derevivacion." + e.getMessage());
        }
    }
    
}
