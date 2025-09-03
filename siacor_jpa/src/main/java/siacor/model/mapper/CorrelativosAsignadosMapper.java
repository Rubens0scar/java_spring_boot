package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.CorrelativosAsignados;

@Mapper
public interface CorrelativosAsignadosMapper {
    CorrelativosAsignadosMapper INSTANCE = Mappers.getMapper(CorrelativosAsignadosMapper.class);
        @Mapping(target = "codigo", source = "codigo")    
        @Mapping(target = "persona", source = "persona")
        @Mapping(target = "fecha", ignore = true)
    CorrelativosAsignados InputCorrelativosToCorrelativos(CorrelativosAsignados inpuCorrelativosAsignados);

}
