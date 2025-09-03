package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import siacor.comun.response.ResponseHandler;
import siacor.model.entity.Usuarios;
//import siacor.model.entity.Usuarios;
import siacor.service.DocumentosService;
import siacor.service.UsuarioService;
import siacor.utilitarios.PasswordEncryptor;
//import siacor.utilitarios.PasswordVerifier;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;
import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.PathVariable;


@RestController
@CrossOrigin
public class
HelloController {
    @Autowired
    private PasswordEncryptor passwordEncryptor;

    // @Autowired
    // private PasswordVerifier passwordVerifier;

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    private final DocumentosService documentosService;

    public HelloController(UsuarioService usuarioService,DocumentosService documentosService){
        this.usuarioService = usuarioService;
        this.documentosService = documentosService;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        try{
            // Usuarios usuarios = usuarioService.buscarUsuario("guancollo.ruben");
            // //return "Hello, World!" + usuarios.getNombreCompleto();
            // return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, usuarios);

            //encriptar
            System.out.println("CONTRASEÑA ENCRIPTADA: " + passwordEncryptor.encryptPassword("ruben0scar"));
            System.out.println("CONTRASEÑA ENCRIPTADA: " + passwordEncryptor.encryptPassword("MUMANAL"));
            // String pass = "$2a$10$dK/29sj3JD8LZWBRjIc8EuscFuh.DBwHUT5sWn1YvlrPFfa4Bwh4y";
            return ResponseEntity.ok(passwordEncryptor.encryptPassword("MUMANAL"));

            //verificar lo encriptado
            // System.out.println("Antes de verificar");
            //     String pass ="$2a$10$BKKDfeGVNhHE0oe6DL5HgOg6P6SHJaziIkeYzI1MxL.caSSzgY5Fy";
            //     System.out.println("ES IGUAL EL PASS: " + passwordVerifier.verifyPassword("ruben0scar", pass));
            //     return ResponseEntity.ok(passwordVerifier.verifyPassword("ruben0scar", pass));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/update")
    public int putMethodName() {
        try{
            //actualizar campo habilitado de vias
            return documentosService.actulizarHabilitados("NI/GER/SIS/2025-0001");
        }
        catch(Exception e){
            e.printStackTrace();
            return -9;
        }
    }
}
