package gestortareas.service;

import gestortareas.dao.CategoriaDAO;
import gestortareas.dao.impl.CategoriaDAOImpl;
import gestortareas.model.Categoria;

import java.util.List;

public class CategoriaService {
    private final CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    public List<Categoria> obtenerCategoriasUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido");
        }
        return categoriaDAO.obtenerPorUsuario(usuarioId);
    }

    public boolean crearCategoria(int usuarioId, String nombre) {
        validarDatosCategoria(usuarioId, nombre);

        Categoria categoria = new Categoria(usuarioId, nombre);
        return categoriaDAO.crear(categoria);
    }

    public boolean existeCategoria(int usuarioId, String nombreCategoria) {
        if (usuarioId <= 0 || nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            return false;
        }

        List<Categoria> categorias = obtenerCategoriasUsuario(usuarioId);
        return categorias.stream()
                .anyMatch(cat -> cat.getNombre().equalsIgnoreCase(nombreCategoria.trim()));
    }

    public boolean actualizarCategoria(int categoriaId, String nombre) {
        if (categoriaId <= 0) {
            throw new IllegalArgumentException("ID de categoría inválido");
        }
        validarDatosCategoria(0, nombre);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setNombre(nombre);

        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminarCategoria(int categoriaId) {
        if (categoriaId <= 0) {
            throw new IllegalArgumentException("ID de categoría inválido");
        }
        return categoriaDAO.eliminar(categoriaId);
    }

    public Categoria obtenerCategoriaPorId(int categoriaId) {
        if (categoriaId <= 0) {
            throw new IllegalArgumentException("ID de categoría inválido");
        }
        return categoriaDAO.obtenerPorId(categoriaId);
    }

    private void validarDatosCategoria(int usuarioId, String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        if (nombre.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre de la categoría no puede tener más de 100 caracteres");
        }
    }
}
