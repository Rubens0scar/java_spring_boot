package siacor.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.RecuperaCiteNuriDto;
import siacor.model.Dto.ReporteCreadosDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.entity.HojasRuta;
import siacor.model.utilitarios.HojasRutaID;

@Repository
public interface HojasRutaRepository extends JpaRepository<HojasRuta, HojasRutaID>{
    HojasRuta findByNurAndCodigo(String nur, String codigo);

    @Query(value = "select * from siacor.hojas_ruta where nur = :nur", nativeQuery = true)
    List<HojasRuta> buscarNurCodigo(String nur);

    @Query(value = "select * from siacor.hojas_ruta where nur = ?1 order by fecha_creacion asc limit 1", nativeQuery = true)
    HojasRuta buscarNur(String nur);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "INSERT INTO siacor.hojas_ruta(nur, codigo, nro, estado, fecha, usuario, proceso, habilitado, fecha_creacion, creado_por, direccion_ip, accion) " +
                "VALUES(:nur, :cite, 0, :estado, now(), :usuario, :proceso, 1, now(), :usuario, :ip, 'insert')",
        nativeQuery = true
    )
    int insertarHojaRuta(String nur,String cite, Long estado, String usuario, Long proceso, String ip);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "delete from siacor.hojas_ruta WHERE nur=:nur and codigo=:cite",
        nativeQuery = true
    )
    int eliminarSeguimientos(String nur, String cite);

    @Query(value = "select * from siacor.hojas_ruta where nur = ?1 order by fecha_creacion asc", nativeQuery = true)
    List<HojasRuta> listarCites(String nur);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "INSERT INTO siacor.hojas_ruta(nur, codigo, nro, estado, fecha, usuario, proceso, habilitado, fecha_creacion, creado_por, direccion_ip, accion) " +
                "VALUES(:nur, :cite, :nro, :estado, now(), :usuario, :proceso, 1, now(), :usuario, :ip, 'insert')",
        nativeQuery = true
    )
    int asignaNuriPendiente(String nur,String cite, Long nro, Long estado, String usuario, Long proceso, String ip);

    @Query(value = "select * from siacor.hojas_ruta hr where nur = :nur and nro = -1", nativeQuery = true)
    HojasRuta buscarPadreHoja(String nur);

    @Query(value = "select * from siacor.hojas_ruta hr where nur = :nur and codigo = :cite and nro = :nro", nativeQuery = true)
    HojasRuta buscarHojaRuta(String nur, String cite, Long nro);

    @Query(value = "select * from siacor.hojas_ruta hr where nur = :nuri order by fecha_creacion desc limit 1", nativeQuery = true)
    HojasRuta buscaRegistroUltimo(String nuri);

    @Query(value = "select * from siacor.hojas_ruta hr where nur = :nuri and codigo = :cite", nativeQuery = true)
    HojasRuta registroNurCite(String nuri,String cite);

    @Query(value = "select * from siacor.hojas_ruta hr where nro = -1 and codigo = :cite", nativeQuery = true)
    HojasRuta registroNur(String cite);

    @Query(value = "select codigo from siacor.hojas_ruta hr where nro = 0 and nur = :nuri", nativeQuery = true)
    List<SalidaDataDto> citesAdjuntos(String nuri);

    @Query(value = "select hr.codigo as cite, hr.nur as nuri from siacor.hojas_ruta hr where hr.codigo = :cite", nativeQuery = true)
    List<RecuperaCiteNuriDto> recuperaNuriCite(String cite);

    @Query(value = "select usuario from siacor.hojas_ruta hr where nro = -1 and nur = :nuri", nativeQuery = true)
    String buscaUsuarioAutor(String nuri);

    @Query(value = "select ROW_NUMBER() OVER (ORDER BY hr.nur) AS numeroFila,hr.nur,COALESCE(TO_CHAR(hr.fecha, 'DD/MM/YYYY HH24:MI:SS'), '') as fecha,hr.codigo as cite,COALESCE(TO_CHAR(d.fecha, 'MM/DD/YYYY HH24:MI:SS'), '') as fechaDocumento,d.referencia,d.destinatario_nombres as destinatario " + 
                   "from siacor.hojas_ruta hr " +
                   "inner join siacor.documentos d on d.codigo = hr.codigo " +
                   "where hr.nro = -1 and " +
                   "      ((DATE(hr.fecha) >= TO_DATE(:fechaIni, 'DD/MM/YYYY')) or :fechaIni is null) and " +
                   "      ((DATE(hr.fecha) <= TO_DATE(:fechaFin, 'DD/MM/YYYY')) or :fechaFin is null) and " +
                   "      (d.autor = :usuario or :usuario is null)" +
                   "order by hr.fecha", 
           nativeQuery = true)
    List<ReporteCreadosDto> reporteCreados(String usuario, String fechaIni, String fechaFin);

    @Query(value = "select ROW_NUMBER() OVER (ORDER BY hr.nur) AS numeroFila,hr.nur,COALESCE(TO_CHAR(hr.fecha, 'DD/MM/YYYY HH24:MI:SS'), '') as fecha,hr.codigo as cite,COALESCE(TO_CHAR(d.fecha, 'DD/MM/YYYY HH24:MI:SS'), '') as fechaDocumento,d.referencia,d.destinatario_nombres as destinatario " +
                   "from siacor.hojas_ruta hr " +
                   "inner join siacor.documentos d on d.codigo = hr.codigo " +
                   "where hr.nro = -1 and " +
	               "      ((DATE(hr.fecha) >= TO_DATE(:fechaIni, 'DD/MM/YYYY')) or :fechaIni is null) and " +
                   "      ((DATE(hr.fecha) <= TO_DATE(:fechaFin, 'DD/MM/YYYY')) or :fechaFin is null) and " +
                   "      (d.autor = :usuario or :usuario is null) and " +
                   "      hr.nur like :nuriCondicion " +
                   "order by hr.fecha;", 
           nativeQuery = true)
    List<ReporteCreadosDto> reporteCreadosNuri(String usuario, String fechaIni, String fechaFin, String nuriCondicion);

    @Query(value = "select * from siacor.hojas_ruta where nro = -1 and estado = 75 and usuario = :usuario", nativeQuery = true)
    List<HojasRuta> listadoHojasSeguimientoInternasCreadas(String usuario);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "update siacor.hojas_ruta set estado = :estado,modificado_por=:usuario,accion='update' where nro = -1 and estado = 75 and nur = :nur and usuario = :usuario",
        nativeQuery = true
    )
    int anularHojaRuta(String nur,Long estado, String usuario);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "update siacor.hojas_ruta set estado = :estado where nur = :nur",
        nativeQuery = true
    )
    int actualizaEstadosConfirmacionRestablecimiento(String nur,Long estado);

    @Query(value = "select * from siacor.hojas_ruta where nro = -1 and nur = :codigo",
           nativeQuery = true)
    HojasRuta datoHojasRuta(String codigo);

    @Query(value = "select * from siacor.hojas_ruta where nro = -1 and estado = 140 and usuario = :usuario", nativeQuery = true)
    List<HojasRuta> listadoHojasSeguimientoExternasCreadas(String usuario);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "update siacor.hojas_ruta set estado = :estado, modificado_por=:usuario,accion='update' where nro = -1 and estado = 140 and nur = :nur and usuario = :usuario",
        nativeQuery = true
    )
    int anularHojaRutaExterno(String nur,Long estado, String usuario);
    
}
