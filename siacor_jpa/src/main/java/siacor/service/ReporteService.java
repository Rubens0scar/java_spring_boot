package siacor.service;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashMap;

import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;

@Service
public class ReporteService {
    private static final String REPORTS_PATH = "src/main/resources/reports/";

    public byte[] generateBarcode(String code) throws Exception {
        // Crear mapa de configuraciones para el generador de códigos de barras
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 2);  // Establecer margen del código de barras

        // Generar el código de barras como un BitMatrix
        BitMatrix matrix = new MultiFormatWriter().encode(code, BarcodeFormat.CODE_39, 200, 100, hints);

        // Convertir el BitMatrix a BufferedImage
        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF); // Establecer el color del píxel
            }
        }

        // Convertir BufferedImage a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

    public void generateAndSaveReport() throws JRException, FileNotFoundException {
        // 1️⃣ Obtener el archivo .jasper desde resources/reports/
        File file = ResourceUtils.getFile(REPORTS_PATH + "sample_report.jasper");

        // 2️⃣ Llenar el reporte con parámetros (puedes añadir más si lo necesitas)
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, new JREmptyDataSource());

        // 3️⃣ Definir la ruta para guardar el PDF
        String outputPath = REPORTS_PATH + "reporte_generado.pdf";
        
        // 4️⃣ Crear la carpeta si no existe
        try {
            Files.createDirectories(Paths.get(REPORTS_PATH));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el directorio de reportes.", e);
        }

        // 5️⃣ Exportar el PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

        System.out.println("📄 Reporte generado y guardado en: " + outputPath);
    }
}
