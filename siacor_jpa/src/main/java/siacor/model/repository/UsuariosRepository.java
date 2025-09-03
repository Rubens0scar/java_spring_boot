package siacor.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.ListarJefesUsuariosDptoDto;
import siacor.model.Dto.SesionesEstadoUsuariosDto;
import siacor.model.Dto.UsuarioDatosDto;
import siacor.model.Dto.UsuarioDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.Usuarios;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, String> {
    Usuarios findByCodigo(String usuario);

    @Query("SELECT new siacor.model.Dto.UsuarioDto(u.password, u.codigo) FROM Usuarios u WHERE u.codigo = :codigo")
    UsuarioDto buscarPassword(@Param("codigo") String codigo);

    List<Usuarios> findByNivelAndHabilitado(Integer nivel, Integer habilitado);

    @Query(value = "SELECT '' codigo, codigo usuario, password, nombre_completo, pagina_inicio, email, oficina, unidad, permisos, cargo, mosca, nivel, genero, habilitado, fecha_creacion, creado_por, fecha_modificacion, modificado_por, direccion_ip, accion " +
                    "FROM public.usuario " + 
                    "WHERE habilitado = 1 AND oficina = " +
                    "(SELECT oficina FROM public.usuario WHERE codigo = ?1) " +
                    "AND nivel = 0 AND codigo <> ?1", 
                    nativeQuery = true)
    List<Usuarios> listaUsuarios(String usuario);

//     @Query(value = " select u.codigo, '' as usuario,u.nombre_completo as nombreCompleto,u.cargo as cargo,u.mosca as mosca " +
//                         "                  from public.usuario u " +
//                         "                  where u.nivel > 0 and u.habilitado = 1 " +
//                         "                  union all " +
//                         "                  select '' codigo, u.codigo as usuario,u.nombre_completo as nombreCompleto,u.cargo as cargo,u.mosca as mosca " +
//                         "                  from public.usuario u " + 
//                         "                  where u.nivel = 0 and u.habilitado = 1 and u.oficina = :oficina and u.codigo <> :usuario", 
//            nativeQuery = true)
//     List<ListarJefesUsuariosDptoDto> listadoUsuarios(String usuario, Long oficina);
    @Query(value = " select u.codigo, '' as usuario,u.nombre_completo as nombreCompleto,u.cargo as cargo,u.mosca as mosca " +
                        "                  from public.usuario u " +
                        "                  where u.nivel > 0 and u.habilitado = 1 " +
                        "                  union all " +
                        "                  select u.codigo as codigo, u.codigo as usuario,u.nombre_completo as nombreCompleto,u.cargo as cargo,u.mosca as mosca " +
                        "                  from public.usuario u " + 
                        "                  where  u.codigo <> 'VUN' and u.nivel = 0 and u.habilitado = 1 and u.oficina = :oficina and u.codigo <> :usuario", 
           nativeQuery = true)
    List<ListarJefesUsuariosDptoDto> listadoUsuarios(String usuario, Long oficina);

    @Query(value = "select u.codigo as codigo,u.nombre_completo as nombreCompleto,u.cargo as cargo,u.mosca as mosca " +
                   "from public.usuario u " +
                   "where u.habilitado = 1 and u.oficina = :oficina and u.nivel = 1", 
           nativeQuery = true)
    buscaDatosDto usuarioVia(Long oficina);

    @Query(value = "select u.codigo as codigo,u.nombre_completo as nombreCompleto,u.cargo as cargo,u.mosca as mosca " +
                   "from public.usuario u " +
                   "where u.oficina = :oficina and u.nivel <> 0 order by u.nivel desc limit 1", 
           nativeQuery = true)
    buscaDatosDto usuarioViaSuperior(Long oficina);

    @Query(value = "select u.codigo,u.nombre_completo as nombreCompleto,u.cargo,p.nombre as oficina,case when s.estado then 'ACTIVA' else 'CERRADA' end as estadoActual " +
                   "from public.usuario u " +
                   "inner join public.parametrica p on p.id_parametrica = u.oficina " +
                   "left join (select s.* from (select max(s.fecha_activo) fecha,s.usuario from public.sesiones s group by s.usuario) s1 inner join public.sesiones s on s.usuario = s1.usuario and s.fecha_activo = s1.fecha) s on s.usuario = u.codigo " +
                   "where u.habilitado = 1 and DATE(s.fecha_activo)=date(now()) and u.oficina = :oficina " +
                   "order by u.nivel desc", 
           nativeQuery = true)
    List<SesionesEstadoUsuariosDto> sesionesEstadoUsuarios(Long oficina);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE public.usuario SET password=:password,valida_password=1 WHERE codigo = :usuario",
        nativeQuery = true
    )
    int actualizarPassword(String usuario, String password);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE public.usuario " +
                "SET nombre_completo=:nombre, pagina_inicio=:pagina, email=:mail, oficina=:oficina, unidad=:unidad, permisos=:permisos, cargo=:cargo, mosca=:mosca, nivel=:nivel, genero=:genero, fecha_modificacion=now(), modificado_por=:modificadoPor, accion='update' " + 
                "WHERE codigo=:usuario",
        nativeQuery = true
    )
    int actualizarUsuario(String usuario, String nombre, Long pagina, String mail, Long oficina, Long unidad, Long permisos, String cargo, String mosca, Integer nivel, Long genero, String modificadoPor);

    @Query(value = "select u.codigo,u.nombre_completo as nombreCompleto,p.nombre as pagina,u.email,p1.nombre as oficina,p2.nombre as permiso,u.cargo,u.mosca,u.nivel,p3.nombre as genero " +
                   "FROM public.usuario u " +
                   "inner join public.parametrica p on p.id_parametrica = u.pagina_inicio " +
                   "inner join public.parametrica p1 on p1.id_parametrica = u.oficina " +
                   "inner join public.parametrica p2 on p2.id_parametrica = u.permisos " +
                   "inner join public.parametrica p3 on p3.id_parametrica = u.genero " +
                   "where u.codigo = :usuario", 
           nativeQuery = true)
    UsuarioDatosDto buscarDataUsuario(String usuario);
}
