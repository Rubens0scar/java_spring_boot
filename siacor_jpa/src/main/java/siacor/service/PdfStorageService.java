package siacor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import siacor.model.pojo.PdfResponse;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.stream.Stream;

//import javax.imageio.ImageIO;

@Service
public class PdfStorageService {
    //private static final String BASE_DIR = "D:/archivosSiacor"; // Reemplaza con tu ruta base
    private static final String BASE_DIR = "/home/gguzman/archivosSiacor"; // Reemplaza con tu ruta base
    private static final String YEAR_FOLDER = Integer.toString(LocalDateTime.now().getYear());
    //private static final String gsPath = "C:/Program Files/gs/gs10.05.1/bin/gswin64c.exe";
    private static final String gsPath = "/usr/bin/gs";

    public PdfResponse guardarPdfConCompresion(String nombreSubcarpeta, MultipartFile archivoPdf) throws Exception {
        // Validar
        String nombreArchivo = archivoPdf.getOriginalFilename();
        if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".pdf")) throw new IllegalArgumentException("El archivo debe tener extensión .pdf");

        if (!archivoPdf.getContentType().equalsIgnoreCase("application/pdf")) throw new IllegalArgumentException("El contenido del archivo no es PDF");

        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);

        // Crear carpetas
        Path yearPath = Paths.get(BASE_DIR, YEAR_FOLDER);
        Files.createDirectories(yearPath);

        Path destinoCarpeta = yearPath.resolve(nombreSubcarpeta);
        Files.createDirectories(destinoCarpeta);

        // Temporal
        File tempOriginal = File.createTempFile("original_", ".pdf");
        archivoPdf.transferTo(tempOriginal);
        long tamañoOriginalKB = tempOriginal.length() / 1024;

        // Destino
        File archivoDestino = destinoCarpeta.resolve(nombreArchivo).toFile();
        //comprimirPdfConGhost4J(tempOriginal, archivoDestino);
        comprimirPdfConGhostscript(tempOriginal, archivoDestino);
        long tamañoComprimidoKB = archivoDestino.length() / 1024;

        if(tamañoComprimidoKB > 30720) throw new IllegalArgumentException("El tamaño del archivo comprimido supera el tamaño admitido de 30Mb, por favor verifique el archivo ORIGINAL.");

        tempOriginal.delete();

        return new PdfResponse(
            nombreSubcarpeta.replace("_", "/"),
            nombreArchivo,
            extension,
            tamañoOriginalKB,
            tamañoComprimidoKB,
            archivoDestino.getAbsolutePath()
        );
    }

    public void comprimirPdfConGhostscript(File original, File destino) throws IOException, InterruptedException {   
        // Comando de Ghostscript para comprimir el PDF
        // List<String> comando = Arrays.asList(
        //     gsPath,
        //     "-sDEVICE=pdfwrite", // Especifica que el dispositivo de salida es PDF
        //     "-dCompatibilityLevel=1.4", // Compatible con PDF 1.4
        //     "-dPDFSETTINGS=/ebook", // Configura la compresión (puedes probar con /screen, /ebook, etc.)
        //     "-dNOPAUSE", // Evita pausas entre páginas
        //     "-dQUIET", // Evita mostrar información adicional
        //     "-dBATCH", // Procesa todos los archivos en el comando
        //     "-sOutputFile=" + destino.getAbsolutePath(), // Archivo de salida
        //     original.getAbsolutePath() // Archivo de entrada
        // );

        List<String> comando = Arrays.asList(
            gsPath,
            "-sDEVICE=pdfwrite", // Especifica que el dispositivo de salida es PDF
            "-dCompatibilityLevel=1.4", // Compatible con PDF 1.4
            "-dPDFSETTINGS=/ebook", // Ajuste de calidad /ebook para mantener un buen equilibrio
            "-dNOPAUSE", // Evita pausas entre páginas
            "-dQUIET", // Evita mostrar información adicional
            "-dBATCH", // Procesa todos los archivos en el comando
            "-dDownsampleColorImages=true", // Permite la reducción de resolución de las imágenes a color
            "-dDownsampleGrayImages=true", // Permite la reducción de resolución de las imágenes en escala de grises
            "-dColorImageResolution=115", // Resolución para imágenes en color (ajustado a 120 DPI)
            "-dGrayImageResolution=115", // Resolución para imágenes en escala de grises (ajustado a 120 DPI)
            "-sOutputFile=" + destino.getAbsolutePath(), // Archivo de salida
            original.getAbsolutePath() // Archivo de entrada
        );
    
        // Crear un ProcessBuilder para ejecutar el comando de Ghostscript
        ProcessBuilder pb = new ProcessBuilder(comando);
        Process proceso = pb.start();
        int codigoSalida = proceso.waitFor(); // Esperar a que el proceso termine
    
        if (codigoSalida != 0) {
            throw new RuntimeException("Ghostscript falló con código: " + codigoSalida);
        }
    }
    //comprimir pdf copn imagenes
    // private void comprimirPdfConGhost4J(File original, File destino) throws Exception {
    //     // Cargar el PDF
    //     PDFDocument doc = new PDFDocument();
    //     doc.load(original);

    //     // Configurar el renderer
    //     SimpleRenderer renderer = new SimpleRenderer();
    //     renderer.setResolution(72); // Menor resolución = menor peso

    //     // Renderizar a java.awt.Image
    //     List<java.awt.Image> awtImages = renderer.render(doc);

    //     // Convertir a BufferedImage
    //     List<BufferedImage> bufferedImages = new ArrayList<>();
    //     for (java.awt.Image awtImg : awtImages) {
    //         BufferedImage buffered = new BufferedImage(
    //             awtImg.getWidth(null),
    //             awtImg.getHeight(null),
    //             BufferedImage.TYPE_INT_RGB
    //         );
    //         buffered.getGraphics().drawImage(awtImg, 0, 0, null);
    //         bufferedImages.add(buffered);
    //     }

    //     // Crear nuevo PDF con iText
    //     Document nuevoDoc = new Document();
    //     PdfWriter.getInstance(nuevoDoc, new FileOutputStream(destino));
    //     nuevoDoc.open();

    //     // for (BufferedImage img : bufferedImages) {
    //     //     ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //     //     ImageIO.write(img, "jpg", baos);
    //     //     Image imagen = Image.getInstance(baos.toByteArray());
    //     //     imagen.scaleToFit(nuevoDoc.getPageSize().getWidth(), nuevoDoc.getPageSize().getHeight());
    //     //     nuevoDoc.add(imagen);
    //     // }
    //     for (BufferedImage img : bufferedImages) {
    //         ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //         ImageIO.write(img, "jpg", baos);
    //         Image imagen = Image.getInstance(baos.toByteArray());
        
    //         // 🔁 Crear una nueva página con el tamaño de la imagen original
    //         float width = img.getWidth();
    //         float height = img.getHeight();
        
    //         nuevoDoc.setPageSize(new com.itextpdf.text.Rectangle(width, height));
    //         nuevoDoc.newPage(); // Importante: nueva página con tamaño actualizado
        
    //         imagen.setAbsolutePosition(0, 0);
    //         imagen.scaleToFit(width, height);
    //         nuevoDoc.add(imagen);
    //     }

    //     nuevoDoc.close();
    // }


    public boolean eliminarArchivo(String carpeta, String nombreArchivo) {
        try {
            // Construye la ruta completa
            Path rutaArchivo = Paths.get(BASE_DIR, YEAR_FOLDER, carpeta, nombreArchivo);
    
            // Verifica si existe el archivo
            if (Files.exists(rutaArchivo)) {
                Files.delete(rutaArchivo); // Elimina el archivo
                return true;
            } else {
                return false; // No existe el archivo
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listarArchivos(String carpeta) throws IOException {
        Path ruta = Paths.get(BASE_DIR, YEAR_FOLDER, carpeta);
        if (!Files.exists(ruta) || !Files.isDirectory(ruta)) {
            throw new IllegalArgumentException("La carpeta no existe: " + ruta);
        }

        try (Stream<Path> stream = Files.list(ruta)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    public Path obtenerArchivo(String carpeta, String archivo) {
        return Paths.get(BASE_DIR, YEAR_FOLDER, carpeta, archivo);
    }

}
