package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import siacor.comun.response.ResponseHandler;
import siacor.model.pojo.AutenticacionUsuarioPojo;
import siacor.model.pojo.DatosPojo;
import siacor.service.UsuarioService;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
public class AutentificacionController {
    @Autowired
    private final UsuarioService usuarioService;

    public AutentificacionController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/autentificacion")
    public ResponseEntity<?> autentificacion(@Validated @RequestBody AutenticacionUsuarioPojo autenticacion,HttpServletRequest request) {
        try{
            autenticacion.setAgente(request.getHeader("User-Agent"));
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.autentificacion(autenticacion));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/cerrarSesiones")
    public ResponseEntity<?> cerrarSesiones(@Validated @RequestBody DatosPojo data) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.cerrarSesiones(data.getId(),"Por Usuario"));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
