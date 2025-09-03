package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Correlativos;
import siacor.model.pojo.CorrelativosPojo;

@Mapper
public interface CorrelativosMapper {

    CorrelativosMapper INSTANCE_EDIT = Mappers.getMapper(CorrelativosMapper.class);
        @Mapping(target = "idCor", ignore = true)
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "correlativo", source = "correlativo")
        @Mapping(target = "gestion", source = "gestion")
        @Mapping(target = "oficina", source = "oficina")
        @Mapping(target = "habilitado", source = "habilitado")
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", ignore = true)
        @Mapping(target = "fechaModificacion", expression = "java(java.time.LocalDateTime.now())")
        @Mapping(target = "modificadoPor", source = "modificadoPor")
        @Mapping(target = "direccionIp", ignore = true)
        @Mapping(target = "accion", source = "accion")
    void InputCorrelativosToCorrelativos(CorrelativosPojo inputCorrelativo, @MappingTarget Correlativos entity);

    // CorrelativosMapper INSTANCE = Mappers.getMapper(CorrelativosMapper.class);
    // Correlativos InputCorrelativosToCorrelativos(CorrelativosPojo inpuCorrelativosPojo);
}
