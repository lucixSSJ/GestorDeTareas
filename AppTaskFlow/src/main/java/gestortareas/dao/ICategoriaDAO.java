package gestortareas.dao;

import gestortareas.model.Categoria;

public interface ICategoriaDAO {
    boolean existCategoria(String nombre);
    boolean registerCategory(Categoria categoria);
}
