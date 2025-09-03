package siacor.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.ArchivadosSalidaDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.entity.Archivados;

@Repository
public interface ArchivadosRepository extends JpaRepository<Archivados, Long> {
    Archivados findByCodigo(String nuri);

    Archivados findByCodigoAndCopiaAndPersona(String nuri, String copia, String persona);

    @Query(value = "select * from siacor.archivados where persona = :usuario and lugar = :lugar and copia = :copia", nativeQuery = true)
    List<Archivados> listarArchivados(String usuario, String lugar, String copia);

    @Query(value = "select distinct(lugar) as codigo from siacor.archivados where copia = :copia and persona = :usuario order by lugar", nativeQuery = true)
    List<SalidaDataDto> listarArchivadosLugar(String usuario, String copia);

    @Query(value = "select h.nur,public.formatear_fecha(h.fecha,'3') as fechaCreacion,h.usuario,public.formatear_fecha(a.fecha,'3') as fechaArchivo,p.codigo as oficina,a.persona as usuarioArchivo,a.lugar as carpeta,a.observaciones,case when a.copia = '' then 'NO' else a.copia end as copia,case when u.habilitado = 1 then 'SI' else 'NO' end as usuarioActivo " +
                   "from siacor.archivados a " +
                   "inner join siacor.hojas_ruta h on h.nur = a.codigo " +
                   "inner join public.usuario u on u.codigo = a.persona " +
                   "inner join public.parametrica p on p.id_parametrica = u.oficina " +
                   "where h.nro = -1 and u.oficina = :oficina " +
                   "order by h.nur desc", 
           nativeQuery = true)
    List<ArchivadosSalidaDto> listarArchivadosReporte(Long oficina);

    Archivados findByIdArchivados(Long idArchivados);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.archivados " +
                "SET lugar=:lugar, observaciones=:observaciones, fecha_modificacion=now(), modificado_por= :modificadoPor, direccion_ip= :direccionIp, accion='update' " +
                "WHERE id_archivado= :idArchivados",
        nativeQuery = true
    )
    int actualizaDocumentoActualizado(Long idArchivados,String lugar, String observaciones, String modificadoPor, String direccionIp);
}
