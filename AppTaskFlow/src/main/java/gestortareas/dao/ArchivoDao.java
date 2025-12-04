/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestortareas.dao;

import gestortareas.model.ArchivoAdjunto;
import java.util.List;

/**
 *
 * @author Michael
 */
public interface ArchivoDao {
    boolean createArchivo(ArchivoAdjunto archivo,int idTarea);
    boolean createVariosArchivos(List<ArchivoAdjunto> archivos,int idTarea);
}
