package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.HojasRuta;
import siacor.model.pojo.HojasRutaEditPojo;

@Mapper
public interface HojasRutaMapper {
    HojasRutaMapper INSTANCE = Mappers.getMapper(HojasRutaMapper.class);
        @Mapping(target = "nur", source = "nur")
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "nro", ignore = true)
        @Mapping(target = "estado", source = "estado")
        @Mapping(target = "fecha", ignore = true)
        @Mapping(target = "usuario", source = "usuario")
        @Mapping(target = "proceso", source = "proceso")
        @Mapping(target = "viasCorr", ignore = true)
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", source = "creadoPor")
        @Mapping(target = "fechaModificacion", ignore = true)
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", source = "direccionIp")
    HojasRuta InputHojasRutaToHojasRuta(HojasRuta inputHojasRuta);

    HojasRutaMapper INSTANCE_EDIT =Mappers.getMapper(HojasRutaMapper.class);
        @Mapping(target = "nur", source = "nur")
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "nro", source = "nro")
        @Mapping(target = "estado", source = "estado")
        @Mapping(target = "fecha", ignore = true)
        @Mapping(target = "usuario", ignore = true)
        @Mapping(target = "proceso", ignore = true)
        @Mapping(target = "viasCorr", ignore = true)
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", ignore = true)
        @Mapping(target = "fechaModificacion", expression = "java(java.time.LocalDateTime.now())")
        @Mapping(target = "modificadoPor", source = "modificadoPor")
        @Mapping(target = "direccionIp", ignore = true)
        @Mapping(target = "accion", constant = "update")
    void InputHojasRutaEditToHojasRuta(HojasRutaEditPojo inpRuta, @MappingTarget HojasRuta entity);

}
