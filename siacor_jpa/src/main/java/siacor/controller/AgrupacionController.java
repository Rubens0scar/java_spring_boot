package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;

import siacor.comun.response.ResponseHandler;
import siacor.model.pojo.AgrupacionEditPojo;
import siacor.model.pojo.AgrupacionPojo;
import siacor.service.AgrupacionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@CrossOrigin
public class AgrupacionController {
    @Autowired
    private final AgrupacionService agrupacionService;

    public AgrupacionController(AgrupacionService agrupacionService){
        this.agrupacionService = agrupacionService;
    }

    @GetMapping("/listaAgrupacion/{usuario}/{cod}/{num}")
    public ResponseEntity<?> listaAgrupacion(@PathVariable String usuario, @PathVariable String cod, @PathVariable String num) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, agrupacionService.listaAgrupacion(cod+"/"+num, usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/registraAgrupacion")
    public ResponseEntity<?> registraAgrupacion(@Validated @RequestBody AgrupacionPojo inputAgrupacionPojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, agrupacionService.registraAgrupacion(inputAgrupacionPojo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listarAgrupados/{cod}/{num}")
    public ResponseEntity<?> listarAgrupados(@PathVariable String cod, @PathVariable String num) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, agrupacionService.listarAgrupados(cod+"/"+num));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizarEstados")
    public ResponseEntity<?> actualizarEstados(@Validated @RequestBody AgrupacionEditPojo pojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, agrupacionService.actualizarEstados(pojo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @DeleteMapping("/eliminarAgrupacion/{id}")
    public ResponseEntity<?> eliminarAgrupacion(@PathVariable Long id) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, agrupacionService.eliminarAgrupacion(id));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

}
