package siacor.service;

import java.util.List;

import siacor.model.Dto.ListadoEntrantesDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.entity.Seguimientos;
import siacor.model.pojo.CabeceraSeguimientosHistoricoPojo;
import siacor.model.pojo.DatosCopiaPojo;
import siacor.model.pojo.DatosPojo;
import siacor.model.pojo.ListaNuriNoRecepcionadosPojo;
import siacor.model.pojo.SeguimientoDataPojo;
import siacor.model.pojo.SeguimientoEditPojo;
import siacor.model.pojo.SeguimientoPojo;
import siacor.model.pojo.reporteSeguimientosPojo;

public interface SeguimientoService {
    Seguimientos registrarSeguimientos(SeguimientoPojo seguimientoPojo) throws Exception;

    Seguimientos registrarSeguimientosDerivado(SeguimientoPojo seguimientoPojo) throws Exception;

    Seguimientos registrarSeguimientosDerivadoCopia(SeguimientoPojo seguimientoPojo) throws Exception;
    
    Seguimientos actualizarSeguimientos(SeguimientoEditPojo inpuEditPojo) throws Exception;

    Seguimientos eliminarSeguimientos(SeguimientoPojo inpuSeguimientoPojo) throws Exception;

    Seguimientos eliminarSeguimientosData(SeguimientoPojo inpuSeguimientoPojo) throws Exception;

    Seguimientos actualizaFechaRecepcion(SeguimientoPojo inpuSeguimientoPojo) throws Exception;

    List<ListadoEntrantesDto> listadoEntrantes(String usuario) throws Exception;

    int cantidadEntrantes(String usuario) throws Exception;
    
    List<ListadoEntrantesDto> listadoRecibidos(String usuario) throws Exception;

    int cantidadRecibidos(String usuario) throws Exception;

    CabeceraSeguimientosHistoricoPojo historicoSeguimiento(String nuri) throws Exception;

    int actualizarEstadoSeguimiento(DatosPojo inDatosPojo) throws Exception;

    int eliminarSeguimientosDerivado(Long id, String codigo, Long idPadre) throws Exception;
    
    List<SeguimientoDataPojo> listarSeguimientosData(Integer oficial, String nuri, String usuario) throws Exception;

    List<SeguimientoDataPojo> listarSeguimientosDestino(Long idSeguimiento, String nuri) throws Exception;

    List<SalidaDataDto> listarNurisPendientes(String usuario) throws Exception;

    int contarArchivados(String usuario) throws Exception;

    Seguimientos archivarDocumento(String nuri) throws Exception;

    Seguimientos archivarDocumentoCopia(DatosCopiaPojo data) throws Exception;    

    List<ListadoEntrantesDto> listadoRecibidosCopias(String usuario) throws Exception;

    int cantidadRecibidosCopias(String usuario) throws Exception;

    int cantidadEntrantesCopias(String usuario) throws Exception;

    int actualizarEstadoSeguimientoCopia(DatosPojo inDatosPojo) throws Exception;

    List<ListadoEntrantesDto> listadoDerivadosPor(String usuario) throws Exception;

    Integer restablecerDerivacion(DatosCopiaPojo data) throws Exception;

    List<ListaNuriNoRecepcionadosPojo> listaNuriNoRecepcionados(Long oficina) throws Exception;

    List<reporteSeguimientosPojo> reporteSeguimientos(String derivadoPor, String derivadoA) throws Exception;
}
