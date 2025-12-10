package gestortareas.utilidad.exportacion;

import gestortareas.model.Tarea;
import javax.swing.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.io.font.constants.StandardFonts;

public class PDFExportadorStrategy implements ExportadorStrategy{

    @Override
    public void exportar(List<Tarea> tareas) {
        if (tareas == null || tareas.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay tareas para exportar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar tareas como " + getDescripcion());

        String nombreArchivo = tareas.size() == 1 ?
                sanitizarNombreArchivo(tareas.get(0).getNombre()) :
                "tareas_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

        fileChooser.setSelectedFile(new java.io.File(nombreArchivo + getExtension()));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.toLowerCase().endsWith(getExtension())) {
                filePath += getExtension();
            }

            try {
                generarPDF(tareas, filePath);
                String mensaje = tareas.size() == 1 ?
                        "Tarea exportada exitosamente a:\n" + filePath :
                        tareas.size() + " tareas exportadas exitosamente a:\n" + filePath;
                JOptionPane.showMessageDialog(null,
                        mensaje,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error al exportar las tareas: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getExtension() {
        return ".pdf";
    }

    @Override
    public String getDescripcion() {
        return "PDF";
    }

    private void generarPDF(List<Tarea> tareas, String filePath) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Crear el documento PDF
        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Fuentes
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Título principal
        Paragraph titulo = new Paragraph("TaskFlow - Exportación de Tareas")
                .setFont(boldFont)
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(44, 62, 80));
        document.add(titulo);

        // Resumen
        Paragraph resumen = new Paragraph()
                .setFont(regularFont)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER)
                .add("Total de tareas exportadas: " + tareas.size() + "\n")
                .add("Fecha de exportación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()) + "\n")
                .add("Generado mediante Factory Method Pattern + Strategy Pattern")
                .setFontColor(ColorConstants.DARK_GRAY);
        document.add(resumen);

        document.add(new Paragraph("\n"));

        // Procesar cada tarea
        for (int i = 0; i < tareas.size(); i++) {
            Tarea tarea = tareas.get(i);

            // Título de la tarea
            Paragraph tareaNumero = new Paragraph((i + 1) + ". " + (tarea.getNombre() != null ? tarea.getNombre() : "Sin nombre"))
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setFontColor(new DeviceRgb(41, 128, 185));
            document.add(tareaNumero);

            // Tabla con información de la tarea
            Table table = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                    .useAllAvailableWidth();

            // Descripción
            table.addCell(createHeaderCell("Descripción:", boldFont));
            table.addCell(createValueCell(tarea.getDescripcion() != null ? tarea.getDescripcion() : "Sin descripción", regularFont));

            // Fecha límite
            table.addCell(createHeaderCell("Fecha Límite:", boldFont));
            table.addCell(createValueCell(
                    tarea.getFechaLimite() != null ? sdf.format(tarea.getFechaLimite()) : "No definida",
                    regularFont));

            // Prioridad
            table.addCell(createHeaderCell("Prioridad:", boldFont));
            Cell prioridadCell = createValueCell(
                    tarea.getPrioridad() != null ? tarea.getPrioridad() : "No definida",
                    boldFont);

            // Color según prioridad
            if (tarea.getPrioridad() != null) {
                if (tarea.getPrioridad().equalsIgnoreCase("ALTA")) {
                    prioridadCell.setBackgroundColor(new DeviceRgb(231, 76, 60))
                            .setFontColor(ColorConstants.WHITE);
                } else if (tarea.getPrioridad().equalsIgnoreCase("MEDIA")) {
                    prioridadCell.setBackgroundColor(new DeviceRgb(243, 156, 18))
                            .setFontColor(ColorConstants.WHITE);
                } else if (tarea.getPrioridad().equalsIgnoreCase("BAJA")) {
                    prioridadCell.setBackgroundColor(new DeviceRgb(39, 174, 96))
                            .setFontColor(ColorConstants.WHITE);
                }
            }
            table.addCell(prioridadCell);

            // Estado
            table.addCell(createHeaderCell("Estado:", boldFont));
            Cell estadoCell = createValueCell(
                    tarea.getEstado() != null ? tarea.getEstado() : "No definido",
                    boldFont)
                    .setBackgroundColor(new DeviceRgb(52, 152, 219))
                    .setFontColor(ColorConstants.WHITE);
            table.addCell(estadoCell);

            // Categoría
            if (tarea.getCategoria() != null) {
                table.addCell(createHeaderCell("Categoría:", boldFont));
                table.addCell(createValueCell(tarea.getCategoria().getNombre(), regularFont));
            }

            document.add(table);
            document.add(new Paragraph("\n"));
        }

        // Footer
        Paragraph footer = new Paragraph("Generado por TaskFlow - Sistema de Gestión de Tareas\n" +
                "Exportación mediante Factory Method Pattern + Strategy Pattern")
                .setFont(regularFont)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);
        document.add(footer);

        // Cerrar documento
        document.close();
    }

    private Cell createHeaderCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font))
                .setBackgroundColor(new DeviceRgb(236, 240, 241))
                .setPadding(8);
    }

    private Cell createValueCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font))
                .setPadding(8);
    }

    private String sanitizarNombreArchivo(String nombre) {
        if (nombre == null) return "tarea";
        return nombre.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
