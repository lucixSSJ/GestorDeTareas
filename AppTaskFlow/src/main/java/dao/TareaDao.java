/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import domain.Tarea;
import java.util.List;

/**
 * Interface para operaciones CRUD de Tarea
 * @author Luciano
 */
public interface TareaDao {
    
    /**
     * Guarda una nueva tarea
     * @param tarea La tarea a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     */
    boolean guardar(Tarea tarea);
    
    /**
     * Actualiza una tarea existente
     * @param tarea La tarea a actualizar
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    boolean actualizar(Tarea tarea);
    
    /**
     * Elimina una tarea por su ID
     * @param id El ID de la tarea a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     */
    boolean eliminar(int id);
    
    /**
     * Busca una tarea por su ID
     * @param id El ID de la tarea a buscar
     * @return La tarea encontrada, null si no existe
     */
    Tarea buscarPorId(int id);
    
    /**
     * Obtiene todas las tareas
     * @return Lista de todas las tareas
     */
    List<Tarea> obtenerTodas();
    
    /**
     * Busca tareas por categoría
     * @param categoria La categoría a buscar
     * @return Lista de tareas de la categoría especificada
     */
    List<Tarea> buscarPorCategoria(String categoria);
    
    /**
     * Busca tareas por estado
     * @param estado El estado a buscar
     * @return Lista de tareas con el estado especificado
     */
    List<Tarea> buscarPorEstado(String estado);
    
    /**
     * Busca tareas por prioridad
     * @param prioridad La prioridad a buscar
     * @return Lista de tareas con la prioridad especificada
     */
    List<Tarea> buscarPorPrioridad(String prioridad);
}
