package siacor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import siacor.model.pojo.DocumentosEditExternoPojo;
//import io.jsonwebtoken.Claims;
import siacor.comun.response.ResponseHandler;
import siacor.model.pojo.BuscadorDataPojo;
//import siacor.model.pojo.DocumentosEditPojo;
import siacor.model.pojo.DocumentosPojo;
import siacor.service.DocumentosService;
//import siacor.service.JwtTokenUtil;

import static siacor.comun.constante.RespuestasConstants.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
public class DocumentosController {
    @Autowired
    private final DocumentosService documentosService;

    // @Autowired
    // private final JwtTokenUtil jwtTokenUtil;

    public DocumentosController(DocumentosService documentosService){
        this.documentosService = documentosService;
        //this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/registraDocumento")
    public ResponseEntity<?> registraDocumento(@Validated @RequestBody DocumentosPojo inputDocumento) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.CREATED, documentosService.registrarDocumentos(inputDocumento));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizaDocumento/**")
    public ResponseEntity<?> actualizaDocumento(@Validated @RequestBody DocumentosPojo iDocumentosEditPojo,HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI();
            String basePath = "/actualizaDocumento/";
            String cite = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("CITE: " + cite);

            return ResponseHandler.generateResponse(ACTUALIZACION_CORRECTA, HttpStatus.CREATED, documentosService.actualizarDocumentos(iDocumentosEditPojo,cite));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listaDocumentos/{usuario}")
    public ResponseEntity<?> listaDocumentos(@PathVariable String usuario,HttpServletRequest request) {
        try{
            // String token = request.getHeader("Authorization");
            // System.out.println("TOKEN: " + token);
            // String user = jwtTokenUtil.extractUsername(token.replace("Bearer ", ""));  // Este método puede ser el que decodifica el token y obtiene todos los claims.
            // System.out.println("USUARIO: " + user);

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.listaDocumentos(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/datosDocumentos/**")
    public ResponseEntity<?> datosDocumentos(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/datosDocumentos/";
            String rutaSinPrefijo = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            String[] partes = rutaSinPrefijo.split("/");

            // Validar que hay al menos 3 partes (para armar los dos segmentos)
            if (partes.length < 4) {
                return ResponseEntity.badRequest().body("Ruta inválida");
            }

            // Extraer primer parámetro fijo
            String nuri = "";
            int inicio = 0;
            if(partes[0].equals("I")){
                nuri = partes[0] + "/" + partes[1];
                inicio = 2;
            }else {
                nuri = partes[0];
                inicio = 1;
            }
            // El resto es el segundo parámetro
            StringBuilder sb = new StringBuilder();
            for (int i = inicio; i < partes.length; i++) {
                sb.append(partes[i]);
                if (i < partes.length - 1) sb.append("/");
            }
            String cite = sb.toString(); // NI/GER/SIS/2025-0002

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.datosDocumentos(nuri,cite));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/datosDocumentosNuri/**")
    public ResponseEntity<?> datosDocumentosNuri(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/datosDocumentosNuri/";
            String cite = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("CITE: " + cite);
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.datosDocumentosNuri(cite));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listaDatosDocumentos/{usuario}/{codigo}")
    public ResponseEntity<?> listaDatosDocumentos(@PathVariable String usuario,@PathVariable String codigo) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.listaDatosDocumentos(usuario,codigo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/documentoValidar/**")
    public ResponseEntity<?> documentoValidar(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/documentoValidar/";
            String cite = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("CITE: " + cite);

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.documentoValidar(cite));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listaDatosDocumentosTodo/{usuario}")
    public ResponseEntity<?> listaDatosDocumentosTodo(@PathVariable String usuario) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.listaDatosDocumentosTodo(usuario));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
    
    @GetMapping("/datosDocumentosCite/**")
    public ResponseEntity<?> datosDocumentosCite(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/datosDocumentosCite/";
            String cite = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("CITE: " + cite);

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.datosDocumentosCite(cite));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/reporteBuscador")
    public ResponseEntity<?> reporteBuscador(@Validated @RequestBody BuscadorDataPojo data) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.reporteBuscador(data));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/fechaPrimerDocumento")
    public String fechaPrimerDocumento() throws Exception {
        try{
            return documentosService.fechaPrimerDocumento();
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error al buscar la fecha del primer documento creado." + e.getMessage());
        }
    }

    @GetMapping("/listarDocumentosDetalle/{tipoDocumento}/{oficina}")
    public ResponseEntity<?> getMethodName(@PathVariable Long tipoDocumento,@PathVariable Long oficina) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.listarDocumentos(tipoDocumento, oficina));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/ultimoDocumento/{tipoDocumento}/{oficina}")
    public ResponseEntity<?> ultimoDocumento(@PathVariable Long tipoDocumento,@PathVariable Long oficina) {
        try{
            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.ultimoDocumento(tipoDocumento, oficina));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/datosDocumentosCiteConNuri/**")
    public ResponseEntity<?> datosDocumentosCiteConNuri(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/datosDocumentosCiteConNuri/";
            String cite = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("CITE: " + cite);

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.datosDocumentosCiteConNuri(cite));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/actualizaDocumentoExterno/**")
    public ResponseEntity<?> actualizaDocumentoExterno(@Validated @RequestBody DocumentosEditExternoPojo iDocumentosEditPojo,HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI();
            String basePath = "/actualizaDocumentoExterno/";
            String cite = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("CITE: " + cite);

            return ResponseHandler.generateResponse(ACTUALIZACION_CORRECTA, HttpStatus.CREATED, documentosService.actualizarDocumentosExterno(iDocumentosEditPojo,cite));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/datosDocumentosExternos/**")
    public ResponseEntity<?> datosDocumentosExternos(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/datosDocumentosExternos/";
            String rutaSinPrefijo = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            String[] partes = rutaSinPrefijo.split("/");

            // Extraer primer parámetro fijo
            String nur = "";
            int inicio = 0;
            nur = partes[0];
            inicio = 1;
            // El resto es el segundo parámetro
            StringBuilder sb = new StringBuilder();
            for (int i = inicio; i < partes.length; i++) {
                sb.append(partes[i]);
                if (i < partes.length - 1) sb.append("/");
            }
            String cite = sb.toString(); // NI/GER/SIS/2025-0002

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, documentosService.datosDocumentosExternos(nur,cite));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }   
    


}