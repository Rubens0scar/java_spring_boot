package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Archivados;
import siacor.model.pojo.ArchivadosPojo;

@Mapper
public interface ArchivadosMapper {
    ArchivadosMapper INSTANCE = Mappers.getMapper(ArchivadosMapper.class);
        @Mapping(target = "idArchivados", ignore = true)
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "fecha", ignore = true)
        @Mapping(target = "persona", source = "persona")
        @Mapping(target = "lugar", source = "lugar")
        @Mapping(target = "observaciones", source = "observaciones")
        @Mapping(target = "copia", source = "copia")
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", source = "persona")
        @Mapping(target = "fechaModificacion", ignore = true)
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", source = "direccionIp")
        @Mapping(target = "accion", ignore = true)
    Archivados InputArchivadosToArchivados(ArchivadosPojo inputArchivadosPojo);
}
