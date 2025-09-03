package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Usuarios;
import siacor.model.pojo.UsuarioPojo;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
        @Mapping(target = "codigo", source ="codigo")
        @Mapping(target = "password", source ="password")
        @Mapping(target = "nombreCompleto", source ="nombreCompleto")
        @Mapping(target = "paginaInicio", source ="paginaInicio")
        @Mapping(target = "email", source ="email")
        @Mapping(target = "oficina", source ="oficina")
        @Mapping(target = "unidad", source ="unidad")
        @Mapping(target = "permisos", source ="permisos")
        @Mapping(target = "cargo", source ="cargo")
        @Mapping(target = "mosca", source ="mosca")
        @Mapping(target = "nivel", source ="nivel")
        @Mapping(target = "genero", source ="genero")
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", source ="creadoPor")
        @Mapping(target = "fechaModificacion", ignore = true)
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", source ="direccionIp")
        @Mapping(target = "accion", ignore = true)
    Usuarios InputUsuarioToUsuario(UsuarioPojo pojo);

}
