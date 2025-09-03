package siacor.service;

import java.time.LocalDate;
import java.util.List;

//import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import siacor.model.Dto.RecuperaCiteNuriDto;
import siacor.model.Dto.ReporteCreadosDto;
import siacor.model.entity.Correlativos;
import siacor.model.entity.CorrelativosAsignados;
import siacor.model.entity.HojasRuta;
import siacor.model.entity.Seguimientos;
import siacor.model.entity.Usuarios;
import siacor.model.mapper.CorrelativosAsignadosMapper;
import siacor.model.mapper.CorrelativosMapper;
import siacor.model.mapper.HojasRutaMapper;
import siacor.model.pojo.CorrelativosPojo;
import siacor.model.pojo.HojasRutaEditPojo;
import siacor.model.pojo.HojasRutaPojo;
import siacor.model.repository.CorrelativosAsignadosRepository;
import siacor.model.repository.CorrelativosRepository;
import siacor.model.repository.HojasRutaRepository;
import siacor.model.repository.SeguimientoRepository;
import siacor.model.repository.UsuariosRepository;

@Service
@Transactional
public class HojasRutaServiceImpl implements HojasRutaService {
    @Autowired
    private final HojasRutaRepository hojasRutaRepository;

    @Autowired
    private final CorrelativosRepository correlativosRepository;

    @Autowired
    private final CorrelativosAsignadosRepository correlativosAsignadosRepository;

    @Autowired
    private final UsuariosRepository usuariosRepository;

    @Autowired
    private final SeguimientoRepository seguimientoRepository;

    public HojasRutaServiceImpl(HojasRutaRepository hojasRutaRepository, CorrelativosRepository correlativosRepository, CorrelativosAsignadosRepository correlativosAsignadosRepository, UsuariosRepository usuariosRepository, SeguimientoRepository seguimientoRepository){
        this.hojasRutaRepository = hojasRutaRepository;
        this.correlativosRepository = correlativosRepository;
        this.correlativosAsignadosRepository = correlativosAsignadosRepository;
        this.usuariosRepository = usuariosRepository;
        this.seguimientoRepository = seguimientoRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public HojasRuta generarHojaRuta(HojasRutaPojo inputHojasRutaPojo) throws Exception {
        try {
            Usuarios usr = usuariosRepository.findByCodigo(inputHojasRutaPojo.getUsuario());           
            HojasRuta hojasRuta = new HojasRuta();
            HojasRuta hojaResultado = null;
            if(usr.getPaginaInicio() == 84){
                Correlativos correlativo = correlativosRepository.findByCodigo("I");
                Integer nroCorrelativo = correlativo.getCorrelativo()+1;
                String nuri = correlativo.getCodigo() + "/" + correlativo.getGestion() + "-" +  String.format("%05d",nroCorrelativo);
                System.out.println("NURI: " + nuri);
                hojasRuta.setNur(nuri);
                hojasRuta.setCodigo(inputHojasRutaPojo.getCodigo());
                //hojasRuta.setNro(inputHojasRutaPojo.getNro());
                hojasRuta.setEstado(inputHojasRutaPojo.getEstado());
                hojasRuta.setUsuario(inputHojasRutaPojo.getUsuario());
                hojasRuta.setProceso(inputHojasRutaPojo.getProceso());
                hojasRuta.setCreadoPor(inputHojasRutaPojo.getUsuario());
                hojasRuta.setDireccionIp(inputHojasRutaPojo.getDireccionIp());
                hojaResultado = HojasRutaMapper.INSTANCE.InputHojasRutaToHojasRuta(hojasRuta);
                hojasRutaRepository.save(hojaResultado);

                //actualizamos el numero de correlativo
                CorrelativosPojo inputCorrelativos = new CorrelativosPojo();
                inputCorrelativos.setCodigo(correlativo.getCodigo());
                inputCorrelativos.setCorrelativo(nroCorrelativo);
                inputCorrelativos.setGestion(correlativo.getGestion());
                inputCorrelativos.setOficina(correlativo.getOficina());
                inputCorrelativos.setHabilitado(correlativo.getHabilitado());
                inputCorrelativos.setModificadoPor(inputHojasRutaPojo.getUsuario());
                inputCorrelativos.setAccion(correlativo.getAccion());
                CorrelativosMapper.INSTANCE_EDIT.InputCorrelativosToCorrelativos(inputCorrelativos, correlativo);
                correlativosRepository.save(correlativo);

                //registramos la asignacion del nuri creado con el usuario que creo
                CorrelativosAsignados inputAsignado = new CorrelativosAsignados();
                inputAsignado.setCodigo(nuri);
                inputAsignado.setPersona(inputHojasRutaPojo.getUsuario());
                CorrelativosAsignados correlativosAsignados = CorrelativosAsignadosMapper.INSTANCE.InputCorrelativosToCorrelativos(inputAsignado);
                correlativosAsignadosRepository.save(correlativosAsignados);
            } else if(usr.getPaginaInicio() == 83){
                Correlativos correlativo = correlativosRepository.findByCodigo("E");
                Integer nroCorrelativo = correlativo.getCorrelativo()+1;
                String nur =/* correlativo.getCodigo() + "/" +*/ correlativo.getGestion() + "-" +  String.format("%05d",nroCorrelativo);
                System.out.println("NUR: " + nur);
                hojasRuta.setNur(nur);
                hojasRuta.setCodigo(inputHojasRutaPojo.getCodigo());
                //hojasRuta.setNro(inputHojasRutaPojo.getNro());
                hojasRuta.setEstado(inputHojasRutaPojo.getEstado());
                hojasRuta.setUsuario(inputHojasRutaPojo.getUsuario());
                hojasRuta.setProceso(inputHojasRutaPojo.getProceso());
                hojasRuta.setCreadoPor(inputHojasRutaPojo.getUsuario());
                hojasRuta.setDireccionIp(inputHojasRutaPojo.getDireccionIp());
                hojaResultado = HojasRutaMapper.INSTANCE.InputHojasRutaToHojasRuta(hojasRuta);
                hojasRutaRepository.save(hojaResultado);

                //actualizamos el numero de correlativo
                CorrelativosPojo inputCorrelativos = new CorrelativosPojo();
                inputCorrelativos.setCodigo(correlativo.getCodigo());
                inputCorrelativos.setCorrelativo(nroCorrelativo);
                inputCorrelativos.setGestion(correlativo.getGestion());
                inputCorrelativos.setOficina(correlativo.getOficina());
                inputCorrelativos.setHabilitado(correlativo.getHabilitado());
                inputCorrelativos.setModificadoPor(inputHojasRutaPojo.getUsuario());
                inputCorrelativos.setAccion(correlativo.getAccion());
                CorrelativosMapper.INSTANCE_EDIT.InputCorrelativosToCorrelativos(inputCorrelativos, correlativo);
                correlativosRepository.save(correlativo);

                //registramos la asignacion del nuri creado con el usuario que creo
                CorrelativosAsignados inputAsignado = new CorrelativosAsignados();
                inputAsignado.setCodigo(nur);
                inputAsignado.setPersona(inputHojasRutaPojo.getUsuario());
                CorrelativosAsignados correlativosAsignados = CorrelativosAsignadosMapper.INSTANCE.InputCorrelativosToCorrelativos(inputAsignado);
                correlativosAsignadosRepository.save(correlativosAsignados);
            }
            return hojaResultado;
        }catch(Exception e){
            throw new Exception("Error al generar el NURI o NUR, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public HojasRuta actualizaHojasRuta(HojasRutaEditPojo inEditPojo) throws Exception {
        try {
            List<HojasRuta> hoja = hojasRutaRepository.buscarNurCodigo(inEditPojo.getNur());
            if (hoja.size() == 0) throw new Exception("Hoja de Ruta no encontrado, por favor revisar los datos.");

            // Long estado = 0L;
            // if(inEditPojo.getNur().contains("I/")) estado = 76L;
            // else estado = 141L;

            int res = hojasRutaRepository.actualizaEstadosConfirmacionRestablecimiento(inEditPojo.getNur(), inEditPojo.getEstado());
            
            if(res >= 1) return hojasRutaRepository.findByNurAndCodigo(inEditPojo.getNur(), inEditPojo.getCodigo());
            else throw new Exception("Error al modificar la Hoja de Ruta.");

        } catch (BadCredentialsException e) {
            throw new Exception("Error al modificar la Hoja de Ruta, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public HojasRuta guardarHojaRuta(HojasRutaPojo inputHojasRutaPojo) throws Exception {
        try {
            Usuarios usr = usuariosRepository.findByCodigo(inputHojasRutaPojo.getUsuario());
            if(usr.getPaginaInicio() == 84){
                HojasRuta hj = hojasRutaRepository.buscarNur(inputHojasRutaPojo.getNur());

                int resultado = hojasRutaRepository.insertarHojaRuta(inputHojasRutaPojo.getNur(),inputHojasRutaPojo.getCodigo(),hj.getEstado(),inputHojasRutaPojo.getUsuario(),hj.getProceso(),inputHojasRutaPojo.getDireccionIp());
                if(resultado == 1) return hojasRutaRepository.findByNurAndCodigo(inputHojasRutaPojo.getNur(),inputHojasRutaPojo.getCodigo());
                throw new Exception("Error al registrar, vuelva a intentar nuevamente, no se genero ninguna operacion.");    
            } else throw new Exception("Error al registrar datos, no se genero ninguna operacion.");
        }catch(Exception e){
            throw new Exception("Error al registra, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public HojasRuta eliminaHojaRuta(HojasRutaPojo inputRutaPojo) throws Exception {
        try {
            HojasRuta hj = hojasRutaRepository.findByNurAndCodigo(inputRutaPojo.getNur(), inputRutaPojo.getCodigo());
            if(hj == null ) throw new Exception("Error al eliminar datos, no se genero ninguna operacion.");

            int res = hojasRutaRepository.eliminarSeguimientos(inputRutaPojo.getNur(),inputRutaPojo.getCodigo());
            if(res == 1)  return hj;
            else throw new Exception("Error al eliminar datos, vuelva a intentar, no se genero ninguna operacion.");
        }catch(Exception e){
            throw new Exception("Error al eliminar, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    public List<HojasRuta> listarCites(String nur) throws Exception {
        try {
            return hojasRutaRepository.listarCites(nur);
        }catch(Exception e){
            throw new Exception("Error al eliminar, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public HojasRuta asignaNuriPendiente(HojasRutaPojo inHojasRutaPojo) throws Exception {
        try {
            Seguimientos seg = seguimientoRepository.buscarUltimoSeguimiento(inHojasRutaPojo.getNur());
            if(seg == null ) throw new Exception("Error buscar el ultimo registro del seguimiento, no se genero ninguna operacion.");

            HojasRuta hr = hojasRutaRepository.buscarPadreHoja(inHojasRutaPojo.getNur());
            if(hr == null ) throw new Exception("Error buscar el registro padre de la hoja de ruta, no se genero ninguna operacion.");

            int res = hojasRutaRepository.asignaNuriPendiente(inHojasRutaPojo.getNur(), inHojasRutaPojo.getCodigo(), seg.getIdSeguimiento(), hr.getEstado(), inHojasRutaPojo.getUsuario(), hr.getProceso(), inHojasRutaPojo.getDireccionIp());

            System.out.println("Registrado: " + res);
            return hojasRutaRepository.buscarHojaRuta(inHojasRutaPojo.getNur(), inHojasRutaPojo.getCodigo(), seg.getIdSeguimiento());
        }catch(Exception e){
            throw new Exception("Error al adjuntar un NURI pendiente, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    public HojasRuta buscaRegistroUltimo(String nuri) throws Exception {
        try {
            return hojasRutaRepository.buscaRegistroUltimo(nuri);
        }catch(Exception e){
            throw new Exception("Error al eliminar, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    public List<RecuperaCiteNuriDto> recuperaNuriCite(String cite) throws Exception {
        try {
            return hojasRutaRepository.recuperaNuriCite(cite);
        }catch(Exception e){
            throw new Exception("Error al eliminar, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    public List<ReporteCreadosDto> reporteCreadosNuri(String usuario, Integer opcionNuri, String fechaIni, String fechaFin) throws Exception {
        try {
            LocalDate fechaActual = null;
            String nuriCondicion = "";
            if(opcionNuri == null || opcionNuri == 0){
                return hojasRutaRepository.reporteCreados(usuario, fechaIni, fechaFin);
            }else if(opcionNuri == 1 ){
                fechaActual = LocalDate.now();
                nuriCondicion = nuriCondicion + "I/" + fechaActual.getYear() + "%";
                System.out.println("CONDICION: " +nuriCondicion);
                return hojasRutaRepository.reporteCreadosNuri(usuario, fechaIni, fechaFin, nuriCondicion);
            } else {
                fechaActual = LocalDate.now();
                nuriCondicion = nuriCondicion + "" + fechaActual.getYear() + "%";
                System.out.println("CONDICION: " +nuriCondicion);
                return hojasRutaRepository.reporteCreadosNuri(usuario, fechaIni, fechaFin, nuriCondicion);
            }
        }catch(Exception e){
            throw new Exception("Error al generar el reporte. Error: "+ e.getMessage());
        }
    }

    @Override
    public List<HojasRuta> listadoHojasSeguimientoInternasCreadas(String usuario) throws Exception {
        try {
            return hojasRutaRepository.listadoHojasSeguimientoInternasCreadas(usuario);
        }catch(Exception e){
            throw new Exception("Error al eliminar, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public HojasRuta anularHojaRuta(HojasRutaPojo inHojasRutaPojo) throws Exception {
        try {
            HojasRuta hr = hojasRutaRepository.buscarPadreHoja(inHojasRutaPojo.getNur());
            if(hr == null) throw new Exception("No se encontro el NUR o NURI enviado.");

            Long estado = inHojasRutaPojo.getNur().contains("I/")? 68L : 69L;
            int res = hojasRutaRepository.anularHojaRuta(inHojasRutaPojo.getNur(), estado, inHojasRutaPojo.getUsuario());

            if(res == 1) return hojasRutaRepository.buscarPadreHoja(inHojasRutaPojo.getNur());
            throw new Exception("Error al anular, no se genero ninguna operacion.");
        }catch(Exception e){
            throw new Exception("Error al anular, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    public List<HojasRuta> listadoHojasSeguimientoExternasCreadas(String usuario) throws Exception {
        try {
            return hojasRutaRepository.listadoHojasSeguimientoExternasCreadas(usuario);
        }catch(Exception e){
            throw new Exception("Error al listar las Hojas de Seguimiento Externas creadas. Error: "+ e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public HojasRuta anularHojaRutaExterno(HojasRutaPojo inHojasRutaPojo) throws Exception {
        try {
            HojasRuta hr = hojasRutaRepository.buscarPadreHoja(inHojasRutaPojo.getNur());
            if(hr == null) throw new Exception("No se encontro el NUR o NURI enviado.");

            Long estado = inHojasRutaPojo.getNur().contains("I/")? 68L : 69L;
            int res = hojasRutaRepository.anularHojaRutaExterno(inHojasRutaPojo.getNur(), estado, inHojasRutaPojo.getUsuario());

            if(res == 1) return hojasRutaRepository.buscarPadreHoja(inHojasRutaPojo.getNur());
            throw new Exception("Error al anular.");
        }catch(Exception e){
            throw new Exception("Error al anular, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }
    
}
