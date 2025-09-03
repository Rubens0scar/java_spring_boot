package siacor.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import siacor.model.Dto.CabeceraSeguimientoDto;
import siacor.model.Dto.DatosUsuariosDto;
import siacor.model.Dto.DocumentosDto;
import siacor.model.Dto.ListaSeguimientosDto;
import siacor.model.Dto.ListadoEntrantesDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.Dto.SeguimientosDto;
import siacor.model.entity.HojasRuta;
import siacor.model.entity.Parametrica;
import siacor.model.entity.Seguimientos;
import siacor.model.mapper.SeguimientoMapper;
import siacor.model.pojo.CabeceraSeguimientoPojo;
import siacor.model.pojo.CabeceraSeguimientosHistoricoPojo;
import siacor.model.pojo.DatosCopiaPojo;
import siacor.model.pojo.DatosPojo;
import siacor.model.pojo.DesplosarFechasPojo;
import siacor.model.pojo.ListaNuriNoRecepcionadosPojo;
import siacor.model.pojo.ListaSeguimientosPojo;
import siacor.model.pojo.ResultadoSeguimientoPojo;
import siacor.model.pojo.SeguimientoDataPojo;
import siacor.model.pojo.SeguimientoEditPojo;
import siacor.model.pojo.SeguimientoPojo;
import siacor.model.pojo.reporteSeguimientosPojo;
import siacor.model.repository.DocumentosRepository;
import siacor.model.repository.HojasRutaRepository;
import siacor.model.repository.ParametricaRepository;
import siacor.model.repository.SeguimientoRepository;
import siacor.utilitarios.FechaUtils;

@Service
@Transactional
public class SeguimientoServiceImpl implements SeguimientoService {
    @Autowired
    private final SeguimientoRepository seguimientoRepository;

    @Autowired
    private final ParametricaRepository parametricaRepository;

    @Autowired
    private final HojasRutaRepository hojasRutaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private final DocumentosRepository documentosRepository;

    public SeguimientoServiceImpl(SeguimientoRepository seguimientoRepository,ParametricaRepository parametricaRepository, HojasRutaRepository hojasRutaRepository,DocumentosRepository documentosRepository){
        this.seguimientoRepository = seguimientoRepository;
        this.parametricaRepository = parametricaRepository;
        this.hojasRutaRepository = hojasRutaRepository;
        this.documentosRepository = documentosRepository;
    }

    @Override
    public Seguimientos registrarSeguimientos(SeguimientoPojo seguimientoPojo) throws Exception {
        try {
            int res = seguimientoRepository.registraSeguimiento(seguimientoPojo.getCodigo(), seguimientoPojo.getDerivadoPor(), seguimientoPojo.getDerivadoA(), seguimientoPojo.getEstado(), seguimientoPojo.getIdAccion(), seguimientoPojo.getObservaciones(), seguimientoPojo.getPadre(), seguimientoPojo.getOficial(), seguimientoPojo.getHijo(), seguimientoPojo.getEscusa(), seguimientoPojo.getCreadoPor(), seguimientoPojo.getDireccionIp());

            if(res == 1) return seguimientoRepository.listarSeguimientos(seguimientoPojo.getCodigo());
            throw new Exception("Error al registrar el Seguimiento.");

        } catch (Exception e) {
            throw new Exception("Error al registrar el Seguimiento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public Seguimientos registrarSeguimientosDerivado(SeguimientoPojo seguimientoPojo) throws Exception {
        try {
            Seguimientos segPadre = seguimientoRepository.buscarPadre(seguimientoPojo.getCodigo(),1);

            seguimientoPojo.setPadre(segPadre.getIdSeguimiento());
            seguimientoPojo.setOficial(1);

            int res = seguimientoRepository.registraSeguimiento(seguimientoPojo.getCodigo(), seguimientoPojo.getDerivadoPor(), seguimientoPojo.getDerivadoA(), seguimientoPojo.getEstado(), seguimientoPojo.getIdAccion(), seguimientoPojo.getObservaciones(), seguimientoPojo.getPadre(), seguimientoPojo.getOficial(), seguimientoPojo.getHijo(), seguimientoPojo.getEscusa(), seguimientoPojo.getCreadoPor(), seguimientoPojo.getDireccionIp());

            SeguimientoEditPojo datos = new SeguimientoEditPojo();
            
            datos.setDerivadoA(segPadre.getDerivadoA());
            datos.setDerivadoPor(segPadre.getDerivadoPor());
            datos.setFechaDerivacion(segPadre.getFechaDerivacion());
            datos.setFechaRecepcion(segPadre.getFechaRecepcion());
            datos.setEstado(segPadre.getEstado());
            datos.setIdAccion(segPadre.getIdAccion());
            datos.setObservaciones(segPadre.getObservaciones());
            datos.setPadre(segPadre.getPadre());
            datos.setOficial(2);
            datos.setHijo(0L);
            datos.setEscusa(segPadre.getEscusa());
            datos.setModificadoPor(seguimientoPojo.getCreadoPor());
            datos.setDireccionIp(seguimientoPojo.getDireccionIp());            

            SeguimientoMapper.INSTANCE_EDIT.InputSeguimientoEditToSeguimiento(datos, segPadre);
            seguimientoRepository.save(segPadre);

            if(res == 1) return seguimientoRepository.listarSeguimientos(seguimientoPojo.getCodigo());
            throw new Exception("Error al registrar el Seguimiento al derivar.");

        } catch (Exception e) {
            throw new Exception("Error al registrar el Seguimiento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public Seguimientos registrarSeguimientosDerivadoCopia(SeguimientoPojo seguimientoPojo) throws Exception {
        try {
            seguimientoPojo.setPadre(seguimientoPojo.getIdSeguimiento());
            seguimientoPojo.setOficial(0);
            
            Seguimientos seguimientos = SeguimientoMapper.INSTANCE.InputSeguimientotoSeguimiento(seguimientoPojo);
            seguimientoRepository.save(seguimientos);

            // Seguimientos segPadre = seguimientoRepository.buscarPadre(seguimientoPojo.getCodigo(),0);

            // seguimientoPojo.setPadre(segPadre.getIdSeguimiento());
            // seguimientoPojo.setOficial(0);
            
            // Seguimientos seguimientos = SeguimientoMapper.INSTANCE.InputSeguimientotoSeguimiento(seguimientoPojo);
            // seguimientoRepository.save(seguimientos);

            // SeguimientoEditPojo datos = new SeguimientoEditPojo();
            
            // datos.setDerivadoA(segPadre.getDerivadoA());
            // datos.setDerivadoPor(segPadre.getDerivadoPor());
            // datos.setFechaDerivacion(segPadre.getFechaDerivacion());
            // datos.setFechaRecepcion(segPadre.getFechaRecepcion());
            // datos.setEstado(segPadre.getEstado());
            // datos.setIdAccion(segPadre.getIdAccion());
            // datos.setObservaciones(segPadre.getObservaciones());
            // datos.setPadre(segPadre.getPadre());
            // datos.setOficial(0);
            // datos.setHijo(0L);
            // datos.setEscusa(segPadre.getEscusa());
            // datos.setModificadoPor(seguimientoPojo.getCreadoPor());
            // datos.setDireccionIp(seguimientoPojo.getDireccionIp());            

            // SeguimientoMapper.INSTANCE_EDIT.InputSeguimientoEditToSeguimiento(datos, segPadre);
            // seguimientoRepository.save(segPadre);

            return seguimientos;

        } catch (Exception e) {
            throw new Exception("Error al registrar el Seguimiento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Seguimientos actualizarSeguimientos(SeguimientoEditPojo inpuEditPojo) throws Exception {
        try {
            Seguimientos seg = seguimientoRepository.findByIdSeguimiento(inpuEditPojo.getIdSeguimiento());
            if (seg == null) throw new Exception("Seguimiento no encontrado, por favor revisar los datos.");

            SeguimientoMapper.INSTANCE_EDIT.InputSeguimientoEditToSeguimiento(inpuEditPojo, seg);
            return seguimientoRepository.save(seg);

        } catch (Exception e) {
            throw new Exception("Error al modificar el Seguimiento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Seguimientos eliminarSeguimientos(SeguimientoPojo inpuSeguimientoPojo) throws Exception{
        try {
            int res = 0;

            Seguimientos seg =null;

            List<Seguimientos> listaSeg = seguimientoRepository.findByCodigo(inpuSeguimientoPojo.getCodigo());

            System.out.println(objectMapper.writeValueAsString(listaSeg));

            if(listaSeg.size() == 1){
                if(listaSeg.get(0).getPadre() == 0 && listaSeg.get(0).getEstado() == 40 && listaSeg.get(0).getOficial() == 1){
                    System.out.println("Elimina Padre");
                    res = seguimientoRepository.eliminarSeguimientos(listaSeg.get(0).getIdSeguimiento());

                    seg = listaSeg.get(0);
                }
            }else{
                seg = seguimientoRepository.listarSeguimientos(inpuSeguimientoPojo.getCodigo());
                System.out.println("Elimina Hijos. Padre: " + seg.getPadre());
                res = this.eliminarSeguimientosDerivado(seg.getIdSeguimiento(), inpuSeguimientoPojo.getCodigo(), seg.getPadre());
            }

            if(res != 0) return seg;
            else throw new Exception("Seguimiento a eliminar tuvo un problema, por favor vuelva a intentar.");

        } catch (Exception e) {
            throw new Exception("Error al eliminar el Seguimiento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Seguimientos eliminarSeguimientosData(SeguimientoPojo inpuSeguimientoPojo) throws Exception{
        try {
            int res = 0;

            Seguimientos seg =null;

            List<Seguimientos> listaSeg = seguimientoRepository.findByCodigo(inpuSeguimientoPojo.getCodigo());

            //System.out.println(objectMapper.writeValueAsString(listaSeg));
            System.out.println("SIZE: " + listaSeg.size());

            if(listaSeg.size() == 1){
                //if(listaSeg.get(0).getPadre() == 0 && listaSeg.get(0).getEstado() == 40 && siacor.seguimientos){
                    System.out.println("Elimina Padre");
                    res = seguimientoRepository.eliminarSeguimientos(listaSeg.get(0).getIdSeguimiento());

                    seg = listaSeg.get(0);
                //}
            }else{
                seg = seguimientoRepository.findByIdSeguimiento(inpuSeguimientoPojo.getIdSeguimiento());
                System.out.println("Elimina Hijos. Padre: " + seg.getPadre());
                if(seg.getPadre() == 0) res = seguimientoRepository.eliminarSeguimientos(inpuSeguimientoPojo.getIdSeguimiento());
                else res = this.eliminarSeguimientosDerivado(seg.getIdSeguimiento(), inpuSeguimientoPojo.getCodigo(), seg.getPadre());
            }

            if(res != 0) return seg;
            else throw new Exception("Seguimiento a eliminar tuvo un problema, por favor vuelva a intentar.");

        } catch (Exception e) {
            throw new Exception("Error al eliminar el Seguimiento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Seguimientos actualizaFechaRecepcion(SeguimientoPojo inpuSeguimientoPojo) throws Exception {
        try {
            //List<Seguimientos> seg = seguimientoRepository.findByIdSeguimientoAndCodigoAndEstadoAndOficialAndDerivadoA(inpuSeguimientoPojo.getIdSeguimiento(), inpuSeguimientoPojo.getCodigo(),40L,inpuSeguimientoPojo.getOficial(),inpuSeguimientoPojo.getUsuario());
            List<Seguimientos> seg = seguimientoRepository.buscarIdSeguimiento(inpuSeguimientoPojo.getIdSeguimiento());
            if (seg.size() != 1) throw new Exception("Seguimiento a actualizar tiene varios registros, por favor revisar los datos.");

            int res = seguimientoRepository.actualizarFechaRecepcionSeguimiento(inpuSeguimientoPojo.getCodigo(), inpuSeguimientoPojo.getEstado(), inpuSeguimientoPojo.getUsuario(),inpuSeguimientoPojo.getDireccionIp(),inpuSeguimientoPojo.getOficial(),inpuSeguimientoPojo.getIdSeguimiento());

            Long id = seg.get(0).getIdSeguimiento();

            if(res == 1) {
                seguimientoRepository.flush();
                Seguimientos resultado = seguimientoRepository.findByIdSeguimiento(id);
                System.out.println("ID: " + resultado.getFechaRecepcion());
                return resultado;
            }
            else throw new Exception("Seguimiento que se tiene que Actualizar tuvo un problema, por favor vuelva a intentar.");
            
        } catch (Exception e) {
            throw new Exception("Error al actualizar la fecha el Seguimiento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<ListadoEntrantesDto> listadoEntrantes(String usuario) throws Exception {
        try {
            return seguimientoRepository.listadoEntrantes(usuario);

        } catch (Exception e) {
            throw new Exception("Error al listar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public int cantidadEntrantes(String usuario) throws Exception {
        try {
            return seguimientoRepository.cantidadEntrantes(usuario);

        } catch (Exception e) {
            throw new Exception("Error al encontrar la cantidad de entrantes, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<ListadoEntrantesDto> listadoRecibidos(String usuario) throws Exception {
        try {
            return seguimientoRepository.listadoRecibidos(usuario);

        } catch (Exception e) {
            throw new Exception("Error al listar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public int cantidadRecibidos(String usuario) throws Exception {
        try {
            return seguimientoRepository.cantidadRecibidos(usuario);

        } catch (Exception e) {
            throw new Exception("Error al encontrar la cantidad de entrantes, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public CabeceraSeguimientosHistoricoPojo historicoSeguimiento(String nuri) throws Exception {
        try {
            System.out.println("INGRESO AL IMPL");
            List<CabeceraSeguimientoDto> seguimientoCabecera = seguimientoRepository.CabeceraSeguimiento(nuri);
            if(seguimientoCabecera == null) throw new Exception("Se tuvo un error al buscar los datos del NUR, por favor intente nuevamente");

            List<ListaSeguimientosDto> listaSeguimientos = seguimientoRepository.ListaSeguimientoLinea(nuri);
            if(listaSeguimientos == null) throw new Exception("No se pudo listar el seguimiento del NUR ingresado.");

            List<ListaSeguimientosPojo> segPojo = listaSeguimientos.stream().
                map(dto -> {
                    ListaSeguimientosPojo pojo = new ListaSeguimientosPojo();
                    BeanUtils.copyProperties(dto, pojo);
                    //pojo.setCites(seguimientoRepository.buscarCiteData(dto.getIdSeguimiento()));
                    return pojo;
                }).collect(Collectors.toList());

            List<SalidaDataDto> cites = hojasRutaRepository.citesAdjuntos(nuri);

            // Copiar los datos al DTO concreto
            List<CabeceraSeguimientoPojo> cabeceraSeguimiento = seguimientoCabecera.stream().
                map(cabeceras -> {
                    CabeceraSeguimientoPojo res = new CabeceraSeguimientoPojo();
                    BeanUtils.copyProperties(cabeceras, res);
                    return res;
                }).collect(Collectors.toList());

            CabeceraSeguimientosHistoricoPojo response = new CabeceraSeguimientosHistoricoPojo();
            response.setCabecera(cabeceraSeguimiento);
            response.setListaSeguimiento(segPojo);
            response.setCitesAdjuntos(cites);

            return response;

        } catch (Exception e) {
            throw new Exception("Error al encontrar el historico, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public int actualizarEstadoSeguimiento(DatosPojo inDatosPojo) throws Exception {
        try {
            int res = seguimientoRepository.actualizarEstadoSeguimiento(inDatosPojo.getId(), inDatosPojo.getNuri(), 44L);
            return res;

        } catch (Exception e) {
            throw new Exception("Error al encontrar la cantidad de entrantes, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public int eliminarSeguimientosDerivado(Long id, String codigo, Long idPadre) throws Exception {
        try {
            System.out.println("Ingresa a eliminarSeguimientosDerivado.");

            int res = seguimientoRepository.eliminarSeguimientosDerivado(id, codigo);

            if(res == 1) {
                System.out.println("Antes de actualizar al Padre. " + idPadre);
                int resultado = seguimientoRepository.actualizarPadre(idPadre, 1, 41L);
                res=res+resultado;
                System.out.println("Res: " + res);
                return res;
            } else throw new Exception("No se pudo eliminar, por favor intente nuevamente.");

        } catch (Exception e) {
            throw new Exception("Error al eliminar seguimientos hijos, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<SeguimientoDataPojo> listarSeguimientosData(Integer oficial, String nuri, String usuario) throws Exception {
        try {
            List<Seguimientos> seg = seguimientoRepository.listarSeguimientosData(oficial, nuri, usuario);
            if(seg == null){
                
            }else{
                for (Seguimientos entity : seg) {
                    Parametrica par = parametricaRepository.datosParametrica(entity.getIdAccion());
                    entity.setAccion(par.getNombre());
                }
            }

            List<SeguimientoDataPojo> listaPojo = new ArrayList<>();
            for (Seguimientos entity : seg) {
                SeguimientoDataPojo pojo = new SeguimientoDataPojo();
                BeanUtils.copyProperties(entity, pojo);
                listaPojo.add(pojo);
            }
            return listaPojo;

        } catch (Exception e) {
            throw new Exception("Error al listar el ultimo registro, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<SeguimientoDataPojo> listarSeguimientosDestino(Long idSeguimiento, String nuri) throws Exception {
        try {
            List<Seguimientos> seg = seguimientoRepository.listarSeguimientosDestino(idSeguimiento,nuri);
            if(seg == null){
                
            }else{
                for (Seguimientos entity : seg) {
                    Parametrica par = parametricaRepository.datosParametrica(entity.getIdAccion());
                    entity.setAccion(par.getNombre());
                }
            }

            List<SeguimientoDataPojo> listaPojo = new ArrayList<>();
            for (Seguimientos entity : seg) {
                SeguimientoDataPojo pojo = new SeguimientoDataPojo();
                BeanUtils.copyProperties(entity, pojo);
                listaPojo.add(pojo);
            }
            return listaPojo;

        } catch (Exception e) {
            throw new Exception("Error al listar el ultimo registro destino, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<SalidaDataDto> listarNurisPendientes(String usuario) throws Exception {
        try {
            return seguimientoRepository.listarNurisPendientes(usuario);

        } catch (Exception e) {
            throw new Exception("Error al listar los nuris pendientes, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public int contarArchivados(String usuario) throws Exception {
        try {
            return seguimientoRepository.contarArchivados(usuario);

        } catch (Exception e) {
            throw new Exception("Error al contar los documentos archivados, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Seguimientos archivarDocumento(String nuri) throws Exception {
        try {
            Seguimientos seg = seguimientoRepository.buscarSeguimientos(nuri,41L);
            if(seg == null ) throw new Exception("Error al buscar, el nuri ingresado se encuentra en otro estado." );

            int res = seguimientoRepository.archivarDocumento(nuri);
            if(res == 1) return seguimientoRepository.buscarSeguimientos(nuri,62L);
            throw new Exception("Error al archivar el documento." );

        } catch (Exception e) {
            throw new Exception("Error al archivar el documento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Seguimientos archivarDocumentoCopia(DatosCopiaPojo data) throws Exception {
        try {
            Seguimientos seg = seguimientoRepository.buscarSeguimientosId(data.getNuri(),65L,data.getIdSeguimiento());
            if(seg == null ) throw new Exception("Error al buscar, el nuri ingresado se encuentra en otro estado." );

            int res = seguimientoRepository.archivarDocumentoCopia(data.getNuri(),data.getIdSeguimiento());
            if(res == 1) return seguimientoRepository.buscarSeguimientosId(data.getNuri(),66L,data.getIdSeguimiento());
            throw new Exception("Error al archivar copia del documento." );

        } catch (Exception e) {
            throw new Exception("Error al archivar la copia del documento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<ListadoEntrantesDto> listadoRecibidosCopias(String usuario) throws Exception {
        try {
            return seguimientoRepository.listadoRecibidosCopias(usuario);

        } catch (Exception e) {
            throw new Exception("Error al encontrar la cantidad de recibidos de las copias, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public int cantidadRecibidosCopias(String usuario) throws Exception {
        try {
            return seguimientoRepository.cantidadRecibidosCopias(usuario);

        } catch (Exception e) {
            throw new Exception("Error al encontrar la cantidad de recibidos de las copias, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public int cantidadEntrantesCopias(String usuario) throws Exception {
        try {
            return seguimientoRepository.cantidadEntrantesCopias(usuario);

        } catch (Exception e) {
            throw new Exception("Error al encontrar la cantidad de entrantes de las copias, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public int actualizarEstadoSeguimientoCopia(DatosPojo inDatosPojo) throws Exception {
        try {
            int res = seguimientoRepository.actualizarEstadoSeguimiento(inDatosPojo.getId(), inDatosPojo.getNuri(), 67L);
            return res;

        } catch (Exception e) {
            throw new Exception("Error al actualizar los estados, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<ListadoEntrantesDto> listadoDerivadosPor(String usuario) throws Exception {
        try {
            return seguimientoRepository.listadoDerivadosPor(usuario);

        } catch (Exception e) {
            throw new Exception("Error al listar datos, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Integer restablecerDerivacion(DatosCopiaPojo data) throws Exception {
        try {
            Seguimientos s = seguimientoRepository.findByIdSeguimiento(data.getIdSeguimiento());
            int res = 0;
            if(s.getCodigo().contains("I/")){
                if(s.getOficial() == 0){
                    if(s.getPadre() != 0) {
                        List<Seguimientos> lista = seguimientoRepository.findByPadre(s.getPadre());
                        if(lista.size() == 1) res = res + seguimientoRepository.actualizaEstadosConfirmacionRestablecimientoId(s.getPadre(), s.getOficial()==1?41L:65L,lista.get(0).getOficial()==2?1:0);
                    }
                    res = res + seguimientoRepository.eliminarSeguimientos(data.getIdSeguimiento());
                }else {
                    if(s.getPadre() != 0){
                        List<Seguimientos> lista = seguimientoRepository.findByPadre(s.getPadre());
                        if(lista.size() == 1){
                            //res = res + hojasRutaRepository.actualizaEstadosConfirmacionRestablecimiento(data.getNuri(),75L);
                            res = res + seguimientoRepository.actualizaEstadosConfirmacionRestablecimientoId(s.getPadre(), s.getOficial()==1?41L:65L,1);
                            res = res + seguimientoRepository.eliminarSeguimientos(data.getIdSeguimiento());
                        }else {
                            res = res + seguimientoRepository.actualizaEstadosConfirmacionRestablecimientoId(s.getPadre(), s.getOficial()==1?41L:65L,1);
                            res = res + seguimientoRepository.eliminaCopiasSeguimientosReestablecimiento(s.getCodigo(),s.getDerivadoPor(),s.getPadre());
                        }
                    }else{
                        res = res + seguimientoRepository.eliminaCopiasSeguimientosRestablecimientoSinPadre(s.getCodigo(),s.getDerivadoPor());
                        List<Seguimientos> datos = seguimientoRepository.findByCodigo(data.getNuri());

                        if(datos.size() == 0) {
                            res = res + hojasRutaRepository.actualizaEstadosConfirmacionRestablecimiento(data.getNuri(),75L);
                        } 

                    }
                }
            }else{
                if(s.getOficial() == 0){
                    if(s.getPadre() != 0) {
                        List<Seguimientos> lista = seguimientoRepository.findByPadre(s.getPadre());
                        if(lista.size() == 1) res = res + seguimientoRepository.actualizaEstadosConfirmacionRestablecimientoId(s.getPadre(), s.getOficial()==1?41L:65L,lista.get(0).getOficial()==2?1:0);
                    }
                    res = res + seguimientoRepository.eliminarSeguimientos(data.getIdSeguimiento());
                }else {
                    if(s.getPadre() != 0){
                        List<Seguimientos> lista = seguimientoRepository.findByPadre(s.getPadre());
                        if(lista.size() == 1){
                            //res = res + hojasRutaRepository.actualizaEstadosConfirmacionRestablecimiento(data.getNuri(),75L);
                            res = res + seguimientoRepository.actualizaEstadosConfirmacionRestablecimientoId(s.getPadre(), s.getOficial()==1?41L:65L,1);
                            res = res + seguimientoRepository.eliminarSeguimientos(data.getIdSeguimiento());
                        }else {
                            res = res + seguimientoRepository.actualizaEstadosConfirmacionRestablecimientoId(s.getPadre(), s.getOficial()==1?41L:65L,1);
                            res = res + seguimientoRepository.eliminaCopiasSeguimientosReestablecimiento(s.getCodigo(),s.getDerivadoPor(),s.getPadre());
                        }
                    }else{
                        res = res + seguimientoRepository.eliminaCopiasSeguimientosRestablecimientoSinPadre(s.getCodigo(),s.getDerivadoPor());
                        List<Seguimientos> datos = seguimientoRepository.findByCodigo(data.getNuri());

                        if(datos.size() == 0) {
                            res = res + hojasRutaRepository.actualizaEstadosConfirmacionRestablecimiento(data.getNuri(),140L);
                        } 

                    }
                }
            }
            return res;
        } catch (Exception e) {
            throw new Exception("Error al restablecer la derivacion, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<ListaNuriNoRecepcionadosPojo> listaNuriNoRecepcionados(Long oficina) throws Exception{
        try {
            List<SalidaDataDto> lista = seguimientoRepository.listaNuriNoRecepcionados(oficina);

            List<ListaNuriNoRecepcionadosPojo> data = new ArrayList<>();

            for (SalidaDataDto nuri : lista) {
                List<DocumentosDto> documentosNur = documentosRepository.listarDocumentosNur(nuri.getCodigo());
                ListaNuriNoRecepcionadosPojo pojo = new ListaNuriNoRecepcionadosPojo();
                pojo.setCodigo(nuri.getCodigo());
                pojo.setListarDocumentosNur(documentosNur);

                data.add(pojo);
            }

            return data;

        } catch (Exception e) {
            throw new Exception("Error al listar datos, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<reporteSeguimientosPojo> reporteSeguimientos(String derivadoPor, String derivadoA) throws Exception {
        List<SalidaDataDto> nuris = seguimientoRepository.listaNurisUsuario(derivadoPor, derivadoA);

        List<reporteSeguimientosPojo> resultado = new ArrayList<>();

        Long i = 1L;
        for (SalidaDataDto codigo : nuris) {
            DatosUsuariosDto datosusuario = seguimientoRepository.datosReporteUsuario(codigo.getCodigo());
            ResultadoSeguimientoPojo s = this.listarSeguimientos(codigo.getCodigo());
            List<String> seguimiento = s.getSeguimiento();
            String permanencia = s.getPermanencia();

            seguimiento.add(seguimientoRepository.estadoDatosSeguimiento(codigo.getCodigo()));
            reporteSeguimientosPojo pojo = new reporteSeguimientosPojo();
            pojo.setNro(i);
            pojo.setDatosusuario(datosusuario);
            pojo.setSeguimiento(seguimiento);
            pojo.setPermanencia(permanencia);

            resultado.add(pojo);
            i=i+1;
        }

        return resultado;

    }

    private ResultadoSeguimientoPojo listarSeguimientos(String codigo){
        List<String> res = new ArrayList<>();
        List<DesplosarFechasPojo> permanencia = new ArrayList<>();

        List<SeguimientosDto> seg = seguimientoRepository.listaDatosSeguimiento(codigo);
        HojasRuta hr = hojasRutaRepository.datoHojasRuta(codigo);

        LocalDateTime fecha_inicio = hr.getFecha();
        LocalDateTime fecha_final = null;

        String datoRetencion = "";
        String datoDespacho = "";

        for (int i = 0; i < seg.size(); i++) {
            fecha_final = seg.get(i).getFechaDerivacion();

            datoRetencion = FechaUtils.calcularDiferencia(fecha_inicio, fecha_final);

            fecha_inicio = fecha_final;
            fecha_final = seg.get(i).getFechaRecepcion();

            datoDespacho = FechaUtils.calcularDiferencia(fecha_inicio, fecha_final);

            List<DesplosarFechasPojo> d = FechaUtils.datosDiferencia(fecha_inicio, fecha_final);

            if (d != null && !d.isEmpty()) {
                permanencia.add(new DesplosarFechasPojo(d.get(0).getDias(), d.get(0).getHoras(), d.get(0).getMinutos()));
            }

            String oficial = "";
            if(seg.get(i).getOficial()==0) oficial = "COPIA";
            else oficial = "OFICIAL";

            String dato = seg.get(i).getDerivadoPor() + " > " + seg.get(i).getDerivadoA() + " (Retencion: " + datoRetencion + " -- Despacho: " + datoDespacho + ") " + oficial;

            fecha_inicio = fecha_final;

            res.add(dato);
        }

        String permanecer = FechaUtils.totalDias(permanencia);

        return new ResultadoSeguimientoPojo(res, permanecer);
    }

        
    
}
