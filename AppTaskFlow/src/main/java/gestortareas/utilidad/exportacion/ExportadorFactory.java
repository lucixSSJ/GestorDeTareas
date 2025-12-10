package gestortareas.utilidad.exportacion;

public class ExportadorFactory {
    public static ExportadorStrategy crearExportador(TipoExportacion tipo) {
        switch (tipo) {
            case PDF:
                return new PDFExportadorStrategy();
            case ICS:
                return new ICSExportadorStrategy();
            default:
                throw new IllegalArgumentException("Tipo de exportación no soportado: " + tipo);
        }
    }

    /**
     * Método alternativo que recibe un String para mayor flexibilidad
     *
     * @param tipoStr Tipo de exportación como String ("PDF", "ICS")
     * @return Exportador específico que implementa ExportadorStrategy
     * @throws IllegalArgumentException Si el tipo no es válido
     */
    public static ExportadorStrategy crearExportador(String tipoStr) {
        try {
            TipoExportacion tipo = TipoExportacion.valueOf(tipoStr.toUpperCase());
            return crearExportador(tipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de exportación no válido: " + tipoStr +
                    ". Tipos válidos: PDF, ICS");
        }
    }
}
