package siacor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.ArchivadosSalidaDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.entity.Archivados;
import siacor.model.mapper.ArchivadosMapper;
import siacor.model.pojo.ArchivadosPojo;
import siacor.model.repository.ArchivadosRepository;

@Service
@Transactional
public class ArchivadosServiceImpl implements ArchivadosService{
    @Autowired
    private final ArchivadosRepository archivadosRepository;

    public ArchivadosServiceImpl(ArchivadosRepository archivadosRepository){
        this.archivadosRepository = archivadosRepository;
    }

    @Override
    public Archivados registraArchivados(ArchivadosPojo inputArchivadosPojo) throws Exception {
        try{
            Archivados archivado = archivadosRepository.findByCodigoAndCopiaAndPersona(inputArchivadosPojo.getCodigo(),inputArchivadosPojo.getCopia(),inputArchivadosPojo.getPersona());

            if(archivado != null) throw new Exception("Documento " + inputArchivadosPojo.getCodigo() + " ya fue archivado.");

            Archivados res = ArchivadosMapper.INSTANCE.InputArchivadosToArchivados(inputArchivadosPojo);

            archivadosRepository.save(res);

            return res;
        }catch(Exception e){
            throw new Exception("Error al registrar el documento archivado, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<Archivados> listarArchivados(String usuario, String lugar, String copia) throws Exception {
        try{
            return archivadosRepository.listarArchivados(usuario, lugar, copia);
        }catch(Exception e){
            throw new Exception("Error al listar los documentos archivados, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<SalidaDataDto> listarArchivadosLugar(String usuario, String copia) throws Exception {
        try{
            return archivadosRepository.listarArchivadosLugar(usuario, copia);
        }catch(Exception e){
            throw new Exception("Error al listar lugares de los documentos archivados, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<ArchivadosSalidaDto> listarArchivadosReporte(Long oficina) throws Exception {
        try{
            return archivadosRepository.listarArchivadosReporte(oficina);
        }catch(Exception e){
            throw new Exception("Error al listar lugares de los documentos archivados, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public Archivados datosArchivado(Long idArchivados) throws Exception {
        try{
            return archivadosRepository.findByIdArchivados(idArchivados);
        }catch(Exception e){
            throw new Exception("Error al recuperar los datos del documento archivado." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public Archivados actualizaDatos(ArchivadosPojo pojo) throws Exception {
        try{
            Archivados data = archivadosRepository.findByIdArchivados(pojo.getIdArchivados());
            if(data == null) throw new Exception("Error al recuperar los datos o datos no encontrados.");

            String lugar = "", observaciones = "";

            if(pojo.getLugar() == null || pojo.getLugar().isEmpty() || pojo.getLugar().equals(data.getLugar())) lugar = data.getLugar();
            else lugar = pojo.getLugar();

            if(pojo.getObservaciones() == null || pojo.getObservaciones().isEmpty() || pojo.getObservaciones().equals(data.getObservaciones())) observaciones = data.getObservaciones();
            else observaciones = pojo.getObservaciones();

            int res = archivadosRepository.actualizaDocumentoActualizado(pojo.getIdArchivados(), lugar, observaciones, pojo.getModificadoPor(), pojo.getDireccionIp());

            if(res == 1) return archivadosRepository.findByIdArchivados(pojo.getIdArchivados());
            else throw new Exception("No se actualizo los datos");
        }catch(Exception e){
            throw new Exception("Error al modificar el documento archivado." + e.getMessage());
        }
    }
    
}
