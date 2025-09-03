package siacor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.repository.ParametricaRepository;

@Service
@Transactional
public class AdministracionServiceImpl implements AdministracionService {
    @Autowired
    private final ParametricaRepository parametricaRepository;

    public AdministracionServiceImpl(ParametricaRepository parametricaRepository){
        this.parametricaRepository = parametricaRepository;
    }

    @Override
    public int resetearBase(Integer gestion) throws Exception {
        try{
            return parametricaRepository.resetearBase(gestion);
        }catch(Exception e){
            throw new Exception("Error al resetear la bsae de datos. " + e.getMessage());
        }
    }
    
}
