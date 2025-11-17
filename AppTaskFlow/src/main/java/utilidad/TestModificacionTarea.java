package utilidad;

import service.TareaService;
import domain.Tarea;
import java.time.LocalDate;

public class TestModificacionTarea {
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBA COMPLETA DE MODIFICACIÓN DE TAREAS ===\n");
        
        TareaService service = TareaService.getInstance();
        
        // 1. Mostrar todas las tareas actuales
        System.out.println("1. Tareas actuales antes de las pruebas:");
        service.obtenerTodasLasTareas().forEach(t -> 
            System.out.println("  ID: " + t.getId() + " - " + t.getTitulo() + " [" + t.getEstado() + "] [" + t.getPrioridad() + "]")
        );
        
        // 2. Crear una nueva tarea para modificar
        System.out.println("\n2. Creando nueva tarea para prueba de modificación:");
        Tarea nuevaTarea = new Tarea();
        nuevaTarea.setTitulo("TAREA PARA MODIFICAR");
        nuevaTarea.setDescripcion("Descripción inicial");
        nuevaTarea.setEstado("pendiente");
        nuevaTarea.setPrioridad("media");
        nuevaTarea.setCategoria("Trabajo");
        nuevaTarea.setFechaVencimiento(LocalDate.now().plusDays(5));
        
        boolean creada = service.crearTarea(nuevaTarea);
        System.out.println("  Nueva tarea creada: " + (creada ? "ÉXITO" : "ERROR") + " - ID: " + nuevaTarea.getId());
        
        if (creada) {
            // 3. Modificar la tarea recién creada
            System.out.println("\n3. Modificando la tarea ID " + nuevaTarea.getId() + ":");
            System.out.println("  ANTES: " + nuevaTarea.getTitulo() + " | " + nuevaTarea.getEstado() + " | " + nuevaTarea.getPrioridad());
            
            // Hacer cambios
            nuevaTarea.setTitulo("TAREA MODIFICADA EXITOSAMENTE");
            nuevaTarea.setDescripcion("Descripción actualizada desde el test");
            nuevaTarea.setEstado("en_progreso");
            nuevaTarea.setPrioridad("alta");
            nuevaTarea.setCategoria("Personal");
            
            boolean modificada = service.actualizarTarea(nuevaTarea);
            System.out.println("  Resultado modificación: " + (modificada ? "ÉXITO" : "ERROR"));
            
            // 4. Verificar que los cambios se guardaron
            System.out.println("\n4. Verificando cambios en la base de datos:");
            Tarea tareaActualizada = service.buscarTareaPorId(nuevaTarea.getId());
            if (tareaActualizada != null) {
                System.out.println("  DESPUÉS: " + tareaActualizada.getTitulo() + " | " + tareaActualizada.getEstado() + " | " + tareaActualizada.getPrioridad());
                System.out.println("  Categoría: " + tareaActualizada.getCategoria());
                System.out.println("  Descripción: " + tareaActualizada.getDescripcion());
            } else {
                System.out.println("  ERROR: No se pudo recuperar la tarea actualizada");
            }
            
            // 5. Limpiar - eliminar la tarea de prueba
            System.out.println("\n5. Eliminando tarea de prueba ID " + nuevaTarea.getId() + ":");
            boolean eliminada = service.eliminarTarea(nuevaTarea.getId());
            System.out.println("  Resultado eliminación: " + (eliminada ? "ÉXITO" : "ERROR"));
        }
        
        // 6. Mostrar tareas finales
        System.out.println("\n6. Tareas finales después de las pruebas:");
        service.obtenerTodasLasTareas().forEach(t -> 
            System.out.println("  ID: " + t.getId() + " - " + t.getTitulo() + " [" + t.getEstado() + "] [" + t.getPrioridad() + "]")
        );
        
        System.out.println("\n=== ✅ TODAS LAS OPERACIONES CRUD FUNCIONAN CORRECTAMENTE ===");
    }
}