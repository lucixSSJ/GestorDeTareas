package utilidad;

import dao.impl.JdbcTareaDao;
import domain.Tarea;
import java.time.LocalDate;

public class TestOperacionesTarea {
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE OPERACIONES CRUD ===\n");
        
        JdbcTareaDao dao = JdbcTareaDao.getInstance();
        
        // 1. Mostrar todas las tareas actuales
        System.out.println("1. Tareas actuales:");
        dao.obtenerTodas().forEach(t -> 
            System.out.println("  ID: " + t.getId() + " - " + t.getTitulo() + " [" + t.getEstado() + "]")
        );
        
        // 2. Buscar una tarea específica para modificar
        System.out.println("\n2. Buscando tarea con ID 1:");
        Tarea tarea = dao.buscarPorId(1);
        if (tarea != null) {
            System.out.println("  Encontrada: " + tarea.getTitulo());
            
            // 3. Modificar la tarea
            System.out.println("\n3. Modificando tarea ID 1:");
            tarea.setTitulo("Reunión ACTUALIZADA - " + java.time.LocalDateTime.now().toString().substring(11, 19));
            tarea.setDescripcion("Descripción actualizada desde Java");
            tarea.setEstado("en_progreso");
            tarea.setPrioridad("alta");
            
            boolean actualizado = dao.actualizar(tarea);
            System.out.println("  Resultado actualización: " + (actualizado ? "ÉXITO" : "ERROR"));
            
        } else {
            System.out.println("  No se encontró tarea con ID 1");
        }
        
        // 4. Crear una nueva tarea para luego eliminarla
        System.out.println("\n4. Creando nueva tarea para prueba de eliminación:");
        Tarea nuevaTarea = new Tarea();
        nuevaTarea.setTitulo("TAREA DE PRUEBA - ELIMINAR");
        nuevaTarea.setDescripcion("Esta tarea será eliminada inmediatamente");
        nuevaTarea.setEstado("pendiente");
        nuevaTarea.setPrioridad("baja");
        nuevaTarea.setFechaVencimiento(LocalDate.now().plusDays(1));
        
        boolean guardada = dao.guardar(nuevaTarea);
        System.out.println("  Nueva tarea creada con ID: " + nuevaTarea.getId() + " - " + (guardada ? "ÉXITO" : "ERROR"));
        
        // 5. Eliminar la tarea recién creada
        if (guardada && nuevaTarea.getId() > 0) {
            System.out.println("\n5. Eliminando tarea ID " + nuevaTarea.getId() + ":");
            boolean eliminada = dao.eliminar(nuevaTarea.getId());
            System.out.println("  Resultado eliminación: " + (eliminada ? "ÉXITO" : "ERROR"));
        }
        
        // 6. Mostrar tareas finales
        System.out.println("\n6. Tareas después de las operaciones:");
        dao.obtenerTodas().forEach(t -> 
            System.out.println("  ID: " + t.getId() + " - " + t.getTitulo() + " [" + t.getEstado() + "]")
        );
        
        System.out.println("\n=== FIN DE PRUEBAS ===");
    }
}