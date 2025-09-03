package siacor.model.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.SesionesEstadoDto;
import siacor.model.Dto.SesionesEstadoUsuariosDto;
import siacor.model.entity.Sesiones;
import siacor.model.pojo.SesionUsuariosDetallePojo;

@Repository
public interface SesionesRepository extends JpaRepository<Sesiones,Long> {
    Sesiones findByIdSesion(Long idSesion);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE public.sesiones SET fecha_no_activo=now(),estado=false,sesion_cerrada=:quien WHERE id_sesion = :id",
        nativeQuery = true
    )
    int actualizarSesion(Long id, String quien);

    @Query(value = "select s.id_sesion as idSesion, u.codigo,u.nombre_completo as nombreCompleto,u.cargo,p.nombre as oficina,case when s.estado then 'ACTIVA' else 'INACTIVA' end as estadoActual " +
                         ",s.fecha_activo " +
                         ",COALESCE(TO_CHAR(s.fecha_no_activo, 'YYYY-MM-DD HH24:MI:SS'), '') AS fecha_no_activo " + 
                         ",TO_CHAR(COALESCE(s.fecha_no_activo, now()) - s.fecha_activo, 'HH24:MI:SS') AS duracion " +
                   "from public.usuario u " +
                   "inner join public.parametrica p on p.id_parametrica = u.oficina " +
                   "left join public.sesiones s on s.usuario = u.codigo " +
                   "where u.habilitado = 1 and DATE(s.fecha_activo)=date(:fecha) and u.oficina = :oficina " +
                   "order by s.id_sesion,u.nivel desc", 
           nativeQuery = true)
    List<SesionesEstadoUsuariosDto> listarEstadoUsuarios(Long oficina, LocalDate fecha);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE public.sesiones SET fecha_no_activo=fecha_activo + make_interval(mins => :minutos),estado=false WHERE fecha_no_activo is null and usuario = :usuario and id_sesion <> :idSesion",
        nativeQuery = true
    )
    int actualizarSesionAnterior(String usuario, Long idSesion, Integer minutos);

    @Query(value = "SELECT u.codigo,u.nombre_completo as nombreCompleto,u.cargo, " +
                   "    CASE " +
                   "        WHEN EXISTS ( " +
                   "            SELECT 1 FROM public.sesiones s WHERE s.usuario = u.codigo AND s.estado = true AND date(s.fecha_activo) = date(:fecha) " +
                   "        ) THEN 'ACTIVO' " +
                   "        ELSE 'INACTIVO' " +
                   "    END AS estadoActual " +
                   "FROM public.usuario u " +
                   "WHERE u.habilitado = 1 AND u.oficina = :oficina " +
                   "order by u.nivel desc;", 
           nativeQuery = true)
    List<SesionesEstadoDto> listaEstadoUsuarios(Long oficina, LocalDate fecha);

    @Query(value = "SELECT u.codigo,u.nombre_completo as nombreCompleto,u.cargo, " +
                   "    CASE " +
                   "        WHEN EXISTS ( " +
                   "            SELECT 1 FROM public.sesiones s WHERE s.usuario = u.codigo AND date(s.fecha_activo) = date(:fecha) " +
                   "        ) THEN 'ACTIVO' " +
                   "        ELSE 'INACTIVO' " +
                   "    END AS estadoActual " +
                   "FROM public.usuario u " +
                   "WHERE u.habilitado = 1 AND u.oficina = :oficina " +
                   "order by u.nivel desc;", 
           nativeQuery = true)
    List<SesionesEstadoDto> listaEstadoUsuariosAnterior(Long oficina, LocalDate fecha);

    @Query(value = "SELECT s.id_sesion as idSesion, " +
                   "    u.codigo, " +
                   "    u.nombre_completo as nombreCompleto, " +
                   "    u.cargo, " +
                   "    CASE WHEN s.estado THEN 'ACTIVA' ELSE 'INACTIVA' end as estado, " +
                   "    s.fecha_activo as fechaActivo,s.fecha_no_activo as fechaCerrar, " +
                   "    TO_CHAR(COALESCE(s.fecha_no_activo, :fecha) - s.fecha_activo, 'HH24:MI:SS') as duracion, " +
                   "    s.sesion_cerrada as sesionCerrada " +
                   "FROM public.usuario u " +
                   "LEFT JOIN public.sesiones s ON s.usuario = u.codigo AND DATE(s.fecha_activo) = DATE(:fecha) " +
                   "WHERE u.habilitado = 1 and s.fecha_activo is not null AND u.codigo = :usuario " +
                   "ORDER BY s.id_sesion, u.nivel DESC;", 
           nativeQuery = true)
    List<SesionUsuariosDetallePojo> listaUsuarioDetalle(String usuario, LocalDate fecha);
}
