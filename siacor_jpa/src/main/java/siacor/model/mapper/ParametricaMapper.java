package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Parametrica;
import siacor.model.pojo.ParametricaEditPojo;
import siacor.model.pojo.ParametricaPojo;

@Mapper
public interface ParametricaMapper {
    ParametricaMapper INSTANCE = Mappers.getMapper(ParametricaMapper.class);
        @Mapping(target = "idParametrica", ignore = true)
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "nombre", source = "nombre")
        @Mapping(target = "tipo", source = "tipo")
        @Mapping(target = "valorTexto", source = "valorTexto")
        @Mapping(target = "valorNumerico", source = "valorNumerico")
        @Mapping(target = "valorBooleano", source = "valorBooleano")
        @Mapping(target = "valorFecha", source = "valorFecha")
        @Mapping(target = "activo", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "fechaModificacion", ignore = true)
    Parametrica InputParametricaToParametrica(ParametricaPojo inpuParametrica);

    ParametricaMapper INSTANCE_EDIT = Mappers.getMapper(ParametricaMapper.class);
        @Mapping(target = "idParametrica", ignore = true)
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "nombre", source = "nombre")
        @Mapping(target = "tipo", source = "tipo")
        @Mapping(target = "valorTexto", source = "valorTexto")
        @Mapping(target = "valorNumerico", source = "valorNumerico")
        @Mapping(target = "valorBooleano", source = "valorBooleano")
        @Mapping(target = "valorFecha", source = "valorFecha")
        @Mapping(target = "activo", source = "activo")
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "fechaModificacion", expression = "java(java.time.LocalDateTime.now())")
    void InputParametricaEditToParametrica(ParametricaEditPojo inParametricaEditPojo,@MappingTarget Parametrica entity);

}
