package siacor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import siacor.comun.response.ResponseHandler;
import siacor.model.pojo.PdfResponse;

import siacor.service.PdfStorageService;

import static siacor.comun.constante.RespuestasConstants.RESPUESTA_CORRECTA;

@RestController
@CrossOrigin
public class PdfController {
    @Autowired
    private final PdfStorageService pdfStorageService;

    public PdfController(PdfStorageService pdfStorageService){
        this.pdfStorageService = pdfStorageService;
    }

    @PostMapping("/subirPdf/**")
    public ResponseEntity<?> subirPdf(HttpServletRequest request, @RequestParam("archivos") List<MultipartFile> archivos) {
        try {
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/subirPdf/";
            String subcarpeta = fullPath.substring(fullPath.indexOf(basePath) + basePath.length()).replace("/", "_");
            System.out.println("SUBCARPETA: " + subcarpeta);
            
            // Procesar cada archivo individualmente
            List<PdfResponse> respuestas = new ArrayList<>();
            for (MultipartFile archivo : archivos) {
                // Llamar al servicio para guardar y comprimir cada archivo
                PdfResponse respuesta = pdfStorageService.guardarPdfConCompresion(subcarpeta, archivo);
                respuestas.add(respuesta);
            }

            return ResponseEntity.ok(respuestas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el servidor: " + e.getMessage());
        }
    }

    @PostMapping("/subir/**")
    public ResponseEntity<?> subirPdf(HttpServletRequest request, @RequestParam("archivo") MultipartFile archivo) {
        try {
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/subir/";
            String subcarpeta = fullPath.substring(fullPath.indexOf(basePath) + basePath.length()).replace("/", "_");
            System.out.println("SUBCARPETA: " + subcarpeta);
            PdfResponse respuesta = pdfStorageService.guardarPdfConCompresion(subcarpeta, archivo);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el servidor: " + e.getMessage());
        }
    }

    // @DeleteMapping("/eliminarPdf/{codigo}/{nuri}/{nombreArchivo}")
    // public ResponseEntity<?> eliminarPdf(@PathVariable String codigo, @PathVariable String nuri, @PathVariable String nombreArchivo) {
    @DeleteMapping("/eliminarPdf/**")
    public ResponseEntity<?> eliminarPdf(HttpServletRequest request) {
        try{
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/eliminarPdf/";
            String rutaSinPrefijo = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            String[] partes = rutaSinPrefijo.split("/");

            String carpeta = "";
            String nombreArchivo = "";
            if(partes.length == 3) {
                carpeta = partes[0] + "_" + partes[1];
                nombreArchivo = partes[2];
            } else if(partes.length == 2){
                carpeta = partes[0];
                nombreArchivo = partes[1];
            }

            return ResponseHandler.generateResponse(RESPUESTA_CORRECTA, HttpStatus.OK, pdfStorageService.eliminarArchivo(carpeta, nombreArchivo));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/listar/**")
    public ResponseEntity<?> listarDocumentos(HttpServletRequest request) {
        try {
            // String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            // String basePath = "/listar/";
            // String carpeta = fullPath.substring(fullPath.indexOf(basePath) + basePath.length()).replace("/", "_");
            // System.out.println("SUBCARPETA: " + carpeta);

            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/listar/";
            String rutaSinPrefijo = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            String[] partes = rutaSinPrefijo.split("/");

            String carpeta = partes.length == 2?partes[0] + "_" + partes[1]:partes[0];
        
            List<String> archivos = pdfStorageService.listarArchivos(carpeta);

            String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            List<String> urls = archivos.stream()
                    .map(nombre -> baseUrl + "/documentos/descargar/" + carpeta + "/" + nombre)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/descargar/**")
    public ResponseEntity<StreamingResponseBody> descargarArchivo(HttpServletRequest request) {
        try {
            String fullPath = request.getRequestURI(); // Ej: /datosDocumentos/NI/GER/SIS/2025-0236
            String basePath = "/descargar/";
            String rutaSinPrefijo = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());

            String[] partes = rutaSinPrefijo.split("/");

            String carpeta = "";
            String nombreArchivo = "";
            if(partes.length == 3) {
                carpeta = partes[0] + "_" + partes[1];
                nombreArchivo = partes[2];
            } else if(partes.length == 2){
                carpeta = partes[0];
                nombreArchivo = partes[1];
            }

            Path ruta = pdfStorageService.obtenerArchivo(carpeta, nombreArchivo);

            if (!Files.exists(ruta)) {
                return ResponseEntity.notFound().build();
            }

            InputStream inputStream = Files.newInputStream(ruta);

            StreamingResponseBody body = outputStream -> {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
            };

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(body);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        
    }
}
