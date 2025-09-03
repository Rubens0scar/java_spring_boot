package siacor.service;

import java.util.List;

import siacor.model.Dto.ListarJefesUsuariosDptoDto;
import siacor.model.Dto.SesionesEstadoUsuariosDto;
import siacor.model.Dto.UsuarioDatosDto;
import siacor.model.Dto.UsuarioDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.Sesiones;
import siacor.model.entity.Usuarios;
import siacor.model.pojo.AutenticacionPojo;
import siacor.model.pojo.AutenticacionUsuarioPojo;
import siacor.model.pojo.UsuarioCambioPasswordPojo;
import siacor.model.pojo.UsuarioPojo;

public interface UsuarioService {
    AutenticacionPojo autentificacion(AutenticacionUsuarioPojo autenticacion) throws Exception ;

    Usuarios buscarUsuario(String usuario);

    List<ListarJefesUsuariosDptoDto> listarJefesUsuariosDpto(String usuario) throws Exception;

    Sesiones cerrarSesiones(Long id, String quien) throws Exception;

    buscaDatosDto usuarioVia(String usuario) throws Exception;

    List<SesionesEstadoUsuariosDto> sesionesEstadoUsuarios(Long oficina) throws Exception;

    Usuarios registraUsuario(UsuarioPojo pojo) throws Exception;

    UsuarioDto actualizaPassword(UsuarioCambioPasswordPojo pojo) throws Exception;

    Usuarios actualizaUsuarios(UsuarioPojo pojo) throws Exception;

    UsuarioDatosDto buscarDataUsuario(String usuario) throws Exception;
    
}
