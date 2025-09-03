package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Agrupacion;
import siacor.model.pojo.AgrupacionPojo;

@Mapper
public interface AgrupacionMapper {
    AgrupacionMapper INSTANCE = Mappers.getMapper(AgrupacionMapper.class);
		@Mapping(target = "idAgru", ignore = true)
		@Mapping(target = "nurP", source = "nurP")
		@Mapping(target = "nurS", source = "nurS")
		@Mapping(target = "idSeguimiento", source = "idSeguimiento")
		@Mapping(target = "oficial", source = "oficial")
		@Mapping(target = "estaGru", ignore = true)
		@Mapping(target = "habilitado", ignore = true)
		@Mapping(target = "fechaCreacion", ignore = true)
		@Mapping(target = "creadoPor", source = "creadoPor")
		@Mapping(target = "fechaModificacion", ignore = true)
		@Mapping(target = "modificadoPor", ignore = true)
		@Mapping(target = "direccionIp", source = "direccionIp")
		@Mapping(target = "accion", ignore = true)
    Agrupacion InputAgregadosToAgregados(AgrupacionPojo inputAgrupacionPojo);
}
