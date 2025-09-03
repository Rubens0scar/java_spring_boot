package siacor.service;

import siacor.model.Dto.DatosHojaRutaDto;
import siacor.model.Dto.DocumentosDto;
import siacor.model.Dto.ListaDocumentosDto;
import siacor.model.Dto.ReporteBuscadorDto;
import siacor.model.entity.Documentos;
import siacor.model.pojo.BuscadorDataPojo;
import siacor.model.pojo.DataDocumentosPojo;
import siacor.model.pojo.DatosDocumentoPojo;
import siacor.model.pojo.DocumentosEditExternoPojo;
import siacor.model.pojo.DocumentosPojo;

import java.util.List;

public interface DocumentosService {

    Documentos registrarDocumentos(DocumentosPojo inputDocumentos) throws Exception;

    //Documentos actualizarDocumentos(DocumentosEditPojo inpuDocumentosEditPojo, String cite) throws Exception;

    Documentos actualizarDocumentos(DocumentosPojo inpuDocumentosEditPojo, String cite) throws Exception;

    List<ListaDocumentosDto> listaDocumentos(String usuario) throws Exception;

    int actulizarHabilitados(String codigo);

    DatosDocumentoPojo datosDocumentos(String nuri, String cite) throws Exception;

    DatosHojaRutaDto datosHojaRuta(String nur) throws Exception;

    DatosHojaRutaDto datosHojaRutaReimprsion(String nur) throws Exception;

    DatosDocumentoPojo datosDocumentosNuri(String cite) throws Exception;

    List<DataDocumentosPojo> listaDatosDocumentos(String usuario, String codigo) throws Exception;

    int documentoValidar(String cite) throws Exception;

    List<DataDocumentosPojo> listaDatosDocumentosTodo(String usuario) throws Exception;

    DatosDocumentoPojo datosDocumentosCite(String cite) throws Exception;

    DatosDocumentoPojo datosDocumentosCiteConNuri(String cite) throws Exception;

    List<ReporteBuscadorDto> reporteBuscador(BuscadorDataPojo pojo) throws Exception;

    String fechaPrimerDocumento() throws Exception;

    List<DocumentosDto> listarDocumentos(Long tipo,Long oficina) throws Exception;

    DocumentosDto ultimoDocumento(Long tipo,Long oficina) throws Exception;

    Documentos actualizarDocumentosExterno(DocumentosEditExternoPojo inpuDocumentosEditExternoPojo, String cite) throws Exception;

    DatosDocumentoPojo datosDocumentosExternos(String nur, String cite) throws Exception;
    
}
