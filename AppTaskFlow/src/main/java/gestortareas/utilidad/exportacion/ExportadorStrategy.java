package gestortareas.utilidad.exportacion;

import gestortareas.model.Tarea;

import java.util.List;

public interface ExportadorStrategy {
    /**
     * Exporta una lista de tareas usando la estrategia específica
     * @param tareas Lista de tareas a exportar
     */
    void exportar(List<Tarea> tareas);

    /**
     * Obtiene la extensión del archivo para esta estrategia
     * @return Extensión del archivo (ej: ".pdf", ".ics")
     */
    String getExtension();

    /**
     * Obtiene la descripción del tipo de exportación
     * @return Descripción del tipo (ej: "PDF", "ICS")
     */
    String getDescripcion();
}
