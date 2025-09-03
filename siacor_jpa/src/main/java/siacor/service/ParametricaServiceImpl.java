package siacor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.ModuloDto;
import siacor.model.entity.Parametrica;
import siacor.model.mapper.ParametricaMapper;
import siacor.model.pojo.ParametricaEditPojo;
import siacor.model.pojo.ParametricaPojo;
import siacor.model.repository.ParametricaRepository;

@Service
@Transactional
public class ParametricaServiceImpl implements ParametricaService{
    @Autowired
    private final ParametricaRepository parametricaRepository;

    public ParametricaServiceImpl(ParametricaRepository parametricaRepository){
        this.parametricaRepository = parametricaRepository;
    }

    @Override
    public List<ModuloDto> listarModulos(String tipo) {
        return parametricaRepository.listarParametrica(tipo);
    }

    @Override
    public Parametrica registraParametrica(ParametricaPojo parametricaPojo) throws Exception {
        try {
            Parametrica parametrica = ParametricaMapper.INSTANCE.InputParametricaToParametrica(parametricaPojo);
            parametricaRepository.save(parametrica);

            return parametrica;

        } catch (BadCredentialsException e) {
            throw new Exception("Error al registrar la parametrica, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public Parametrica actualizarParametrcia(ParametricaEditPojo parametricaEditPojo) throws Exception {
        try {
            Parametrica parametrica = parametricaRepository.findByIdParametrica(parametricaEditPojo.getIdParametrica());


            // Parametrica parametrica = ParametricaMapper.INSTANCE.InputParametricaToParametrica(parametricaPojo);
            // parametricaRepository.save(parametrica);

            return parametrica;

        } catch (BadCredentialsException e) {
            throw new Exception("Error al registrar la parametrica, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public Parametrica buscarTipoDocumento(String codigo) throws Exception {
        try {
            Parametrica parametrica = parametricaRepository.findByCodigo(codigo);

            return parametrica;

        } catch (BadCredentialsException e) {
            throw new Exception("Error al registrar la parametrica, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public Parametrica buscarDatosID(Long idParametrica) throws Exception {
        try {
            Parametrica parametrica = parametricaRepository.findByIdParametrica(idParametrica);

            return parametrica;

        } catch (BadCredentialsException e) {
            throw new Exception("Error al buscar la parametrica, por favor intente nuevamente." + e.getMessage());
        }
    }

    
}
