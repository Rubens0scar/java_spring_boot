package siacor.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import siacor.model.Dto.AgrupacionDto;
import siacor.model.Dto.DatosHojaRutaDto;
import siacor.model.Dto.ModuloDto;
import siacor.model.pojo.AgrupacionEditPojo;
import siacor.model.pojo.DatosHojaRutaPojo;
import siacor.service.AgrupacionService;
import siacor.service.DocumentosService;
import siacor.service.ParametricaService;
import siacor.service.ReporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping()
public class ReportController {
    @Autowired
    private final ReporteService reportService;

    @Autowired
    private final ParametricaService parametricaService;

    @Autowired
    private final DocumentosService documentosService;

    @Autowired
    private final AgrupacionService agrupacionService;

    public ReportController(ReporteService reportService,ParametricaService parametricaService,DocumentosService documentosService,AgrupacionService agrupacionService) {
        this.reportService = reportService;
        this.parametricaService = parametricaService;
        this.documentosService = documentosService;
        this.agrupacionService = agrupacionService;
    }

    @GetMapping(value = "/reporteHoja/**")
    public ResponseEntity<byte[]> generarHoja(HttpServletRequest request) {
        try {
            String fullPath = request.getRequestURI();
            String basePath = "/reporteHoja/";
            String nuri = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("NURI: " + nuri);

            DatosHojaRutaDto hojaRuta =  documentosService.datosHojaRuta(nuri);
            if(hojaRuta == null) throw new RuntimeException("No se encontró los datos del NUR ingresado.");

            InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/reporteHoja.jrxml");
            //InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/sample_report.jrxml");
            if (reportStream == null) throw new RuntimeException("No se encontró el reporte: " + "reports/sample_report.jrxml");

            // Compilamos el archivo
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Generamos el codigo de barras
            byte[] barcodeImage = reportService.generateBarcode(nuri);

            String tipo = hojaRuta.getTipoDocumento()==33?"NUR":"NURI";
            // Llenar el reporte con datos
            // Parámetros del reporte
            Map<String, Object> params = new HashMap<>();
            params.put("titulo", "HOJA DE SEGUIMIENTO INTERNO");
            params.put("institucion", "MUTUALIDAD DEL MAGISTERIO NACIONAL");
            params.put("sigla", "MUMANAL");

            params.put("tituloNuri",tipo);
            params.put("nuri", nuri);
            params.put("procedencia", hojaRuta.getProcedencia());
            params.put("remitente", hojaRuta.getRemitente());
            params.put("referencia", hojaRuta.getReferencia());
            params.put("adjuntos", hojaRuta.getAdjuntos());
            //params.put("adjuntos", "Ruben Guancollo Quisbert Ruben Guancollo Quisbert Ruben Guancollo Quisbert Ruben Guancollo Quisbert Ruben Guancollo Quisbert");
            params.put("cite", hojaRuta.getCite());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm", new Locale("es", "ES"));
            // String fechaFormateada = LocalDateTime.now().format(formatter);
            String fechaFormateada = hojaRuta.getFecha().format(formatter);
            params.put("fecha", fechaFormateada.substring(0, 3) + fechaFormateada.substring(3, 4).toUpperCase() + fechaFormateada.substring(4));
            params.put("hojas", hojaRuta.getNroHojas());
            params.put("proceso", hojaRuta.getProceso());
            //params.put("proceso", "Atención al Cliente (comunicación, consultas)");
            params.put("plazo", hojaRuta.getPlazo());
            //params.put("plazo", "RUBEN OSCAR UANCOLLO");
            params.put("proveido", hojaRuta.getProveido());
            params.put("destinatario", hojaRuta.getDestinatario());
            fechaFormateada = hojaRuta.getFechaDerivacion().format(formatter);
            params.put("fderivacion", fechaFormateada.split(" ")[0].substring(0, 3) + fechaFormateada.substring(3, 4).toUpperCase() + fechaFormateada.substring(4));
            params.put("hderivacion", fechaFormateada.split(" ")[1]);
            
            params.put("barcode", barcodeImage);

            List<ModuloDto> par = parametricaService.listarModulos("ACCIONES");
            List<DatosHojaRutaPojo> acciones = new ArrayList<DatosHojaRutaPojo>();

            //List<MyPojo> pojos = new ArrayList<>();

            // Recorrer la lista de DTOs
            String selec = "";
            for (ModuloDto dto : par) {
                //System.out.println(this.convertirACamelCase(dto.getNombre()));
                // Copiar los valores de cada DTO a un nuevo POJO
                if (dto.getIdParametrica().equals(hojaRuta.getIdAccion())) selec = "X";
                else selec = "";

                DatosHojaRutaPojo pojo = new DatosHojaRutaPojo(dto.getIdParametrica(), dto.getNombre(), selec);
                acciones.add(pojo);
            }

            for (DatosHojaRutaPojo reg : acciones) {                
                String variable = this.convertirACamelCase(reg.getNombre());
                // params.put(variable, (reg.getCodigo() != null && !reg.getCodigo().isEmpty() ? reg.getNombre() + "-" + reg.getCodigo() : reg.getNombre()));
                params.put(variable, reg.getNombre());
                variable = variable + "X";
                //System.out.println("Variable: " + variable);
                params.put(variable, reg.getCodigo());
            }
            params.put("vacio", "");
            
            System.out.println(params);

            // // Datos de ejemplo para la lista de documentos
            List<Map<String, Object>> documentos = new ArrayList<>();
            
            Map<String, Object> doc2 = new HashMap<>();
            doc2.put("numero", 2);
            documentos.add(doc2);


            // Convertir la lista a JRDataSource
            JRDataSource dataSource = new JRBeanCollectionDataSource(documentos);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Convertir el reporte a PDF
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            // Establecer los encabezados de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");

            // Devolver la respuesta como un InputStreamResource con el tipo de contenido PDF
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/reporteHojaVacia/**")
    public ResponseEntity<byte[]> generarHojaVacia(HttpServletRequest request){
        try {
            String fullPath = request.getRequestURI();
            String basePath = "/reporteHojaVacia/";
            String nuri = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("NURI: " + nuri);

            DatosHojaRutaDto hojaRuta =  documentosService.datosHojaRutaReimprsion(nuri);
            if(hojaRuta == null) throw new RuntimeException("No se encontró los datos del NUR ingresado.");

            InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/reporteHojaVacia.jrxml");
            //InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/sample_report.jrxml");
            if (reportStream == null) throw new RuntimeException("No se encontró el reporte: " + "reports/sample_report.jrxml");

            // Compilamos el archivo
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> params = new HashMap<>();
            List<ModuloDto> par = parametricaService.listarModulos("ACCIONES");
            List<DatosHojaRutaPojo> acciones = new ArrayList<DatosHojaRutaPojo>();

            //List<MyPojo> pojos = new ArrayList<>();

            String tipo = hojaRuta.getTipoDocumento()==33?"NUR":"NURI";

            // Recorrer la lista de DTOs
            String selec = "";
            for (ModuloDto dto : par) {
                //System.out.println(this.convertirACamelCase(dto.getNombre()));
                // Copiar los valores de cada DTO a un nuevo POJO
                if (dto.getIdParametrica().equals(hojaRuta.getIdAccion())) selec = "X";
                else selec = "";

                DatosHojaRutaPojo pojo = new DatosHojaRutaPojo(dto.getIdParametrica(), dto.getNombre(), selec);
                acciones.add(pojo);
            }

            for (DatosHojaRutaPojo reg : acciones) {                
                String variable = this.convertirACamelCase(reg.getNombre());
                // params.put(variable, (reg.getCodigo() != null && !reg.getCodigo().isEmpty() ? reg.getNombre() + "-" + reg.getCodigo() : reg.getNombre()));
                params.put(variable, reg.getNombre());
                variable = variable + "X";
                //System.out.println("Variable: " + variable);
                params.put(variable, reg.getCodigo());
            }
            params.put("vacio", "");
            params.put("tituloNuri",tipo);
            params.put("nuri", nuri);
            
            System.out.println(params);

            // // Datos de ejemplo para la lista de documentos
            List<Map<String, Object>> documentos = new ArrayList<>();
            
            Map<String, Object> doc2 = new HashMap<>();
            doc2.put("numero", 2);
            documentos.add(doc2);

            // Convertir la lista a JRDataSource
            JRDataSource dataSource = new JRBeanCollectionDataSource(documentos);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Convertir el reporte a PDF
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            // Establecer los encabezados de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");

            // Devolver la respuesta como un InputStreamResource con el tipo de contenido PDF
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/reporteAgrupacion/{nur}/{codigo}")
    public ResponseEntity<byte[]> reporteAgrupacion(@PathVariable String nur,@PathVariable String codigo){
        try {
            String nuri = nur + "/" + codigo;

            List<AgrupacionDto> agrupa =  agrupacionService.reporteAgrupacion(nuri);
            if(agrupa == null) throw new RuntimeException("No se encontró los datos del NUR ingresado.");

            InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/reporteAgrupacion.jrxml");
            //InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/sample_report.jrxml");
            if (reportStream == null) throw new RuntimeException("No se encontró el reporte: " + "reports/reporteAgrupacion.jrxml");

            // Compilamos el archivo
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Generamos el codigo de barras
            byte[] barcodeImage = reportService.generateBarcode(nuri);

            Map<String, Object> params = new HashMap<>();

            params.put("nuri", nuri);
            params.put("barcode", barcodeImage);

             // Convertir la lista a JRDataSource
            JRDataSource dataSource = new JRBeanCollectionDataSource(agrupa);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Convertir el reporte a PDF
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            // Establecer los encabezados de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporteAgrupado-" + nuri.replace("/", "-") + ".pdf");

            //actualizamos los estados
            AgrupacionEditPojo pojo = new AgrupacionEditPojo();
            pojo.setNuri(nuri);
            pojo.setModificadoPor("SIS");
            int res = agrupacionService.actualizarEstados(pojo);

            if(res >= 1) {
                // Devolver la respuesta como un InputStreamResource con el tipo de contenido PDF
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .contentLength(pdfBytes.length)
                        .body(pdfBytes);
            }else {
                throw new RuntimeException("No se genero el reporte los datos del NUR ingresado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "/reporteHojaReimpresion/**")
    public ResponseEntity<byte[]> reporteHojaReimpresion(HttpServletRequest request) {
        try {
            String fullPath = request.getRequestURI();
            String basePath = "/reporteHojaReimpresion/";
            String nuri = fullPath.substring(fullPath.indexOf(basePath) + basePath.length());
            System.out.println("NURI: " + nuri);

            DatosHojaRutaDto hojaRuta =  documentosService.datosHojaRutaReimprsion(nuri);
            if(hojaRuta == null) throw new RuntimeException("No se encontró los datos del NUR ingresado.");

            InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/reporteHoja.jrxml");
            //InputStream reportStream = getClass().getClassLoader().getResourceAsStream("reports/sample_report.jrxml");
            if (reportStream == null) throw new RuntimeException("No se encontró el reporte: " + "reports/sample_report.jrxml");

            // Compilamos el archivo
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Generamos el codigo de barras
            byte[] barcodeImage = reportService.generateBarcode(nuri);

            String tipo = hojaRuta.getTipoDocumento()==33?"NUR":"NURI";
            // Llenar el reporte con datos
            // Parámetros del reporte
            Map<String, Object> params = new HashMap<>();
            params.put("titulo", "HOJA DE SEGUIMIENTO INTERNO");
            params.put("institucion", "MUTUALIDAD DEL MAGISTERIO NACIONAL");
            params.put("sigla", "MUMANAL");

            params.put("tituloNuri",tipo);
            params.put("nuri", nuri);
            params.put("procedencia", hojaRuta.getProcedencia());
            params.put("remitente", hojaRuta.getRemitente());
            params.put("referencia", hojaRuta.getReferencia());
            params.put("adjuntos", hojaRuta.getAdjuntos());
            //params.put("adjuntos", "Ruben Guancollo Quisbert Ruben Guancollo Quisbert Ruben Guancollo Quisbert Ruben Guancollo Quisbert Ruben Guancollo Quisbert");
            params.put("cite", hojaRuta.getCite());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm", new Locale("es", "ES"));
            // String fechaFormateada = LocalDateTime.now().format(formatter);
            String fechaFormateada = hojaRuta.getFecha().format(formatter);
            params.put("fecha", fechaFormateada.substring(0, 3) + fechaFormateada.substring(3, 4).toUpperCase() + fechaFormateada.substring(4));
            params.put("hojas", hojaRuta.getNroHojas());
            params.put("proceso", hojaRuta.getProceso());
            //params.put("proceso", "Atención al Cliente (comunicación, consultas)");
            params.put("plazo", hojaRuta.getPlazo());
            //params.put("plazo", "RUBEN OSCAR UANCOLLO");
            params.put("proveido", hojaRuta.getProveido());
            params.put("destinatario", hojaRuta.getDestinatario());
            fechaFormateada = hojaRuta.getFechaDerivacion().format(formatter);
            params.put("fderivacion", fechaFormateada.split(" ")[0].substring(0, 3) + fechaFormateada.substring(3, 4).toUpperCase() + fechaFormateada.substring(4));
            params.put("hderivacion", fechaFormateada.split(" ")[1]);
            
            params.put("barcode", barcodeImage);

            List<ModuloDto> par = parametricaService.listarModulos("ACCIONES");
            List<DatosHojaRutaPojo> acciones = new ArrayList<DatosHojaRutaPojo>();

            //List<MyPojo> pojos = new ArrayList<>();

            // Recorrer la lista de DTOs
            String selec = "";
            for (ModuloDto dto : par) {
                //System.out.println(this.convertirACamelCase(dto.getNombre()));
                // Copiar los valores de cada DTO a un nuevo POJO
                if (dto.getIdParametrica().equals(hojaRuta.getIdAccion())) selec = "X";
                else selec = "";

                DatosHojaRutaPojo pojo = new DatosHojaRutaPojo(dto.getIdParametrica(), dto.getNombre(), selec);
                acciones.add(pojo);
            }

            for (DatosHojaRutaPojo reg : acciones) {                
                String variable = this.convertirACamelCase(reg.getNombre());
                // params.put(variable, (reg.getCodigo() != null && !reg.getCodigo().isEmpty() ? reg.getNombre() + "-" + reg.getCodigo() : reg.getNombre()));
                params.put(variable, reg.getNombre());
                variable = variable + "X";
                //System.out.println("Variable: " + variable);
                params.put(variable, reg.getCodigo());
            }
            params.put("vacio", "");
            
            System.out.println(params);

            // // Datos de ejemplo para la lista de documentos
            List<Map<String, Object>> documentos = new ArrayList<>();
            
            Map<String, Object> doc2 = new HashMap<>();
            doc2.put("numero", 2);
            documentos.add(doc2);


            // Convertir la lista a JRDataSource
            JRDataSource dataSource = new JRBeanCollectionDataSource(documentos);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Convertir el reporte a PDF
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            // Establecer los encabezados de la respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");

            // Devolver la respuesta como un InputStreamResource con el tipo de contenido PDF
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public String convertirACamelCase(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
    
        String[] palabras = texto.split(" ");
        StringBuilder resultado = new StringBuilder(palabras[0].toLowerCase());
    
        for (int i = 1; i < palabras.length; i++) {
            resultado.append(palabras[i].substring(0,1).toUpperCase())
                     .append(palabras[i].substring(1).toLowerCase());
        }
    
        return resultado.toString();
    }
}
