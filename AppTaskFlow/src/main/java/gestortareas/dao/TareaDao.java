/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortareas.dao;

import gestortareas.DTO.TareaDetalle;
import gestortareas.model.Tarea;
import gestortareas.model.Usuario;

import java.util.List;

/**
 *
 * @author Michael Medina
 */
public interface TareaDao {
    int create(Tarea tarea);
    boolean existe(String nombreTarea);
    List<Tarea> getTareas(Usuario user);
    TareaDetalle getTareaIDdetalle(int idTarea);
    boolean actualizarTarea(Tarea tarea);
    Tarea obtenerTarea(int idTarea);
}
