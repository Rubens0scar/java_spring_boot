package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Vias;
import siacor.model.pojo.ViasPojo;

@Mapper
public interface ViasMapper {
    ViasMapper INSTANCE = Mappers.getMapper(ViasMapper.class);
        @Mapping(target = "idVias", ignore = true)
        @Mapping(target = "codigoUsuario", source = "codigoUsuario")
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", source = "creadoPor")
        @Mapping(target = "fechaModificacion", ignore = true)
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", source = "direccionIp")
        @Mapping(target = "accion", constant = "insert")
    Vias InputViasToVias(ViasPojo inputVias);

    ViasMapper INSTANCE_EDIT = Mappers.getMapper(ViasMapper.class);
        @Mapping(target = "idVias", ignore = true)
        @Mapping(target = "codigoUsuario", source = "codigoUsuario")
        @Mapping(target = "codigo", ignore = true)
        @Mapping(target = "habilitado", source = "habilitado")
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", ignore = true)
        @Mapping(target = "fechaModificacion", expression = "java(java.time.LocalDateTime.now())")
        @Mapping(target = "modificadoPor", source = "modificadoPor")
        @Mapping(target = "direccionIp", ignore = true)
        @Mapping(target = "accion", constant = "update")
    void InputEditViasToVias(Vias inputVias,@MappingTarget Vias entity);
}
