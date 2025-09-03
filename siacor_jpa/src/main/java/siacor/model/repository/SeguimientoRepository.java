package siacor.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.CabeceraSeguimientoDto;
import siacor.model.Dto.DatosUsuariosDto;
import siacor.model.Dto.ListaSeguimientosDto;
import siacor.model.Dto.ListadoEntrantesDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.Dto.SeguimientosDto;
import siacor.model.entity.Seguimientos;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimientos, Long>{
    Seguimientos findByIdSeguimiento(Long idSeguimiento);

    List<Seguimientos> findByCodigo(String codigo);

    List<Seguimientos> findByPadre(Long padre);

    List<Seguimientos> findByPadreAndOficial(Long padre, Integer oficial);

    List<Seguimientos> findByCodigoAndIdSeguimiento(String codigo, Long idSeguimiento);

    Seguimientos findByCodigoAndPadreAndOficial(String codigo, Long padre, Integer oficial);

    Seguimientos findByIdSeguimientoAndCodigo(Long idSeguimiento, String codigo);

    List<Seguimientos> findByIdSeguimientoAndCodigoAndEstadoAndOficialAndDerivadoA(Long idSeguimiento, String codigo, Long estado, Integer oficial, String usuario);

    @Query(value = "select * " +
                    "FROM siacor.seguimientos " +
                    "where id_seguimiento = :idSeguimiento", nativeQuery = true)
    List<Seguimientos> buscarIdSeguimiento(Long idSeguimiento);

    @Modifying
    @Transactional
    @Query(
        value = "delete from siacor.seguimientos WHERE id_seguimiento=:id",
        nativeQuery = true
    )
    int eliminarSeguimientos(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET fecha_recepcion=now(),modificado_por=:usuario,direccion_ip=:ip,accion='update',estado=:estado WHERE derivado_a = :usuario and oficial = :oficial and codigo = :nur and estado = 40 and id_seguimiento = :idSeguimiento",
        nativeQuery = true
    )
    int actualizarFechaRecepcionSeguimiento(String nur, Long estado, String usuario, String ip, Integer oficial, Long idSeguimiento);

    @Query(value = "SELECT s.id_seguimiento as idSeguimiento, hr.nur as nur, d.cite_original as codigo, d.referencia as referencia, " +
                    "d.remitente_nombres as remitenteNombre, d.remitente_cargo as remitenteCargo, " +
                    "s.fecha_derivacion as fechaDerivacion, s.observaciones as proveido, " +
                    "s.derivado_por as derivadoPor, s.derivado_a as derivadoA, " +
                    "p.nombre as accion, p2.nombre as proceso,s.oficial " +
                    "FROM siacor.seguimientos s " + 
                    "inner join siacor.hojas_ruta hr on s.codigo=hr.nur " +
                    "inner join siacor.documentos d on hr.codigo=d.codigo " +
                    "inner join public.parametrica p on p.id_parametrica = s.id_accion " +
                    "left join public.parametrica p2 on p2.id_parametrica = hr.proceso " +
                    "WHERE s.estado = 40 AND hr.estado in (76,141) AND hr.nro=-1 AND CAST(s.hijo AS INTEGER)<=1 AND s.derivado_a = :usuario " +
                    "ORDER BY s.fecha_derivacion DESC", nativeQuery = true)
    List<ListadoEntrantesDto> listadoEntrantes(String usuario);

    @Query(value = "SELECT s.id_seguimiento as idSeguimiento, hr.nur as nur, d.cite_original as codigo, d.referencia as referencia, " +
                    "d.remitente_nombres as remitenteNombre, d.remitente_cargo as remitenteCargo, " +
                    "s.fecha_derivacion as fechaDerivacion, s.observaciones as proveido, " +
                    "s.derivado_por as derivadoPor, s.derivado_a as derivadoA, " +
                    "p.nombre as accion, p2.nombre as proceso,s.oficial " +
                    "FROM siacor.seguimientos s " + 
                    "inner join siacor.hojas_ruta hr on s.codigo=hr.nur " +
                    "inner join siacor.documentos d on hr.codigo=d.codigo " +
                    "inner join public.parametrica p on p.id_parametrica = s.id_accion " +
                    "left join public.parametrica p2 on p2.id_parametrica = hr.proceso " +
                    "WHERE s.estado = 40 AND hr.estado in (76,141) AND hr.nro=-1 AND CAST(s.hijo AS INTEGER)<=1 AND s.derivado_por = :usuario " +
                    "ORDER BY s.fecha_derivacion DESC", nativeQuery = true)
    List<ListadoEntrantesDto> listadoDerivadosPor(String usuario);

    @Query(value = "SELECT count(*) " +
                   "FROM siacor.seguimientos, siacor.hojas_ruta " +
                   "WHERE seguimientos.estado = 40 AND hojas_ruta.estado in (76,141) AND seguimientos.codigo=hojas_ruta.nur AND hojas_ruta.nro=-1 AND seguimientos.oficial=1 AND CAST(seguimientos.hijo AS INTEGER)<=1 AND " +
                   "seguimientos.derivado_a = :usuario", nativeQuery = true)
    int cantidadEntrantes(String usuario);

    @Query(value = "SELECT s.id_seguimiento as idSeguimiento,hr.nur,d.cite_original as codigo,d.referencia,d.remitente_nombres as remitenteNombre,d.remitente_cargo as remitenteCargo,s.fecha_derivacion as fechaDerivacion,s.observaciones as proveido,s.derivado_por as derivadoPor,s.derivado_a as derivadoA, p.nombre as accion,p2.nombre as proceso,hr.creado_por as creadoPor,s.oficial " +
                    "FROM siacor.seguimientos s " +
                    "inner join siacor.hojas_ruta hr on s.codigo=hr.nur " +
                    "inner join siacor.documentos d on hr.codigo=d.codigo " +
                    "inner join public.parametrica p on p.id_parametrica = s.id_accion " +
                    "left join public.parametrica p2 on p2.id_parametrica = hr.proceso " +
                    "WHERE (s.estado = 41 or s.estado = 0) AND hr.estado in (76,141) AND hr.nro=-1 AND CAST(s.hijo AS INTEGER)<=1 AND s.derivado_a = :usuario " +
                    "ORDER BY s.fecha_recepcion DESC", nativeQuery = true)
    List<ListadoEntrantesDto> listadoRecibidos(String usuario);

    @Query(value = "SELECT count(*) " +
                    "FROM siacor.seguimientos, siacor.hojas_ruta " +
                    "WHERE (seguimientos.estado = 41 or seguimientos.estado = 0) AND hojas_ruta.estado in (76,141) AND seguimientos.codigo=hojas_ruta.nur AND hojas_ruta.nro=-1 AND seguimientos.oficial=1 AND CAST(seguimientos.hijo AS INTEGER)<=1 AND " +
                    "seguimientos.derivado_a = :usuario", nativeQuery = true)
    int cantidadRecibidos(String usuario);

    @Query(value = "select * " +
                    "FROM siacor.seguimientos " +
                    "where id_seguimiento = (SELECT max(id_seguimiento) FROM siacor.seguimientos where codigo = :nuri and oficial = :oficial)", nativeQuery = true)
    Seguimientos buscarPadre(String nuri,Integer oficial);

    @Query(value = "select s.id_seguimiento as idSeguimiento, p.codigo || ' ' || u.nombre_completo as derivadoPor, p1.codigo || ' ' || u1.nombre_completo as derivadoA, " +
                    "TO_CHAR(s.fecha_derivacion, 'DD/Mon/YYYY HH24:MI') as fechaDespacho, " +
                    "TO_CHAR(s.fecha_recepcion, 'DD/Mon/YYYY HH24:MI') as fechaRecepcion, " +
                    "p2.nombre as accion,p3.nombre as estado,s.observaciones as proveido " +
                   "FROM siacor.seguimientos s " +
                   "inner join public.usuario u on u.codigo = s.derivado_por " +
                   "inner join public.parametrica p on p.id_parametrica = u.oficina " +
                   "inner join public.usuario u1 on u1.codigo = s.derivado_a " +
                   "inner join public.parametrica p1 on p1.id_parametrica = u1.oficina " +
                   "inner join public.parametrica p2 on p2.id_parametrica = s.id_accion " +
                   "inner join public.parametrica p3 on p3.id_parametrica = s.estado " +
                   "left join (select  hr.nro,string_to_array(string_agg(NULLIF(hr.codigo, ''), ';'), ';') AS cite from siacor.hojas_ruta hr where hr.nur = :nuri group by hr.nro) hr on hr.nro = s.id_seguimiento " +
                   "where s.codigo = :nuri order by s.id_seguimiento,s.padre", 
           nativeQuery = true)
    List<ListaSeguimientosDto> ListaSeguimientos(String nuri);

    @Query(value = "select s.id_seguimiento as idSeguimiento, p.codigo || ' ' || u.nombre_completo as derivadoPor, p1.codigo || ' ' || u1.nombre_completo as derivadoA,TO_CHAR(s.fecha_derivacion, 'DD/Mon/YYYY HH24:MI') as fechaDespacho,TO_CHAR(s.fecha_recepcion, 'DD/Mon/YYYY HH24:MI') as fechaRecepcion,p2.nombre as accion,p3.nombre as estado,s.observaciones as proveido,s.oficial,da.cite1,da.cite2,da.cite3,da.cite4,da.cite5,da.cite6,da.cite7,da.cite8,da.cite9,da.cite10,da.cite11,da.cite12 " +
                   "FROM siacor.seguimientos s " +
                   "inner join public.usuario u on u.codigo = s.derivado_por " +
                   "inner join public.parametrica p on p.id_parametrica = u.oficina " +
                   "inner join public.usuario u1 on u1.codigo = s.derivado_a " +
                   "inner join public.parametrica p1 on p1.id_parametrica = u1.oficina " +
                   "inner join public.parametrica p2 on p2.id_parametrica = s.id_accion " +
                   "inner join public.parametrica p3 on p3.id_parametrica = s.estado " +
                   "left join (select  hr.nro,string_to_array(string_agg(NULLIF(hr.codigo, ''), ';'), ';') AS cite from siacor.hojas_ruta hr where hr.nur = :nuri group by hr.nro) hr on hr.nro = s.id_seguimiento " +
                   "left join (WITH numeradas AS (select hr.nur,hr.nro,hr.codigo,ROW_NUMBER() OVER (PARTITION BY hr.nro ORDER BY hr.codigo) AS rn FROM siacor.hojas_ruta hr where hr.nur = :nuri) " +
                                "select nur,nro,MAX(codigo) FILTER (WHERE rn = 1) AS cite1,MAX(codigo) FILTER (WHERE rn = 2) AS cite2,MAX(codigo) FILTER (WHERE rn = 3) AS cite3,MAX(codigo) FILTER (WHERE rn = 4) AS cite4,MAX(codigo) FILTER (WHERE rn = 5) AS cite5,MAX(codigo) FILTER (WHERE rn = 6) AS cite6,MAX(codigo) FILTER (WHERE rn = 7) AS cite7,MAX(codigo) FILTER (WHERE rn = 8) AS cite8,MAX(codigo) FILTER (WHERE rn = 9) AS cite9,MAX(codigo) FILTER (WHERE rn = 10) AS cite10,MAX(codigo) FILTER (WHERE rn = 11) AS cite11,MAX(codigo) FILTER (WHERE rn = 12) AS cite12 " +
                                "FROM numeradas " +
                                "GROUP BY nur,nro " +
                                "ORDER BY nro) da on da.nro = s.id_seguimiento " +
                   "where s.codigo = :nuri order by s.id_seguimiento,s.padre", 
           nativeQuery = true)
    List<ListaSeguimientosDto> ListaSeguimientoLinea(String nuri);

    @Query(value = "SELECT hr.nur,d.codigo as cite,d.referencia,d.remitente_nombres as remitenteNombre,d.remitente_cargo as remitenteCargo,s.oficial, " +
                           "s.observaciones as proveido,s.derivado_por as derivadoPor,s.derivado_a as derivadoA, p.nombre as accion,p2.nombre as proceso, " +
                           "d.destinatario_cargo as destinatarioCargo, d.destinatario_nombres as destinatarioNombre,d.adjuntos,p3.nombre as tipoDocumento,s.fecha_creacion as fechaCreacion " +
                   "FROM siacor.seguimientos s " +
                   "inner join siacor.hojas_ruta hr on s.codigo=hr.nur " +
                   "inner join siacor.documentos d on hr.codigo=d.codigo " +
                   "inner join public.parametrica p on p.id_parametrica = s.id_accion " +
                   "left join public.parametrica p2 on p2.id_parametrica = hr.proceso " +
                   "inner join public.parametrica p3 on p3.id_parametrica = d.tipo_documento " +
                   "WHERE s.padre = 0 and hr.nro = -1 and s.codigo = :nuri", 
           nativeQuery = true)
    List<CabeceraSeguimientoDto> CabeceraSeguimiento(String nuri);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET estado=:estado WHERE codigo = :nur and id_seguimiento=:idSeguimiento",
        nativeQuery = true
    )
    int actualizarEstadoSeguimiento(Long idSeguimiento, String nur, Long estado);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET estado=:estado,oficial=1 WHERE codigo = :nur and id_seguimiento=:idSeguimiento",
        nativeQuery = true
    )
    int actualizarEstadoSeguimientoReestablecer(Long idSeguimiento, String nur, Long estado);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "delete from siacor.seguimientos WHERE id_seguimiento=:id and codigo = :codigo and padre <> 0",
        nativeQuery = true
    )
    int eliminarSeguimientosDerivado(Long id, String codigo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET estado=:estado,oficial=:oficial WHERE id_seguimiento=:idSeguimiento",
        nativeQuery = true
    )
    int actualizarPadre(Long idSeguimiento, Integer oficial, Long estado);

    @Query(value = "select * " +
                    "FROM siacor.seguimientos " +
                    "where codigo = :nuri and oficial = 1 and estado = 40", nativeQuery = true)
    Seguimientos listarSeguimientos(String nuri);

    @Query(value = "select * " +
                    "FROM siacor.seguimientos " +
                    "where codigo = :nuri and derivado_por = :usuario and oficial in (:oficial) and estado = 40", nativeQuery = true)
    List<Seguimientos> listarSeguimientosData(Integer oficial, String nuri, String usuario);

    @Query(value = "SELECT * FROM siacor.seguimientos where codigo = :nuri and padre = :idSeguimiento order by 1 asc", 
           nativeQuery = true)
    List<Seguimientos> listarSeguimientosDestino(Long idSeguimiento, String nuri);

    @Query(value = "SELECT hr.nur as codigo " + 
                   "FROM siacor.seguimientos s " +
                   "inner join siacor.hojas_ruta hr on s.codigo=hr.nur " +
                   "inner join siacor.documentos d on hr.codigo=d.codigo " +
                   "inner join public.parametrica p on p.id_parametrica = s.id_accion " +
                   "left join public.parametrica p2 on p2.id_parametrica = hr.proceso " +
                   "WHERE s.estado = 41 AND hr.estado in (76,141) AND hr.nro=-1 AND CAST(s.hijo AS INTEGER)<=1 AND s.derivado_a = :usuario " +
                   "ORDER BY hr.nur DESC", 
           nativeQuery = true)
    List<SalidaDataDto> listarNurisPendientes(String usuario);

    @Query(value = "select * " +
                    "FROM siacor.seguimientos " +
                    "where codigo = :nuri and oficial = 1 and estado = 41", nativeQuery = true)
    Seguimientos buscarUltimoSeguimiento(String nuri);

    @Query(value = "select count(distinct(codigo)) FROM siacor.seguimientos where derivado_a = :usuario and estado = 62", nativeQuery = true)
    int contarArchivados(String usuario);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET estado=62,fecha_modificacion=now(),accion='update' WHERE codigo=:nuri and estado = 41",
        nativeQuery = true
    )
    int archivarDocumento(String nuri);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET estado=66,fecha_modificacion=now(),accion='update' WHERE id_seguimiento = :id and  codigo=:nuri and estado = 65",
        nativeQuery = true
    )
    int archivarDocumentoCopia(String nuri,Long id);

    @Query(value = "select * FROM siacor.seguimientos where codigo = :nuri and estado = :estado", nativeQuery = true)
    Seguimientos buscarSeguimientos(String nuri, Long estado);

    @Query(value = "select * FROM siacor.seguimientos where id_seguimiento = :id and codigo = :nuri and estado = :estado", nativeQuery = true)
    Seguimientos buscarSeguimientosId(String nuri, Long estado, Long id);

    @Query(value = "select codigo from siacor.hojas_ruta where nro = :id", nativeQuery = true)
    List<SalidaDataDto> buscarCiteData(Long id);

    @Query(value = "SELECT s.id_seguimiento as idSeguimiento, hr.nur,d.codigo,d.referencia,d.remitente_nombres as remitenteNombre,d.remitente_cargo as remitenteCargo,s.fecha_derivacion as fechaDerivacion,s.observaciones as proveido,s.derivado_por as derivadoPor,s.derivado_a as derivadoA, p.nombre as accion,p2.nombre as proceso,hr.creado_por as creadoPor,s.oficial " +
                   "FROM siacor.seguimientos s " +
                   "inner join siacor.hojas_ruta hr on s.codigo=hr.nur " +
                   "inner join siacor.documentos d on hr.codigo=d.codigo " +
                   "inner join public.parametrica p on p.id_parametrica = s.id_accion " +
                   "left join public.parametrica p2 on p2.id_parametrica = hr.proceso " +
                   "WHERE s.estado = 65 AND hr.estado in (76,141) AND hr.nro=-1 AND s.oficial = 0 AND s.derivado_a = :usuario " +
                   "ORDER BY hr.nur DESC", nativeQuery = true)
    List<ListadoEntrantesDto> listadoRecibidosCopias(String usuario);

    @Query(value = "SELECT count(*) " +
                   "FROM siacor.seguimientos, siacor.hojas_ruta " +
                   "WHERE seguimientos.estado = 65 AND hojas_ruta.estado in (76,141) AND seguimientos.codigo=hojas_ruta.nur AND hojas_ruta.nro=-1 AND seguimientos.oficial=0 AND " +
                   "seguimientos.derivado_a = :usuario", nativeQuery = true)
    int cantidadRecibidosCopias(String usuario);

    @Query(value = "SELECT count(*) " +
                   "FROM siacor.seguimientos, siacor.hojas_ruta " +
                   "WHERE seguimientos.estado = 40 AND hojas_ruta.estado in (76,141) AND seguimientos.codigo=hojas_ruta.nur AND hojas_ruta.nro=-1 AND seguimientos.oficial=0 AND " +
                         "seguimientos.derivado_a = :usuario", nativeQuery = true)
    int cantidadEntrantesCopias(String usuario);

    @Query(value = "select * " +
                   "from siacor.seguimientos s " +
                   "where (s.estado=41 or s.estado=65) and s.id_seguimiento not in (select id_seguimiento from siacor.agrupacion) and s.hijo='0' " +
                   "      and s.codigo != :nuri and s.derivado_a=:usuario " +
                   "order by codigo asc", 
           nativeQuery = true)
    List<Seguimientos> listaAgrupacion(String nuri, String usuario);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET hijo=:nuriPadre,estado=:estado,fecha_modificacion=now(),accion='update' WHERE id_seguimiento = :idSeguimiento",
        nativeQuery = true
    )
    int actualizaSeguimientoAgrupado(Long idSeguimiento, String nuriPadre, Long estado);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.seguimientos SET hijo='0',estado=:estado,fecha_modificacion=now(),accion='update' WHERE id_seguimiento = :idSeguimiento",
        nativeQuery = true
    )
    int actualizaEstadoAgrupacion(Long idSeguimiento, Long estado);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "delete from siacor.seguimientos where codigo = :nuri and derivado_por = :usuario and estado = 40 and padre = :idPadre",
        nativeQuery = true
    )
    int eliminaCopiasSeguimientosReestablecimiento(String nuri, String usuario, Long idPadre);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "delete from siacor.seguimientos where codigo = :nuri and derivado_por = :usuario and estado = 40",
        nativeQuery = true
    )
    int eliminaCopiasSeguimientosRestablecimientoSinPadre(String nuri, String usuario);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "update siacor.seguimientos set estado = :estado,oficial = :oficial where id_seguimiento = :id",
        nativeQuery = true
    )
    int actualizaEstadosConfirmacionRestablecimientoId(Long id,Long estado, Integer oficial);

    @Query(value = "select * from siacor.seguimientos where codigo = :nuri and derivado_por = :usuario and estado = 40", nativeQuery = true)
    List<Seguimientos> listaSeguimientos(String nuri, String usuario);

    @Query(value = "select distinct s.codigo " +
                   "from siacor.seguimientos s " +
                   "inner join public.usuario u on u.codigo = s.derivado_por " +
                   "where s.estado = 40 and u.oficina = :oficina " +
                   "order by codigo desc",
           nativeQuery = true)
    List<SalidaDataDto> listaNuriNoRecepcionados(Long oficina);

    @Query(value = "SELECT id_seguimiento as idSeguimiento, codigo, derivado_por as derivadoPor, derivado_a as derivadoA, fecha_derivacion as fechaDerivacion, fecha_recepcion as fechaRecepcion, estado, oficial " +
                   "FROM siacor.seguimientos where codigo = :codigo order by id_seguimiento asc",
           nativeQuery = true)
    List<SeguimientosDto> listaDatosSeguimiento(String codigo);

    @Query(value = "select p.nombre " +
                   "from public.parametrica p " +
                   "inner join siacor.seguimientos s on s.estado = p.id_parametrica " +
                   "where s.id_seguimiento = (select max(id_seguimiento) from siacor.seguimientos where oficial <> 0 and codigo = :codigo)",
           nativeQuery = true)
    String estadoDatosSeguimiento(String codigo);

    @Query(value = "select distinct codigo from siacor.seguimientos where oficial <> 0 and padre = 0 and derivado_a = :derivadoA and derivado_por = :derivadoPor order by codigo",
           nativeQuery = true)
    List<SalidaDataDto> listaNurisUsuario(String derivadoPor, String derivadoA);

    @Query(value = "select h.nur,d.cite_original as cite,s.observaciones, (select nombre_completo from public.usuario where codigo = s.derivado_por) as remitente,(select nombre_completo from public.usuario where codigo = s.derivado_a) as destinatario " +
                   "from siacor.hojas_ruta h " +
                   "inner join siacor.documentos d on d.codigo = h.codigo " +
                   "inner join siacor.seguimientos s on s.codigo = h.nur " +
                   "where s.padre = 0 and s.oficial <> 0 and h.nro = -1 and h.nur = :codigo",
           nativeQuery = true)
    DatosUsuariosDto datosReporteUsuario(String codigo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "INSERT INTO siacor.seguimientos(codigo, derivado_por, derivado_a, fecha_derivacion, fecha_recepcion, estado, id_accion, observaciones, padre, oficial, hijo, escusa, habilitado, fecha_creacion, creado_por, direccion_ip, accion) " +
                "VALUES(:codigo, :derivadoPor, :derivadoA, now(), null, :estado, :idAccion, :observaciones, :padre, :oficial, :hijo, :escusa, 1, now(), :creadoPor, :direccionIp, 'insert')",
        nativeQuery = true
    )
    int registraSeguimiento(String codigo, String derivadoPor, String derivadoA, Long estado, Long idAccion, String observaciones, Long padre, Integer oficial, Long hijo, String escusa, String creadoPor, String direccionIp);


}
