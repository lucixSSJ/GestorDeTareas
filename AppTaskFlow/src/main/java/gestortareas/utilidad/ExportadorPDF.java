package gestortareas.utilidad;

import gestortareas.model.Tarea;
import gestortareas.utilidad.exportacion.ExportadorFactory;
import gestortareas.utilidad.exportacion.ExportadorStrategy;
import gestortareas.utilidad.exportacion.TipoExportacion;

import java.util.List;

public class ExportadorPDF {

    /**
     * Exporta tareas a PDF usando el patrón Factory Method
     * @param tareas Lista de tareas a exportar
     */
    public static void exportarTareasPDF(List<Tarea> tareas) {
        // Usa Factory Method Pattern para crear el exportador
        ExportadorStrategy exportador = ExportadorFactory.crearExportador(TipoExportacion.PDF);
        exportador.exportar(tareas);
    }
}
