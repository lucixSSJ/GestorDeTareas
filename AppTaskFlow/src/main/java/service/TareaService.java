/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.TareaDao;
import dao.impl.JdbcTareaDao;
import domain.Tarea;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de las tareas
 * @author Luciano
 */
public class TareaService {
    
    private TareaDao tareaDao;
    private static TareaService instance;
    
    private TareaService() {
        this.tareaDao = JdbcTareaDao.getInstance();
    }
    
    public static synchronized TareaService getInstance() {
        if (instance == null) {
            instance = new TareaService();
        }
        return instance;
    }
    
    /**
     * Crea una nueva tarea
     * @param tarea La tarea a crear
     * @return true si se creó exitosamente
     */
    public boolean crearTarea(Tarea tarea) {
        if (tarea == null) {
            return false;
        }
        
        // Validaciones de negocio
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea es obligatorio");
        }
        
        if (tarea.getTitulo().length() > 100) {
            throw new IllegalArgumentException("El título no puede exceder 100 caracteres");
        }
        
        return tareaDao.guardar(tarea);
    }
    
    /**
     * Actualiza una tarea existente
     * @param tarea La tarea a actualizar
     * @return true si se actualizó exitosamente
     */
    public boolean actualizarTarea(Tarea tarea) {
        if (tarea == null || tarea.getId() <= 0) {
            return false;
        }
        
        // Validar que la tarea existe
        Tarea tareaExistente = tareaDao.buscarPorId(tarea.getId());
        if (tareaExistente == null) {
            throw new IllegalArgumentException("La tarea no existe");
        }
        
        // Validaciones de negocio
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea es obligatorio");
        }
        
        return tareaDao.actualizar(tarea);
    }
    
    /**
     * Elimina una tarea
     * @param id El ID de la tarea a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean eliminarTarea(int id) {
        if (id <= 0) {
            return false;
        }
        
        // Validar que la tarea existe
        Tarea tarea = tareaDao.buscarPorId(id);
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no existe");
        }
        
        return tareaDao.eliminar(id);
    }
    
    /**
     * Busca una tarea por ID
     * @param id El ID de la tarea
     * @return La tarea encontrada o null
     */
    public Tarea buscarTareaPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return tareaDao.buscarPorId(id);
    }
    
    /**
     * Obtiene todas las tareas
     * @return Lista de todas las tareas
     */
    public List<Tarea> obtenerTodasLasTareas() {
        return tareaDao.obtenerTodas();
    }
    
    /**
     * Busca tareas por categoría
     * @param categoria La categoría a buscar
     * @return Lista de tareas de la categoría
     */
    public List<Tarea> buscarTareasPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return obtenerTodasLasTareas();
        }
        return tareaDao.buscarPorCategoria(categoria);
    }
    
    /**
     * Busca tareas por estado
     * @param estado El estado a buscar
     * @return Lista de tareas con el estado
     */
    public List<Tarea> buscarTareasPorEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return obtenerTodasLasTareas();
        }
        return tareaDao.buscarPorEstado(estado);
    }
    
    /**
     * Busca tareas por prioridad
     * @param prioridad La prioridad a buscar
     * @return Lista de tareas con la prioridad
     */
    public List<Tarea> buscarTareasPorPrioridad(String prioridad) {
        if (prioridad == null || prioridad.trim().isEmpty()) {
            return obtenerTodasLasTareas();
        }
        return tareaDao.buscarPorPrioridad(prioridad);
    }
    
    /**
     * Cambia el estado de una tarea
     * @param id ID de la tarea
     * @param nuevoEstado Nuevo estado
     * @return true si se cambió exitosamente
     */
    public boolean cambiarEstadoTarea(int id, String nuevoEstado) {
        Tarea tarea = buscarTareaPorId(id);
        if (tarea != null) {
            tarea.setEstado(nuevoEstado);
            return actualizarTarea(tarea);
        }
        return false;
    }
    
    /**
     * Obtiene las categorías disponibles
     * @return Array de categorías
     */
    public String[] obtenerCategorias() {
        return new String[]{"Trabajo", "Personal", "Estudio", "Hogar", "Salud", "Otros"};
    }
    
    /**
     * Obtiene los estados disponibles
     * @return Array de estados
     */
    public String[] obtenerEstados() {
        return new String[]{"Pendiente", "En Progreso", "Completada"};
    }
    
    /**
     * Obtiene las prioridades disponibles
     * @return Array de prioridades
     */
    public String[] obtenerPrioridades() {
        return new String[]{"Alta", "Media", "Baja"};
    }
}
