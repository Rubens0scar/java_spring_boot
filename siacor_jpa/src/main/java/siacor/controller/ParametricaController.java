package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import siacor.comun.response.ResponseHandler;
import siacor.service.ParametricaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;


@RestController
@CrossOrigin
public class ParametricaController {
    @Autowired
    private final ParametricaService parametricaService;

    public ParametricaController(ParametricaService parametricaService){
        this.parametricaService = parametricaService;
    }

    @GetMapping("/listarParametrica/{tipo}")
    public ResponseEntity<?> listarParametrica(@PathVariable String tipo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, parametricaService.listarModulos(tipo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/buscarTipoDocumento/{codigo}")
    public ResponseEntity<?> buscarTipoDocumento(@PathVariable String codigo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, parametricaService.buscarTipoDocumento(codigo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/buscarDatosID/{idParametrica}")
    public ResponseEntity<?> buscarDatosID(@PathVariable Long idParametrica) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, parametricaService.buscarDatosID(idParametrica));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
