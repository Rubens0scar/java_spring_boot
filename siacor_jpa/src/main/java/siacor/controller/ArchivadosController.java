package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import siacor.comun.response.ResponseHandler;
import siacor.model.pojo.ArchivadosPojo;
import siacor.service.ArchivadosService;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@CrossOrigin
public class ArchivadosController {
    @Autowired
    private final ArchivadosService archivadosService;

    public ArchivadosController(ArchivadosService archivadosService){
        this.archivadosService = archivadosService;
    }

    @PostMapping("/registraArchivados")
    public ResponseEntity<?> registraArchivados(@Validated @RequestBody ArchivadosPojo inputArchivadosPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.registraArchivados(inputArchivadosPojo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listarArchivadosCarpeta/{persona}/{lugar}")
    public ResponseEntity<?> listarArchivadosCarpeta(@PathVariable String persona, @PathVariable String lugar) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.listarArchivados(persona, lugar, ""));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listarArchivadosLugar/{usuario}")
    public ResponseEntity<?> listarArchivadosLugar(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.listarArchivadosLugar(usuario, ""));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listarArchivadosCarpetaCopia/{persona}/{lugar}")
    public ResponseEntity<?> listarArchivadosCarpetaCopia(@PathVariable String persona, @PathVariable String lugar) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.listarArchivados(persona, lugar, "SI"));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listarArchivadosLugarCopia/{usuario}")
    public ResponseEntity<?> listarArchivadosLugarCopia(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.listarArchivadosLugar(usuario, "SI"));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listarArchivadosReporte/{oficina}")
    public ResponseEntity<?> listarArchivadosReporte(@PathVariable Long oficina) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.listarArchivadosReporte(oficina));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/detalleArchivado/{idArchivados}")
    public ResponseEntity<?> detalleArchivado(@PathVariable Long idArchivados) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.datosArchivado(idArchivados));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @PutMapping("/actualizaDatosArchivado")
    public ResponseEntity<?> actualizaDatosArchivado(@Validated @RequestBody ArchivadosPojo pojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, archivadosService.actualizaDatos(pojo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}

