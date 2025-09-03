package siacor.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.DatosHojaRutaDto;
import siacor.model.Dto.DocumentosDto;
import siacor.model.Dto.ListaDocumentosDto;
import siacor.model.Dto.ReporteBuscadorDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.Documentos;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentosRepository extends JpaRepository<Documentos, String> {
    Documentos findByCodigo(String codigo);

    @Query(value = "select codigo, autor from siacor.documentos where autor like ?1", nativeQuery = true)
    List<ListaDocumentosDto> listaDocumentos(String usuario);

    @Query(value = "select u.codigo, u.nombre_completo as nombreCompleto,u.cargo,u.mosca " +
                   "from siacor.vias v " +
                   "inner join public.usuario u on u.codigo = v.codigo_usuario " +
                   "where v.habilitado = 1 and v.codigo = ?1", nativeQuery = true)
    List<buscaDatosDto> listaDocumentosVias(String cite);

    @Query(value = "select d.remitente_institucion as procedencia,d.remitente_nombres||'-'||d.remitente_cargo as remitente,d.cite_original as cite,d.fecha,d.referencia,d.adjuntos,d.nrohojas, COALESCE(p.nombre,'') as proceso,'' as plazo,s.observaciones as proveido,d.destinatario_nombres||'-'||d.destinatario_cargo destinatario,s.fecha_derivacion fechaDerivacion,s.id_accion as idAccion,d.tipo_documento as tipoDocumento " +
                   "from siacor.documentos d " +
                   "inner join public.usuario u on u.codigo = d.autor " +
                   "inner join siacor.hojas_ruta h on h.codigo = d.codigo " +
                   "left join public.parametrica p on p.id_parametrica = h.proceso " +
                   "inner join siacor.seguimientos s on s.codigo = h.nur " +
                   "where h.nro = -1 and s.padre = 0 and s.oficial = 1 and s.codigo = :nur", 
           nativeQuery = true)
    DatosHojaRutaDto datosHojaRuta(String nur);

    @Query(value = "select d.remitente_institucion as procedencia,d.remitente_nombres||'-'||d.remitente_cargo as remitente,d.cite_original as cite,d.fecha,d.referencia,d.adjuntos,d.nrohojas, COALESCE(p.nombre,'') as proceso,'' as plazo,s.observaciones as proveido,d.destinatario_nombres||'-'||d.destinatario_cargo destinatario,s.fecha_derivacion fechaDerivacion,s.id_accion as idAccion,d.tipo_documento as tipoDocumento " +
                   "from siacor.documentos d " +
                   "inner join public.usuario u on u.codigo = d.autor " +
                   "inner join siacor.hojas_ruta h on h.codigo = d.codigo " +
                   "left join public.parametrica p on p.id_parametrica = h.proceso " +
                   "inner join siacor.seguimientos s on s.codigo = h.nur " +
                   "where h.nro = -1 and s.padre = 0 and s.codigo = :nur", 
           nativeQuery = true)
    DatosHojaRutaDto datosHojaRutaReimprsion(String nur);

    @Query(value = "SELECT d.* FROM siacor.documentos d inner join public.parametrica p on p.id_parametrica = d.tipo_documento WHERE d.autor = :usuario and p.codigo = :codigo", nativeQuery = true)
    List<Object[]> listaDatosDocumentos(String usuario, String codigo);

    @Query(value = "select distinct(d.codigo) " +
                   "from siacor.documentos d "+
                   "inner join siacor.hojas_ruta hr on hr.codigo = d.codigo " +
                   "inner join siacor.seguimientos s on s.codigo = hr.nur " +
                   "where hr.estado IN (75,140) and d.codigo = :cite", 
           nativeQuery = true)
    List<SalidaDataDto> documentoCompletoNoConfirmado(String cite);

    @Query(value = "select distinct(d.codigo) " +
                   "from siacor.documentos d "+
                   "inner join siacor.hojas_ruta hr on hr.codigo = d.codigo " +
                   "inner join siacor.seguimientos s on s.codigo = hr.nur " +
                   "where (hr.estado <> 75 OR hr.estado <> 140) and d.codigo = :cite", 
           nativeQuery = true)
    List<SalidaDataDto> documentoCompleto(String cite);

    @Query(value = "select distinct(d.codigo) " +
                   "from siacor.documentos d " +
                   "inner join siacor.hojas_ruta hr on hr.codigo = d.codigo " +
                   "where hr.nur not in (select distinct(s.codigo) from siacor.seguimientos s) " +
                        "and d.codigo = :cite", 
           nativeQuery = true)
    List<SalidaDataDto> documentoCiteNuri(String cite);

    @Query(value = "select distinct(d.codigo) " +
                   "from siacor.documentos d " +
                   "where d.codigo not in (select distinct(d.codigo) from siacor.documentos d inner join siacor.hojas_ruta hr on hr.codigo = d.codigo inner join siacor.seguimientos s on s.codigo = hr.nur union all select distinct(d.codigo) from siacor.documentos d inner join siacor.hojas_ruta hr on hr.codigo = d.codigo where hr.nur not in (select distinct(s.codigo) from siacor.seguimientos s)) " +
                         "and d.codigo = :cite", 
           nativeQuery = true)
    List<SalidaDataDto> documentoSolo(String cite);

    @Query(value = "SELECT d.* FROM siacor.documentos d inner join public.parametrica p on p.id_parametrica = d.tipo_documento WHERE d.autor = :usuario", nativeQuery = true)
    List<Object[]> listaDatosDocumentosTodo(String usuario);

    @Query(value = "SELECT h.nur, d.cite_original AS cite, p.nombre AS tipoDocumento,d.referencia, d.remitente_nombres AS remitente, d.destinatario_nombres AS destinatario " +
                   "FROM siacor.documentos d " +
                   "INNER JOIN siacor.hojas_ruta h ON h.codigo = d.codigo " +
                   "INNER JOIN public.parametrica p ON p.id_parametrica = d.tipo_documento " +
                   "WHERE (:cite IS NULL OR d.codigo ILIKE '%' || :cite || '%') AND " +
                   "      (:adjuntos IS NULL OR d.adjuntos ILIKE '%' || :adjuntos || '%') AND " +
                   "      (:referencia IS NULL OR d.referencia ILIKE '%' || :referencia || '%') AND " +
                   "      (:remitente IS NULL OR d.remitente_nombres ILIKE '%' || :remitente || '%') AND " +
                   "      (:remCargo IS NULL OR d.remitente_cargo ILIKE '%' || :remCargo || '%') AND " +
                   "      (:remInstitucion IS NULL OR d.remitente_institucion ILIKE '%' || :remInstitucion || '%') AND " +
                   "      (:destinatario IS NULL OR d.destinatario_nombres ILIKE '%' || :destinatario || '%') AND " +
                   "      (:desCargo IS NULL OR d.destinatario_cargo ILIKE '%' || :desCargo || '%') AND " +
                   "      (:desInstitucion IS NULL OR d.destinatario_institucion ILIKE '%' || :desInstitucion || '%') AND " +
                   "      (:nur IS NULL OR h.nur ILIKE '%' || :nur || '%') AND " +
                   "      (:usuario IS NULL OR d.autor ILIKE '%' || :usuario || '%') AND " +
                   "      (date(d.fecha) BETWEEN :fechaIni AND :fechaFin) " +
                   "ORDER BY h.nur",
       nativeQuery = true)
    List<ReporteBuscadorDto> reporteBuscador(
        @Param("usuario") String usuario,
        @Param("cite") String cite,
        @Param("nur") String nur,
        @Param("adjuntos") String adjuntos,
        @Param("referencia") String referencia,
        @Param("remitente") String remitente,
        @Param("remCargo") String remCargo,
        @Param("remInstitucion") String remInstitucion,
        @Param("destinatario") String destinatario,
        @Param("desCargo") String desCargo,
        @Param("desInstitucion") String desInstitucion,
        @Param("fechaIni") LocalDate fechaIni,
        @Param("fechaFin") LocalDate fechaFin
    );


    @Query(value = "select TO_CHAR(date(min(fecha_creacion)), 'DD/MM/YYYY') as fecha from siacor.documentos", 
           nativeQuery = true)
    String fechaPrimerDocumento();

    @Query(value = "select h.nur,d.cite_original as cite,d.autor,public.formatear_fecha(d.fecha_creacion,'2') as fechaCreacion,d.referencia,d.remitente_nombres as remitente,d.destinatario_nombres as destinatario " +
                   "from siacor.documentos d " +
                   "inner join siacor.hojas_ruta h on h.codigo = d.codigo " +
                   "inner join public.usuario u on u.codigo = h.usuario " +
                   "where d.tipo_documento = :tipo and u.oficina = :oficina " +
                   "order by cite_original desc", 
           nativeQuery = true)
    List<DocumentosDto> listarDocumentos(Long tipo,Long oficina);

    @Query(value = "select h.nur,d.cite_original as cite,d.autor,public.formatear_fecha(d.fecha_creacion,'2') as fechaCreacion,d.referencia,d.remitente_nombres as remitente,d.destinatario_nombres as destinatario " +
                   "from siacor.documentos d " +
                   "inner join siacor.hojas_ruta h on h.codigo = d.codigo " +
                   "inner join public.usuario u on u.codigo = h.usuario " +
                   "where d.tipo_documento = :tipo and u.oficina = :oficina " +
                   "order by cite_original desc limit 1", 
           nativeQuery = true)
    DocumentosDto ultimoDocumento(Long tipo,Long oficina);

    @Query(value = "select h.nur,d.cite_original as cite,d.autor,public.formatear_fecha(d.fecha_creacion,'2') as fechaCreacion,d.referencia,d.remitente_nombres as remitente,d.destinatario_nombres as destinatario " +
                   "from siacor.documentos d " +
                   "inner join siacor.hojas_ruta h on h.codigo = d.codigo " +
                   "inner join public.usuario u on u.codigo = h.usuario " +
                   "where h.nur = :nuri " +
                   "order by cite_original desc", 
           nativeQuery = true)
    List<DocumentosDto> listarDocumentosNur(String nuri);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "INSERT INTO siacor.vias(codigo_usuario, codigo, habilitado, fecha_creacion, creado_por, direccion_ip, accion) VALUES('', :cite, 0, now(), :creador, :direccionip, 'insert')",
        nativeQuery = true
    )
    int registraVias(String cite,String creador, String direccionip);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE siacor.documentos SET destinatario_titulo=:desTitulo, destinatario_nombres=:desNombre, destinatario_cargo=:desCargo, destinatario_institucion=:destInstitucion, referencia=:referencia, contenido=:contenido, adjuntos=:adjuntos, copias=:copias, nrohojas=:nroHojas, fecha_modificacion=now(), modificado_por=:modificadoPor, direccion_ip=:direccionIp, accion='update' WHERE codigo=:cite",
        nativeQuery = true
    )
    int modificaDocumento(String cite, String desTitulo, String desNombre, String desCargo, String destInstitucion, String referencia, String contenido, String adjuntos, String copias, Integer nroHojas, String modificadoPor, String direccionIp);
}