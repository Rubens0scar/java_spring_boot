package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import siacor.comun.response.ResponseHandler;
import siacor.model.Dto.UsuarioDto;
import siacor.model.pojo.UsuarioCambioPasswordPojo;
import siacor.model.pojo.UsuarioPojo;
import siacor.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin
public class UsuariosController {
    @Autowired
    private final UsuarioService usuarioService;

    public UsuariosController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listarJefesUsuariosDpto/{usuario}")
    public ResponseEntity<?> listarJefesUsuariosDpto(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.listarJefesUsuariosDpto(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/buscarJefeUsuario/{usuario}")
    public ResponseEntity<?> usuarioVia(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.usuarioVia(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/sesionesEstadoUsuarios/{oficina}")
    public ResponseEntity<?> sesionesEstadoUsuarios(@PathVariable Long oficina) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.sesionesEstadoUsuarios(oficina));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/buscarDataUsuario/{usuario}")
    public ResponseEntity<?> buscarDataUsuario(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.buscarDataUsuario(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    //CRUD
    @PostMapping("/registraUsuario")
    public ResponseEntity<?> registraUsuario(@Validated @RequestBody UsuarioPojo pojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.registraUsuario(pojo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizaPasswordUsuario")
    public ResponseEntity<?> actualizaPasswordUsuario(@Validated @RequestBody UsuarioCambioPasswordPojo pojo) {
        try{
            UsuarioDto dto = usuarioService.actualizaPassword(pojo);
            if(dto != null) return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, dto);
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CONFLICT, "Formato incorrecto, el password debe tener minimamente 8 caracteres.");
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizaUsuario")
    public ResponseEntity<?> actualizaUsuario(@Validated @RequestBody UsuarioPojo pojo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarioService.actualizaUsuarios(pojo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    

}
