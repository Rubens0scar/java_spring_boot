package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Seguimientos;
import siacor.model.pojo.SeguimientoEditPojo;
import siacor.model.pojo.SeguimientoPojo;

@Mapper
public interface SeguimientoMapper {
    SeguimientoMapper INSTANCE = Mappers.getMapper(SeguimientoMapper.class);
        @Mapping(target = "idSeguimiento", ignore = true)
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "derivadoPor", source = "derivadoPor")
        @Mapping(target = "derivadoA", source = "derivadoA")
        @Mapping(target = "fechaDerivacion", source = "fechaDerivacion")
        @Mapping(target = "fechaRecepcion", source = "fechaRecepcion")
        @Mapping(target = "estado", source = "estado")
        @Mapping(target = "idAccion", source = "idAccion")
        @Mapping(target = "observaciones", source = "observaciones")
        @Mapping(target = "padre", source = "padre")
        @Mapping(target = "oficial", source = "oficial")
        @Mapping(target = "hijo", source = "hijo")
        @Mapping(target = "escusa", source = "escusa")
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", source = "creadoPor")
        @Mapping(target = "fechaModificacion", ignore = true)
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", source = "direccionIp")
        @Mapping(target = "accion", ignore = true)
    Seguimientos InputSeguimientotoSeguimiento(SeguimientoPojo inpuSeguimientoPojo);

    SeguimientoMapper INSTANCE_EDIT = Mappers.getMapper(SeguimientoMapper.class);
        @Mapping(target = "idSeguimiento", ignore = true)
        @Mapping(target = "codigo", ignore = true)
        @Mapping(target = "derivadoPor", source = "derivadoPor")
        @Mapping(target = "derivadoA", source = "derivadoA")
        @Mapping(target = "fechaDerivacion", source = "fechaDerivacion")
        @Mapping(target = "fechaRecepcion", source = "fechaRecepcion")
        @Mapping(target = "estado", source = "estado")
        @Mapping(target = "idAccion", source = "idAccion")
        @Mapping(target = "observaciones", source = "observaciones")
        @Mapping(target = "padre", source = "padre")
        @Mapping(target = "oficial", source = "oficial")
        @Mapping(target = "hijo", source = "hijo")
        @Mapping(target = "escusa", source = "escusa")
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", ignore = true)
        @Mapping(target = "fechaModificacion", expression = "java(java.time.LocalDateTime.now())")
        @Mapping(target = "modificadoPor", source = "modificadoPor")
        @Mapping(target = "direccionIp", source = "direccionIp")
        @Mapping(target = "accion", constant = "update")
    void InputSeguimientoEditToSeguimiento(SeguimientoEditPojo inputSeguimiento, @MappingTarget Seguimientos entity);
}
