package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import siacor.comun.response.ResponseHandler;
import siacor.model.pojo.DatosCopiaPojo;
import siacor.model.pojo.DatosPojo;
import siacor.model.pojo.SeguimientoDataPojo;
import siacor.model.pojo.SeguimientoEditPojo;
import siacor.model.pojo.SeguimientoPojo;
import siacor.service.SeguimientoService;

import static siacor.comun.constante.RespuestasConstants.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
public class SeguimientosController {
    @Autowired
    private final SeguimientoService seguimientoService;

    public SeguimientosController(SeguimientoService seguimientoService){
        this.seguimientoService = seguimientoService;
    }
    
    @PostMapping("/registraSeguimiento")
    public ResponseEntity<?> registraSeguimiento(@Validated @RequestBody SeguimientoPojo inputHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, seguimientoService.registrarSeguimientos(inputHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizaSeguimiento")
    public ResponseEntity<?> actualizaSeguimiento(@Validated @RequestBody SeguimientoEditPojo iSeguimientoEditPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.actualizarSeguimientos(iSeguimientoEditPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @DeleteMapping("/eliminarSeguimiento")
    public ResponseEntity<?> eliminarSeguimiento(@Validated @RequestBody SeguimientoPojo inpuSeguimientoPojo) {
        try{
            //return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.eliminarSeguimientos(inpuSeguimientoPojo));
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.eliminarSeguimientosData(inpuSeguimientoPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizarFechaRecepcionSeguimiento")
    public ResponseEntity<?> actualizarFechaRecepcionSeguimiento(@Validated @RequestBody SeguimientoPojo inpuSeguimientoPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.actualizaFechaRecepcion(inpuSeguimientoPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listadoEntrantes/{usuario}")
    public ResponseEntity<?> listadoEntrantes(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.listadoEntrantes(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/cantidadEntrantes/{usuario}")
    public int cantidadEntrantes(@PathVariable String usuario) throws Exception {
        try{
            return seguimientoService.cantidadEntrantes(usuario);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al contar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @GetMapping("/listadoRecibidos/{usuario}")
    public ResponseEntity<?> listadoRecibidos(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.listadoRecibidos(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/cantidadRecibidos/{usuario}")
    public int cantidadRecibidos(@PathVariable String usuario) throws Exception {
        try{
            return seguimientoService.cantidadRecibidos(usuario);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al contar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @PostMapping("/registrarSeguimientosDerivado")
    public ResponseEntity<?> registrarSeguimientosDerivado(@Validated @RequestBody SeguimientoPojo inputHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, seguimientoService.registrarSeguimientosDerivado(inputHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
 
    @GetMapping("/historicoSeguimientos/**")
    public ResponseEntity<?> historicoSeguimientos(HttpServletRequest request) throws Exception {
        try{
            String fullPath = request.getRequestURI();
            String basePath = "/historicoSeguimientos/";
            String nuri = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("NURI: " + nuri);
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.historicoSeguimiento(nuri));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al buscar el NURI ingresado, por favor intente nuevamente." + e.getMessage());
        }
    }

    @PutMapping("/actualizarEstadoSeguimiento")
    public ResponseEntity<?> actualizarEstadoSeguimiento(@Validated @RequestBody DatosPojo inDatosPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.actualizarEstadoSeguimiento(inDatosPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // @DeleteMapping("/eliminarSeguimientosDerivado/{id}/{nur}/{codigo}")
    // public ResponseEntity<?> eliminarSeguimientosDerivado(@PathVariable Long id,@PathVariable String nur,@PathVariable String codigo) {
    //     try{
    //         return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.eliminarSeguimientosDerivado(id,nur+"/"+codigo));
    //     }
    //     catch (Exception e){
    //         e.printStackTrace();
    //         return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
    //     }
    // }

    // @GetMapping("/listaUltimoSeguimiento/{usuario}/{oficial}/{nuri}/{codigo}")
    // public ResponseEntity<?> listaUltimoSeguimiento(@PathVariable String usuario, @PathVariable Integer oficial, @PathVariable String nuri,@PathVariable String codigo) {
    @GetMapping("/listaUltimoSeguimiento/**")
    public ResponseEntity<?> listaUltimoSeguimiento(HttpServletRequest request) {
        try{
            String resultado = "";
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/listaUltimoSeguimiento/";
            String rutaSinPrefijo = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            String[] partes = rutaSinPrefijo.split("/");
            String usuario = partes[0];
            Integer oficial = Integer.parseInt(partes[1]);
            String nur = partes.length == 4?partes[2]+"/"+partes[3]:partes[2];

            List<SeguimientoDataPojo> seg = seguimientoService.listarSeguimientosData(oficial, nur, usuario);
            if(seg == null) {
                resultado = "Datos no encontrados";
                return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.NOT_FOUND, resultado);
            } else {
                return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seg);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listaUltimoSeguimientoDestino/{idSeguimiento}/{nuri}/{codigo}")
    public ResponseEntity<?> listaUltimoSeguimientoDestino(@PathVariable Long idSeguimiento,@PathVariable String nuri,@PathVariable String codigo) {
        try{
            String resultado = "";
            List<SeguimientoDataPojo> seg = seguimientoService.listarSeguimientosDestino(idSeguimiento, nuri+"/"+codigo);
            if(seg == null) {
                resultado = "Datos no encontrados destino.";
                return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.NOT_FOUND, resultado);
            } else {
                return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seg);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/listarNurisPendientes/{usuario}")
    public ResponseEntity<?> listarNurisPendientes(@PathVariable String usuario) throws Exception {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.listarNurisPendientes(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al listar los NURI's pendientes, por favor intente nuevamente." + e.getMessage());
        }
    }

    @GetMapping("/contarArchivados/{usuario}")
    public ResponseEntity<?> contarArchivados(@PathVariable String usuario) throws Exception {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.contarArchivados(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al contar documentos archivados, por favor intente nuevamente." + e.getMessage());
        }
    }

    @PutMapping("/archivarDocumento")
    public ResponseEntity<?> archivarDocumento(@Validated @RequestBody DatosPojo inDatosPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.archivarDocumento(inDatosPojo.getNuri()));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/archivarDocumentoCopia")
    public ResponseEntity<?> archivarDocumentoCopia(@Validated @RequestBody DatosCopiaPojo data) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.archivarDocumentoCopia(data));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/listadoRecibidosCopias/{usuario}")
    public ResponseEntity<?> listadoRecibidosCopias(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.listadoRecibidosCopias(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/cantidadRecibidosCopias/{usuario}")
    public int cantidadRecibidosCopias(@PathVariable String usuario) throws Exception {
        try{
            return seguimientoService.cantidadRecibidosCopias(usuario);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al contar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @GetMapping("/cantidadEntrantesCopias/{usuario}")
    public int cantidadEntrantesCopias(@PathVariable String usuario) throws Exception {
        try{
            return seguimientoService.cantidadEntrantesCopias(usuario);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al contar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @PutMapping("/actualizarEstadoSeguimientoCopia")
    public ResponseEntity<?> actualizarEstadoSeguimientoCopia(@Validated @RequestBody DatosPojo inDatosPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, seguimientoService.actualizarEstadoSeguimientoCopia(inDatosPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/registrarSeguimientosDerivadoCopia")
    public ResponseEntity<?> registrarSeguimientosDerivadoCopia(@Validated @RequestBody SeguimientoPojo inputHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, seguimientoService.registrarSeguimientosDerivadoCopia(inputHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/listadoDerivadosPor/{usuario}")
    public ResponseEntity<?> listadoDerivadosPor(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, seguimientoService.listadoDerivadosPor(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @PutMapping("/reestablecerDerivacion")
    public ResponseEntity<?> restablecerDerivacion(@Validated @RequestBody DatosCopiaPojo data) {
        try{
            Integer res = seguimientoService.restablecerDerivacion(data);
            if(res >= 1) return ResponseHandler.generateResponse("Restablecimiento correcto", HttpStatus.ACCEPTED, null);

             return ResponseHandler.generateResponse("Hubo un problema al realizar el restablecimiento, favor revisar los datos", HttpStatus.CONFLICT, null);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/listaNuriNoRecepcionados/{oficina}")
    public ResponseEntity<?> listaNuriNoRecepcionados(@PathVariable Long oficina) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, seguimientoService.listaNuriNoRecepcionados(oficina));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/reporteSeguimientos/{derivadoPor}/{derivadoA}")
    public ResponseEntity<?> reporteSeguimientos(@PathVariable String derivadoPor,@PathVariable String derivadoA) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, seguimientoService.reporteSeguimientos(derivadoPor,derivadoA));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
