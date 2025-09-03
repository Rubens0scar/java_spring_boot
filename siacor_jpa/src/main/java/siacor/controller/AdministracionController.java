package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import siacor.comun.response.ResponseHandler;
import siacor.service.AdministracionService;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;

@RestController
@CrossOrigin
public class AdministracionController {
    @Autowired
    private final AdministracionService administracionService;

    public AdministracionController(AdministracionService administracionService){
        this.administracionService = administracionService;
    }

    @GetMapping("/resetearBase/{gestion}")
    public ResponseEntity<?> buscarJefeUsuario(@PathVariable Integer gestion) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, administracionService.resetearBase(gestion));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
