package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Sesiones;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface SesionesMapper {
    SesionesMapper INSTANCE = Mappers.getMapper(SesionesMapper.class);
        @Mapping(target = "idSesion", ignore = true)
        @Mapping(target = "usuario", source = "usuario")
        @Mapping(target = "estado", source = "estado")
        @Mapping(target = "fechaActivo", source = "fechaActivo")
        @Mapping(target = "fechaNoActivo", ignore = true)
        @Mapping(target = "ipUsuario", source = "ipUsuario")
        @Mapping(target = "userAgent", source = "userAgent")
    Sesiones InputSessionesToSesiones(Sesiones inputSesiones);

    SesionesMapper INSTANCE_EDIT = Mappers.getMapper(SesionesMapper.class);
        @Mapping(target = "idSesion", ignore = true)
        @Mapping(target = "usuario", ignore = true)
        @Mapping(target = "estado", source = "estado")
        @Mapping(target = "fechaActivo", ignore = true)
        @Mapping(target = "fechaNoActivo", source = "fechaNoActivo")
        @Mapping(target = "ipUsuario", ignore = true)
        @Mapping(target = "userAgent", ignore = true)
    void InputSesionEditToSesion(Sesiones inputSesiones, @MappingTarget Sesiones entity);
}
