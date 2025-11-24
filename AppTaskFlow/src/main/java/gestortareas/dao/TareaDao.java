/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortareas.dao;

import gestortareas.model.Tarea;

/**
 *
 * @author Michael Medina
 */
public interface TareaDao {
    boolean create(Tarea tarea);
    boolean existe(String nombreTarea);
}
