package siacor.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.AgrupacionDto;
import siacor.model.Dto.AgrupacionSalidaDto;
import siacor.model.entity.Agrupacion;

import java.util.List;


@Repository
public interface AgrupacionRepository extends JpaRepository<Agrupacion, Long> {
    Agrupacion findByNurPAndNurSAndIdSeguimiento(String nurp, String nurs, Long idSeguimiento);

    List<Agrupacion> findByNurPOrderByIdAgruAsc(String nurP);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.agrupacion SET esta_gru = 1,modificado_por = :modificadoPor,fecha_modificacion = now(), accion = 'update' WHERE nur_p = :nuri",
        nativeQuery = true
    )
    int actualizarEstados(String nuri, String modificadoPor);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "delete from siacor.agrupacion WHERE id_agru = :id",
        nativeQuery = true
    )
    int eliminarAgrupacion(Long id);

    Agrupacion findByIdAgru(Long idAgru);

    @Query(value = "select a.nur_s as nuri, COALESCE(TO_CHAR(a.fecha_creacion, 'YYYY-MM-DD HH24:MI:SS'), '') as fechaCreacion, " +
                   "   (select usuario from siacor.hojas_ruta hr where nro = -1 and nur = a.nur_s) as usuario, CASE WHEN a.oficial = 1 THEN 'SI' ELSE 'NO' END as oficial " +
                   "from siacor.agrupacion a where a.nur_p = :nuri", 
           nativeQuery = true)
    List<AgrupacionDto> reporteAgrupacion(String nuri);

    @Query(value = "select id_seguimiento as id, oficial from siacor.agrupacion where id_agru = :id", nativeQuery = true)
    AgrupacionSalidaDto buscarData(Long id);

}
