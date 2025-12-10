package gestortareas.utilidad.exportacion;

import gestortareas.model.Tarea;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class ICSExportadorStrategy implements ExportadorStrategy{

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
                generarICS(tareas, filePath);
                String mensaje = tareas.size() == 1 ?
                        "Tarea exportada exitosamente a:\n" + filePath + "\n\nPuedes importar este archivo en Google Calendar, Outlook u otras aplicaciones de calendario." :
                        tareas.size() + " tareas exportadas exitosamente a:\n" + filePath + "\n\nPuedes importar este archivo en Google Calendar, Outlook u otras aplicaciones de calendario.";
                JOptionPane.showMessageDialog(null,
                        mensaje,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Error al exportar las tareas: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public String getExtension() {
        return ".ics";
    }

    @Override
    public String getDescripcion() {
        return "ICS (iCalendar)";
    }

    private void generarICS(List<Tarea> tareas, String filePath) throws IOException {
        SimpleDateFormat sdfICS = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        sdfICS.setTimeZone(TimeZone.getTimeZone("UTC"));

        StringBuilder ics = new StringBuilder();

        // Encabezado ICS
        ics.append("BEGIN:VCALENDAR\r\n");
        ics.append("VERSION:2.0\r\n");
        ics.append("PRODID:-//TaskFlow//Gestor de Tareas//ES\r\n");
        ics.append("CALSCALE:GREGORIAN\r\n");
        ics.append("METHOD:PUBLISH\r\n");
        ics.append("X-WR-CALNAME:Tareas - TaskFlow\r\n");
        ics.append("X-WR-CALDESC:Tareas exportadas desde TaskFlow usando Factory Method Pattern\r\n");

        // Iterar sobre cada tarea y crear un evento VEVENT para cada una
        for (Tarea tarea : tareas) {
            agregarEventoICS(tarea, ics, sdfICS);
        }

        // Fin del calendario
        ics.append("END:VCALENDAR\r\n");

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(ics.toString());
        }
    }

    private void agregarEventoICS(Tarea tarea, StringBuilder ics, SimpleDateFormat sdfICS) {
        ics.append("BEGIN:VEVENT\r\n");

        ics.append("UID:tarea-").append(tarea.getIdTarea()).append("-").append(System.currentTimeMillis()).append("@taskflow.com\r\n");
        ics.append("DTSTAMP:").append(sdfICS.format(new java.util.Date())).append("Z\r\n");

        if (tarea.getFechaLimite() != null) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
            ics.append("DTSTART;VALUE=DATE:").append(sdfDate.format(tarea.getFechaLimite())).append("\r\n");
            ics.append("DTEND:").append(sdfICS.format(tarea.getFechaLimite())).append("Z\r\n");

            if (tarea.getRecordatorio() > 0) {
                ics.append("BEGIN:VALARM\r\n");
                ics.append("TRIGGER:-PT").append(tarea.getRecordatorio()).append("M\r\n");
                ics.append("DESCRIPTION:Recordatorio de tarea\r\n");
                ics.append("ACTION:DISPLAY\r\n");
                ics.append("END:VALARM\r\n");
            }
        } else {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
            ics.append("DTSTART;VALUE=DATE:").append(sdfDate.format(new java.util.Date())).append("\r\n");
        }

        ics.append("SUMMARY:").append(escaparICS(tarea.getNombre())).append("\r\n");

        StringBuilder descripcion = new StringBuilder();
        if (tarea.getDescripcion() != null && !tarea.getDescripcion().isEmpty()) {
            descripcion.append(escaparICS(tarea.getDescripcion()));
        }

        if (tarea.getPrioridad() != null) {
            descripcion.append("\\n\\nPrioridad: ").append(escaparICS(tarea.getPrioridad()));
        }
        if (tarea.getEstado() != null) {
            descripcion.append("\\nEstado: ").append(escaparICS(tarea.getEstado()));
        }
        if (tarea.getCategoria() != null) {
            descripcion.append("\\nCategoría: ").append(escaparICS(tarea.getCategoria().getNombre()));
        }

        ics.append("DESCRIPTION:").append(descripcion.toString()).append("\r\n");

        if (tarea.getCategoria() != null) {
            ics.append("CATEGORIES:").append(escaparICS(tarea.getCategoria().getNombre())).append("\r\n");
        }

        int prioridadICS = 5;
        if (tarea.getPrioridad() != null) {
            if (tarea.getPrioridad().equalsIgnoreCase("ALTA")) {
                prioridadICS = 1;
            } else if (tarea.getPrioridad().equalsIgnoreCase("BAJA")) {
                prioridadICS = 9;
            }
        }
        ics.append("PRIORITY:").append(prioridadICS).append("\r\n");

        String statusICS = "NEEDS-ACTION";
        if (tarea.getEstado() != null) {
            if (tarea.getEstado().equalsIgnoreCase("COMPLETADA")) {
                statusICS = "COMPLETED";
            } else if (tarea.getEstado().equalsIgnoreCase("EN_PROGRESO")) {
                statusICS = "IN-PROCESS";
            } else if (tarea.getEstado().equalsIgnoreCase("CANCELADA")) {
                statusICS = "CANCELLED";
            }
        }
        ics.append("STATUS:").append(statusICS).append("\r\n");

        ics.append("END:VEVENT\r\n");
    }

    private String escaparICS(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                .replace(",", "\\,")
                .replace(";", "\\;")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private String sanitizarNombreArchivo(String nombre) {
        if (nombre == null) return "tarea";
        return nombre.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
