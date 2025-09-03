package siacor.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.ReporteBuscadorDto;
import siacor.model.Dto.SesionesEstadoDto;
import siacor.model.Dto.SesionesEstadoUsuariosDto;
import siacor.model.pojo.BuscadorDataPojo;
import siacor.model.pojo.SesionUsuariosDetallePojo;
import siacor.model.repository.SesionesRepository;

@Service
@Transactional
public class SessionesServiceImpl implements SessionesService{
    @Autowired
    private final SesionesRepository sesionesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public SessionesServiceImpl(SesionesRepository sesionesRepository){
        this.sesionesRepository = sesionesRepository;
    }

    // @Override
    // public List<SesionesEstadoUsuariosDto> listarUsuario(Long oficina) throws Exception {
    //     try {
    //         return sesionesRepository.listarEstadoUsuarios(oficina);

    //     } catch (Exception e) {
    //         throw new Exception("Error al registrar el Seguimiento, por favor intente nuevamente." + e.getMessage());
    //     }
    // }

    @Override
    public List<SesionesEstadoUsuariosDto> listarEstadoUsuarios(Long oficina, LocalDate fecha) throws Exception {
        try {
            return sesionesRepository.listarEstadoUsuarios(oficina,fecha);

        } catch (Exception e) {
            throw new Exception("Error al listar las sesiones completas del dia, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<SesionesEstadoDto> listaEstadoUsuarios(Long oficina, LocalDate fecha) throws Exception {
        try {
            LocalDate hoy = LocalDate.now();

            if (hoy.equals(fecha)) return sesionesRepository.listaEstadoUsuarios(oficina,fecha);
            else return sesionesRepository.listaEstadoUsuariosAnterior(oficina, fecha);

        } catch (Exception e) {
            throw new Exception("Error al listar las sesiones del dia, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<SesionUsuariosDetallePojo> listaUsuarioDetalle(String usuario, LocalDate fecha) throws Exception {
        Query query = entityManager.createNativeQuery(
            "SELECT " +
                "s.id_sesion as idSesion, " +
                "u.codigo, " +
                "u.nombre_completo as nombreCompleto, " +
                "u.cargo, " +
                "CASE WHEN s.fecha_activo IS NOT NULL AND s.fecha_no_activo IS NOT NULL THEN 'ACTIVO' WHEN s.fecha_activo IS NOT NULL AND s.fecha_no_activo IS NULL THEN 'EN USO' ELSE 'INACTIVO' END AS estado, " +
                "s.fecha_activo as fechaActivo,s.fecha_no_activo as fechaCerrar, " +
                "TO_CHAR(GREATEST(COALESCE(s.fecha_no_activo, :fecha) - s.fecha_activo, interval '0'),'HH24:MI:SS') as duracion, " +
                "COALESCE(s.sesion_cerrada,'') as sesionCerrada " +
            "FROM public.usuario u " +
            "LEFT JOIN public.sesiones s ON s.usuario = u.codigo AND DATE(s.fecha_activo) = DATE(:fecha) " +
            "WHERE u.habilitado = 1 and s.fecha_activo is not null AND u.codigo = :usuario " +
            "ORDER BY s.id_sesion, u.nivel DESC;",
            "SesionUsuariosDetalleMapping" // nombre del mapping definido en el POJO
        );

        query.setParameter("usuario", usuario);
        query.setParameter("fecha", fecha);

        return query.getResultList();

    }

    
    
}
