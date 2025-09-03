package siacor.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.interceptor.ActivityInterceptor;
import siacor.model.Dto.ListarJefesUsuariosDptoDto;
import siacor.model.Dto.RolesDto;
import siacor.model.Dto.SesionesEstadoUsuariosDto;
import siacor.model.Dto.UsuarioDatosDto;
import siacor.model.Dto.UsuarioDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.Parametrica;
import siacor.model.entity.Sesiones;
import siacor.model.entity.UsrModRol;
import siacor.model.entity.Usuarios;
import siacor.model.mapper.SesionesMapper;
import siacor.model.mapper.UsrModRolMapper;
import siacor.model.mapper.UsuarioMapper;
import siacor.model.pojo.AutenticacionPojo;
import siacor.model.pojo.AutenticacionUsuarioPojo;
import siacor.model.pojo.UsrModRolPojo;
import siacor.model.pojo.UsuarioCambioPasswordPojo;
import siacor.model.pojo.UsuarioPojo;
import siacor.model.repository.ParametricaRepository;
import siacor.model.repository.SesionesRepository;
import siacor.model.repository.UsrModRolRepository;
import siacor.model.repository.UsuariosRepository;

import siacor.utilitarios.PasswordEncryptor;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    @Value("${jwt.minutosExpiracion}")
    private Integer minutosExpiracion;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final SesionesRepository sesionesRepository;

    @Autowired
    private final UsuariosRepository usuariosRepository;

    @Autowired
    private final ParametricaRepository parametricaRepository;

    @Autowired
    private final UsrModRolRepository usrModRolRepository;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    public UsuarioServiceImpl(UsuariosRepository usuariosRepository,UsrModRolRepository usrModRolRepository,SesionesRepository sesionesRepository,ParametricaRepository parametricaRepository){
        this.usuariosRepository = usuariosRepository;
        this.usrModRolRepository = usrModRolRepository;
        this.sesionesRepository = sesionesRepository;
        this.parametricaRepository = parametricaRepository;
    }

    @Override
    public Usuarios buscarUsuario(String usuario) {
        return usuariosRepository.findByCodigo(usuario);
    }

    @Override
    public AutenticacionPojo autentificacion(AutenticacionUsuarioPojo autenticacion) throws Exception {
        // Cargar los detalles del usuario desde la base de datos
        UsuarioDto passUsr = usuariosRepository.buscarPassword(autenticacion.getUsuario());
        if (passUsr == null) throw new Exception("Usuario no encontrado, por favor revisar el nombre de usuario y/o contraseña.");

        // Autenticar al usuario
        authenticate(autenticacion.getUsuario(), autenticacion.getPassword());

        // Verificar la contraseña
        if (!passwordEncoder.matches(autenticacion.getPassword(), passUsr.getPassword())) {
            throw new Exception("Credenciales inválidas: Usuario no encontrado, por favor revisar el nombre de usuario y/o contraseña.");
        }

        // Verificamos los roles del usuario
        RolesDto usrRol = usrModRolRepository.buscarRol(autenticacion.getUsuario(), autenticacion.getModulo());
        if (usrRol == null) throw new Exception("Usuario no cuenta con los roles necesarios para ingresar al sistema.");
        System.out.println("DATA: " + usrRol.getNombre());

        // Cargar los detalles del usuario para generar el token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(autenticacion.getUsuario());

        // Generar el token JWT
        final String token = jwtTokenUtil.generateToken(userDetails);

        Usuarios usuarios = usuariosRepository.findByCodigo(passUsr.getCodigo());

        Sesiones sesion = new Sesiones();
        sesion.setUsuario(autenticacion.getUsuario());
        sesion.setEstado(true);
        sesion.setFechaActivo(LocalDateTime.now());
        sesion.setIpUsuario(autenticacion.getIp());
        sesion.setUserAgent(autenticacion.getAgente());
        Sesiones resultadoSesion = SesionesMapper.INSTANCE.InputSessionesToSesiones(sesion);
        sesionesRepository.save(resultadoSesion);

        Parametrica par = parametricaRepository.findByIdParametrica(usuarios.getOficina());

        AutenticacionPojo usr = new AutenticacionPojo(
            usuarios.getCodigo(),
            usuarios.getNombreCompleto(),
            usuarios.getPaginaInicio(),
            usuarios.getEmail(),
            usuarios.getOficina(),
            usuarios.getUnidad(),
            usuarios.getPermisos(),
            usuarios.getCargo(),
            usuarios.getMosca(),
            usuarios.getNivel(),
            usuarios.getAccion(),
            usuarios.getGenero(),
            usrRol,
            resultadoSesion.getIdSesion(),
            usuarios.getValidaPassword(),
            token,
            usrRol.getIdUsrModRol(),
            par.getValorTexto(),
            par.getValorBooleano()
        );

        ActivityInterceptor.cargarActividad(autenticacion.getUsuario(), resultadoSesion.getIdSesion());

        int a = sesionesRepository.actualizarSesionAnterior(autenticacion.getUsuario(),resultadoSesion.getIdSesion(),minutosExpiracion);
        
        System.out.println("Se actualizaron " + a + " sesiones.");
        
        return usr;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new Exception("Credenciales inválidas: Por favor revisar el nombre de usuario y/o contraseña." + e.getMessage());
        }
    }

    @Override
    public List<ListarJefesUsuariosDptoDto> listarJefesUsuariosDpto(String usuario) throws Exception {
        try {
            Usuarios usr = usuariosRepository.findByCodigo(usuario);
            return usuariosRepository.listadoUsuarios(usuario,usr.getOficina());
        } catch (Exception e) {

            throw new Exception("Problemas en el listado, por favor intentar nuevamente." + e.getMessage());
        }
    }

    @Override
    public Sesiones cerrarSesiones(Long id, String quien) throws Exception {
        try {
            Sesiones sesionInicio = sesionesRepository.findByIdSesion(id);
            if(sesionInicio == null) throw new Exception("No se encontro la sesion.");

            int res = sesionesRepository.actualizarSesion(id,quien);

            if(res == 1) return sesionesRepository.findByIdSesion(id);
            throw new Exception("No se actualizo el registro de la sesion.");
        } catch (Exception e) {
            throw new Exception("Problemas en la actualizacion de la sesión, por favor intentar nuevamente. " + e.getMessage());
        }
    }

    @Override
    public buscaDatosDto usuarioVia(String usuario) throws Exception {
        try {
            Usuarios usr = usuariosRepository.findByCodigo(usuario);
            if(usr == null) throw new Exception("No se encontro al usuario enviado.");

            // if(usr.getNivel() == 0){
            //     return usuariosRepository.usuarioVia(usr.getOficina());
            // } else if(usr.getNivel() == 1){
            //     return usuariosRepository.usuarioViaSuperior(usr.getNivel()+1);
            // } else {

            // }
            return usuariosRepository.usuarioViaSuperior(usr.getOficina());
        } catch (Exception e) {
            throw new Exception("Problemas en buscar Usuario VIA, por favor intentar nuevamente. " + e.getMessage());
        }
    }

    @Override
    public List<SesionesEstadoUsuariosDto> sesionesEstadoUsuarios(Long oficina) throws Exception {
        try {
            return usuariosRepository.sesionesEstadoUsuarios(oficina);
        } catch (Exception e) {
            throw new Exception("Problemas en la actualizacion de la sesión, por favor intentar nuevamente. " + e.getMessage());
        }
    }

    @Override
    public Usuarios registraUsuario(UsuarioPojo pojo) throws Exception {
        try {
            Usuarios usr = usuariosRepository.findByCodigo(pojo.getCodigo());
            if(usr != null) throw new Exception("Ya existe un registro con el usuario: " + pojo.getCodigo() + ", por favor cambie de usuario.");

            String pass = passwordEncryptor.encryptPassword("MUMANAL");
            pojo.setPassword(pass);

            Usuarios usuario = UsuarioMapper.INSTANCE.InputUsuarioToUsuario(pojo);
            usuariosRepository.save(usuario);

            UsrModRol usrMod = usrModRolRepository.findByCodigoUsuarioAndCodModulo(pojo.getCodigo(), pojo.getModulo());
            if(usrMod == null){
                UsrModRolPojo usrModRolPojo = new UsrModRolPojo();
                usrModRolPojo.setCodigoUsuario(pojo.getCodigo());
                usrModRolPojo.setCodRol(pojo.getRol());
                usrModRolPojo.setCodModulo(pojo.getModulo());
                usrModRolPojo.setCreadoPor(pojo.getCreadoPor());
                usrModRolPojo.setDireccionIp(pojo.getDireccionIp());

                UsrModRol rol = UsrModRolMapper.INSTANCE.InputUsrModRolToUsrModRol(usrModRolPojo);
                usrModRolRepository.save(rol);
            }
            return usuario;
        } catch (Exception e) {
            throw new Exception("No se registro al usuario, por favor intentar nuevamente. " + e.getMessage());
        }
    }

    @Override
    public UsuarioDto actualizaPassword(UsuarioCambioPasswordPojo pojo) throws Exception {
        try {
            UsuarioDto dto = null;

            UsuarioDto passUsr = usuariosRepository.buscarPassword(pojo.getUsuario());
            if (passUsr == null) throw new Exception("Usuario no encontrado, por favor revisar el nombre de usuario y/o contraseña.");

            // Verificar la contraseña
            if (!passwordEncoder.matches(pojo.getAnteriorPassword(), passUsr.getPassword())) {
                throw new Exception("Contraseña Anterior no es igual con la registrada en la base de datos.");
            }

            //Verificamos que el nuevo password sea mayor o igual a 8 caracteres
            if(pojo.getNuevoPassword().length() < 8) return dto;

            int res = usuariosRepository.actualizarPassword(pojo.getUsuario(), passwordEncryptor.encryptPassword(pojo.getNuevoPassword()));

            if (res == 1)  {
                dto = new UsuarioDto(pojo.getUsuario(), passwordEncryptor.encryptPassword(pojo.getNuevoPassword()));
                return dto;
            }
            
            throw new Exception("No se pudo actualizar el password, por favor intentar nuevamente. ");
        } catch (Exception e) {
            throw new Exception("Se tuvo un problema al actualizar el password. " + e.getMessage());
        }
    }

    @Override
    public Usuarios actualizaUsuarios(UsuarioPojo pojo) throws Exception {
        try {
            Usuarios user = usuariosRepository.findByCodigo(pojo.getCodigo());
            if(user == null) throw new Exception("No se encontro los datos del usuario " + pojo.getCodigo() + ".");

            int resUser = usuariosRepository.actualizarUsuario(pojo.getCodigo(), pojo.getNombreCompleto(), pojo.getPaginaInicio(), pojo.getEmail(), pojo.getOficina(), pojo.getUnidad(), pojo.getPermisos(), pojo.getCargo(), pojo.getMosca(), pojo.getNivel(), pojo.getGenero(), pojo.getModificadoPor());

            int resUsMod = usrModRolRepository.actualizarUsrMod(pojo.getIdUsrModRol(), pojo.getRol(), pojo.getModulo(), pojo.getModificadoPor());

            if(resUser == 1 && resUsMod == 1) return usuariosRepository.findByCodigo(pojo.getCodigo());
            throw new Exception("Exisitio un problema al actualizar, por favor verifique.");

        } catch (Exception e) {
            throw new Exception("Problemas en la actualizacion: " + e.getMessage());
        }
    }

    @Override
    public UsuarioDatosDto buscarDataUsuario(String usuario) throws Exception {
        try {
            return usuariosRepository.buscarDataUsuario(usuario);
        } catch (Exception e) {
            throw new Exception("Problemas en la buscar datos del Usuario: " + usuario + "..Error: " + e.getMessage());
        }
    }

    
}
