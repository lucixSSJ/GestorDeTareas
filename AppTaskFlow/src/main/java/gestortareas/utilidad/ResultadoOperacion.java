package gestortareas.utilidad;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Michael
 * @param <T>
 */
public class ResultadoOperacion<T> {
    private final boolean isExitoso;
    private final String mensaje;
    private final int tipoMensaje;
    private final T datos;

    private ResultadoOperacion(boolean isExitoso, String mensaje, int tipoMensaje, T datos) {
        this.isExitoso = isExitoso;
        this.mensaje = mensaje;
        this.tipoMensaje = tipoMensaje;
        this.datos = datos;
    }
    
    public static <T> ResultadoOperacion<T> exito(String mensaje, T datos) {
        return new ResultadoOperacion<>(true, mensaje, JOptionPane.INFORMATION_MESSAGE, datos);
    }

    public static <T> ResultadoOperacion<T> error(String mensaje) {
        return new ResultadoOperacion<>(false, mensaje, JOptionPane.ERROR_MESSAGE, null);
    }

    public static <T> ResultadoOperacion<T> advertencia(String mensaje) {
        return new ResultadoOperacion<>(false, mensaje, JOptionPane.WARNING_MESSAGE, null);
    }

    public boolean esExitoso() { return isExitoso; }
    public String getMensaje() { return mensaje; }
    public T getDatos() { return datos; }
    
    public void mostrarDialogo(Component parent) {
        JOptionPane.showMessageDialog(parent, mensaje, "Gestión de Tareas", tipoMensaje);
    }
}
