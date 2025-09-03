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
import siacor.model.pojo.DataActualizaListaPojo;
import siacor.service.ListasDerivacionService;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
public class ListasDerivacionController {
    @Autowired
    private final ListasDerivacionService listasDerivacionService;

    public ListasDerivacionController(ListasDerivacionService listasDerivacionService){
        this.listasDerivacionService = listasDerivacionService;
    }

    @GetMapping("/buscarJefeUsuario11/{usuario}")
    public ResponseEntity<?> buscarJefeUsuario(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, listasDerivacionService.buscarJefeUsuario(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listaDerivacion/{usuario}")
    public ResponseEntity<?> listaDerivacion(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, listasDerivacionService.listaDerivacion(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listaDerivacionUsuario/{usuario}")
    public ResponseEntity<?> listaDerivacionUsuario(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, listasDerivacionService.listaDerivacionUsuario(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizaOpcionLista")
    public ResponseEntity<?> actualizaOpcionLista(@Validated @RequestBody DataActualizaListaPojo pojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, listasDerivacionService.actualizaOpcionLista(pojo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
