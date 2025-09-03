package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import siacor.comun.response.ResponseHandler;
import siacor.service.SessionesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;

import java.time.LocalDate;

@RestController
@CrossOrigin
public class SesionesController {
    @Autowired
    private final SessionesService sessionesService;

    public SesionesController(SessionesService sessionesService){
        this.sessionesService = sessionesService;
    }

    @GetMapping("/listarEstadoUsuarios/{oficina}/{dia}/{mes}/{anio}")
    public ResponseEntity<?> listarEstadoUsuarios(@PathVariable Long oficina, @PathVariable Integer dia,@PathVariable Integer mes,@PathVariable Integer anio) {
        try{
            LocalDate fecha = LocalDate.of(anio, mes, dia);
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, sessionesService.listaEstadoUsuarios(oficina,fecha));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/listarEstadoUsuariosTotal/{oficina}/{fecha}")
    public ResponseEntity<?> listarEstadoUsuariosTotal(@PathVariable Long oficina, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, sessionesService.listarEstadoUsuarios(oficina,fecha));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listaUsuarioDetalle/{usuario}/{dia}/{mes}/{anio}")
    public ResponseEntity<?> listaUsuarioDetalle(@PathVariable String usuario, @PathVariable Integer dia,@PathVariable Integer mes,@PathVariable Integer anio) {
        try{
            LocalDate fecha = LocalDate.of(anio, mes, dia);
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, sessionesService.listaUsuarioDetalle(usuario,fecha));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
