package gestortareas.dao;

import gestortareas.model.Categoria;

import java.util.List;

public interface CategoriaDAO {
    List<Categoria> obtenerPorUsuario(int usuarioId);
    boolean crear(Categoria categoria);
    boolean actualizar(Categoria categoria);
    boolean eliminar(int categoriaId);
    boolean existeCategoriaUsuario(int usuarioId, String nombreCategoria);
    Categoria obtenerPorId(int categoriaId);
    boolean existeNombreCategoria(int usuarioId, String nombreCategoria, int excludeCategoriaId);
}
