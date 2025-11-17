/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import domain.Usuario;
import java.util.List;

/**
 * Interface DAO para operaciones con Usuario
 * @author Luciano
 */
public interface UsuarioDao {
    
    /**
     * Guarda un nuevo usuario
     */
    boolean guardar(Usuario usuario);
    
    /**
     * Actualiza un usuario existente
     */
    boolean actualizar(Usuario usuario);
    
    /**
     * Elimina un usuario por ID
     */
    boolean eliminar(int id);
    
    /**
     * Busca un usuario por ID
     */
    Usuario buscarPorId(int id);
    
    /**
     * Busca un usuario por username
     */
    Usuario buscarPorUsername(String username);
    
    /**
     * Busca un usuario por email
     */
    Usuario buscarPorEmail(String email);
    
    /**
     * Verifica si existe un username
     */
    boolean existeUsername(String username);
    
    /**
     * Verifica si existe un email
     */
    boolean existeEmail(String email);
    
    /**
     * Autentica un usuario (login)
     */
    Usuario autenticar(String username, String password);
    
    /**
     * Actualiza la fecha del último login
     */
    boolean actualizarUltimoLogin(int idUsuario);
    
    /**
     * Obtiene todos los usuarios
     */
    List<Usuario> obtenerTodos();
}