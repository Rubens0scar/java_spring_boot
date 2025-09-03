package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import siacor.comun.response.ResponseHandler;
import siacor.model.pojo.HojasRutaEditPojo;
import siacor.model.pojo.HojasRutaPojo;
import siacor.service.HojasRutaService;

import static siacor.comun.constante.RespuestasConstants.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
public class HojasRutaController {
    @Autowired
    private final HojasRutaService hojasRutaService;

    public HojasRutaController(HojasRutaService hojasRutaService){
        this.hojasRutaService = hojasRutaService;
    }

    @PostMapping("/generarNuri")
    public ResponseEntity<?> generarNuri(@Validated @RequestBody HojasRutaPojo inputHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, hojasRutaService.generarHojaRuta(inputHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizaHojaRuta")
    public ResponseEntity<?> actualizaHojaRuta(@Validated @RequestBody HojasRutaEditPojo inputHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, hojasRutaService.actualizaHojasRuta(inputHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/guardarHojaRuta")
    public ResponseEntity<?> guardarHojaRuta(@Validated @RequestBody HojasRutaPojo inputHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, hojasRutaService.guardarHojaRuta(inputHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @DeleteMapping("/eliminarHojaRuta")
    public ResponseEntity<?> eliminarHojaRuta(@Validated @RequestBody HojasRutaPojo inputHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, hojasRutaService.eliminaHojaRuta(inputHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
 
    @GetMapping("/listarCites/**")
    public ResponseEntity<?> listarCites(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/listarCites/";
            String nur = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, hojasRutaService.listarCites(nur));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/asignaNuriPendiente")
    public ResponseEntity<?> asignaNuriPendiente(@Validated @RequestBody HojasRutaPojo inHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, hojasRutaService.asignaNuriPendiente(inHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/buscaRegistroUltimo/{nur}/{codigo}")
    public ResponseEntity<?> buscaRegistroUltimo(@PathVariable String nur,@PathVariable String codigo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, hojasRutaService.buscaRegistroUltimo(nur+"/"+codigo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/recuperaNuriCite/**")
    public ResponseEntity<?> recuperaNuriCite(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/recuperaNuriCite/";
            String cite = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("CITE: " + cite);

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, hojasRutaService.recuperaNuriCite(cite));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/reporteCreadosNuri/**")
    public ResponseEntity<?> reporteCreadosNuri(HttpServletRequest request/*@Validated @RequestBody ReporteCreadosPojo pojo/ */) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/reporteCreadosNuri/";
            String rutaSinPrefijo = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            String[] partes = rutaSinPrefijo.split("/");

            int cantidad = partes.length;
            String usuario = partes[0];
            Integer opcionNuri = null;
            String fechaIni = null;
            String fechaFin = null;

            if(cantidad == 8){
                opcionNuri = Integer.parseInt(partes[1]);
                fechaIni = partes[2] + "/" + partes[3] + "/" + partes[4];
                fechaFin = partes[5] + "/" + partes[6] + "/" + partes[7];
            } else if(cantidad == 7){
                opcionNuri = null;
                fechaIni = partes[1] + "/" + partes[2] + "/" + partes[3];
                fechaFin = partes[4] + "/" + partes[5] + "/" + partes[6];
            } else if (cantidad == 6){
                if(partes[5].equals("null")){
                    opcionNuri = partes[1].equals("null")?0:Integer.parseInt(partes[1]);
                    fechaIni = partes[2] + "/" + partes[3] + "/" + partes[4];
                    fechaFin = null;
                } else if(partes[2].equals("null")){
                    opcionNuri = partes[1].equals("null")?0:Integer.parseInt(partes[1]);
                    fechaIni = null;
                    fechaFin = partes[3] + "/" + partes[4] + "/" + partes[5];
                } 
            } else if(cantidad == 5){
                opcionNuri = Integer.parseInt(partes[1]);
                fechaIni = partes[2] + "/" + partes[3] + "/" + partes[4];
                fechaFin = null;
            } else if(cantidad == 4){
                opcionNuri = null;
                fechaIni = partes[1] + "/" + partes[2] + "/" + partes[3];
                fechaFin = null;
            } else if(cantidad == 2){
                opcionNuri = Integer.parseInt(partes[1]);
                fechaIni = null;
                fechaFin = null;
            }
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, hojasRutaService.reporteCreadosNuri(usuario, opcionNuri, fechaIni, fechaFin));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listadoHojasSeguimientoInternasCreadas/{usuario}")
    public ResponseEntity<?> listadoHojasSeguimientoInternasCreadas(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, hojasRutaService.listadoHojasSeguimientoInternasCreadas(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @PutMapping("/anularHojaRuta")
    public ResponseEntity<?> anularHojaRuta(@Validated @RequestBody HojasRutaPojo inHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, hojasRutaService.anularHojaRuta(inHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listadoHojasSeguimientoExternasCreadas/{usuario}")
    public ResponseEntity<?> listadoHojasSeguimientoExternasCreadas(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.ACCEPTED, hojasRutaService.listadoHojasSeguimientoExternasCreadas(usuario));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/anularHojaRutaExterno")
    public ResponseEntity<?> anularHojaRutaExterno(@Validated @RequestBody HojasRutaPojo inHojasRutaPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, hojasRutaService.anularHojaRutaExterno(inHojasRutaPojo));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

}
