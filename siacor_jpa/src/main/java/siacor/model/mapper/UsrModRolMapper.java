package siacor.model.mapper;

import org.mapstruct.factory.Mappers;

import siacor.model.entity.UsrModRol;
import siacor.model.pojo.UsrModRolPojo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UsrModRolMapper {
    UsrModRolMapper INSTANCE = Mappers.getMapper(UsrModRolMapper.class);
        @Mapping(target = "idUsrModRol", ignore = true)
        @Mapping(target = "codigoUsuario", source = "codigoUsuario")
        @Mapping(target = "codRol", source = "codRol")
        @Mapping(target = "codModulo", source = "codModulo")
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", source = "creadoPor")
        @Mapping(target = "fechaModificacion", ignore = true)
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", source = "direccionIp")
        @Mapping(target = "accion", ignore = true)
    UsrModRol InputUsrModRolToUsrModRol(UsrModRolPojo pojo);
}
