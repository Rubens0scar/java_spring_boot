package siacor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import siacor.model.entity.Documentos;
import siacor.model.pojo.DocumentosEditPojo;
import siacor.model.pojo.DocumentosEditExternoPojo;
import siacor.model.pojo.DocumentosPojo;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Mapper
public interface DocumentosMapper {
    DocumentosMapper INSTANCE = Mappers.getMapper(DocumentosMapper.class);
        @Mapping(target = "codigo", source = "codigo")
        @Mapping(target = "citeOriginal", source = "citeOriginal")
        @Mapping(target = "destinatarioTitulo", source = "destinatarioTitulo")
        @Mapping(target = "destinatarioNombres", source = "destinatarioNombres")
        @Mapping(target = "destinatarioCargo", source = "destinatarioCargo")
        @Mapping(target = "destinatarioInstitucion", source = "destinatarioInstitucion")
        @Mapping(target = "remitenteNombres", source = "remitenteNombres")
        @Mapping(target = "remitenteCargo", source = "remitenteCargo")
        @Mapping(target = "remitenteInstitucion", source = "remitenteInstitucion")
        @Mapping(target = "mosca", source = "mosca")
        @Mapping(target = "referencia", source = "referencia")
        @Mapping(target = "contenido", source = "contenido")
        @Mapping(target = "fecha", source = "fecha")
        @Mapping(target = "autor", source = "autor")
        @Mapping(target = "adjuntos", source = "adjuntos")
        @Mapping(target = "copias", source = "copias")
        @Mapping(target = "nroHojas", source = "nroHojas")
        @Mapping(target = "tipoDocumento", source = "tipoDocumento")
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", source = "creadoPor")
        @Mapping(target = "fechaModificacion", ignore = true)
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", source = "direccionIp")
        @Mapping(target = "accion", source = "accion")
    Documentos InputDocumentosToDocumentos(DocumentosPojo inpDocumentos);

    DocumentosMapper INSTANCE_EDIT = Mappers.getMapper(DocumentosMapper.class);
        @Mapping(target = "codigo", ignore = true)
        @Mapping(target = "citeOriginal", ignore = true)
        @Mapping(target = "destinatarioTitulo", source = "destinatarioTitulo")
        @Mapping(target = "destinatarioNombres", source = "destinatarioNombres")
        @Mapping(target = "destinatarioCargo", source = "destinatarioCargo")
        @Mapping(target = "destinatarioInstitucion", source = "destinatarioInstitucion")
        @Mapping(target = "remitenteNombres", ignore = true)
        @Mapping(target = "remitenteCargo", ignore = true)
        @Mapping(target = "remitenteInstitucion", ignore = true)
        @Mapping(target = "mosca", ignore = true)
        @Mapping(target = "referencia", source = "referencia")
        @Mapping(target = "contenido", source = "contenido")
        @Mapping(target = "fecha", ignore = true)
        @Mapping(target = "autor", ignore = true)
        @Mapping(target = "adjuntos", source = "adjuntos")
        @Mapping(target = "copias", source = "copias")
        @Mapping(target = "nroHojas", source = "nroHojas")
        @Mapping(target = "tipoDocumento", ignore = true)
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", ignore = true)
        @Mapping(target = "fechaModificacion", expression = "java(java.time.LocalDateTime.now())")
        @Mapping(target = "modificadoPor", source = "modificadoPor")
        @Mapping(target = "direccionIp", ignore = true)
        @Mapping(target = "accion", constant = "update")
    void InputDocumentosEditToDocumentos(DocumentosEditPojo inpuDocumentosEditPojo, @MappingTarget Documentos entity);

    DocumentosMapper INSTANCE_EDIT_EXTERNO = Mappers.getMapper(DocumentosMapper.class);
        @Mapping(target = "codigo", ignore = true)
        @Mapping(target = "citeOriginal", source = "citeOriginal")
        @Mapping(target = "destinatarioTitulo", ignore = true)
        @Mapping(target = "destinatarioNombres", ignore = true)
        @Mapping(target = "destinatarioCargo", ignore = true)
        @Mapping(target = "destinatarioInstitucion", ignore = true)
        @Mapping(target = "remitenteNombres", source = "remitenteNombres")
        @Mapping(target = "remitenteCargo", source = "remitenteCargo")
        @Mapping(target = "remitenteInstitucion", source = "remitenteInstitucion")
        @Mapping(target = "mosca", ignore = true)
        @Mapping(target = "referencia", source = "referencia")
        @Mapping(target = "contenido", ignore = true)
        @Mapping(target = "fecha", source = "fecha")
        @Mapping(target = "autor", ignore = true)
        @Mapping(target = "adjuntos", ignore = true)
        @Mapping(target = "copias", ignore = true)
        @Mapping(target = "nroHojas", ignore = true)
        @Mapping(target = "tipoDocumento", ignore = true)
        @Mapping(target = "habilitado", ignore = true)
        @Mapping(target = "fechaCreacion", ignore = true)
        @Mapping(target = "creadoPor", ignore = true)
        @Mapping(target = "fechaModificacion", expression = "java(java.time.LocalDateTime.now())")
        @Mapping(target = "modificadoPor", ignore = true)
        @Mapping(target = "direccionIp", ignore = true)
        @Mapping(target = "accion", constant = "update")
    void InputDocumentosEditExternoToDocumentos(DocumentosEditExternoPojo inpuDocumentosEditExternoPojo, @MappingTarget Documentos entity);

    // 👇 Método auxiliar para que MapStruct sepa convertir
    default LocalDateTime map(OffsetDateTime fecha) {
        return fecha != null ? fecha.toLocalDateTime() : null;
    }
}
