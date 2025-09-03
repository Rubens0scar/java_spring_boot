package siacor.service;

import java.util.List;

import siacor.model.Dto.ArchivadosSalidaDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.entity.Archivados;
import siacor.model.pojo.ArchivadosPojo;

public interface ArchivadosService {
    Archivados registraArchivados(ArchivadosPojo inputArchivadosPojo) throws Exception;

    List<Archivados> listarArchivados(String usuario, String lugar, String copia) throws Exception;

    List<SalidaDataDto> listarArchivadosLugar(String usuario, String copia) throws Exception;

    List<ArchivadosSalidaDto> listarArchivadosReporte(Long oficina) throws Exception;

    Archivados datosArchivado(Long idArchivados) throws Exception;

    Archivados actualizaDatos(ArchivadosPojo pojo) throws Exception;
}
