package gestortareas.controller;

import gestortareas.dao.impl.CategoriaDAOImpl;
import gestortareas.interfacesGUI.frmCategorias;
import gestortareas.model.Categoria;
import gestortareas.model.Usuario;
import gestortareas.service.CategoriaService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaController {
    private frmCategorias vista;
    private Usuario usuario;
    private CategoriaService categoriaService;

    public CategoriaController(frmCategorias vista, Usuario usuario) {
        this.vista = vista;
        this.usuario = usuario;
        this.categoriaService = new CategoriaService(new CategoriaDAOImpl());
    }

    public void cargarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasUsuario(usuario.getIdUsuario());
            vista.mostrarCategorias(categorias);
            System.out.println("Categorías cargadas: " + categorias.size());
        } catch (Exception e) {
            mostrarError("Error al cargar categorías: " + e.getMessage());
        }
    }

    public List<Categoria> obtenerCategoriasActuales() {
        try {
            return categoriaService.obtenerCategoriasUsuario(usuario.getIdUsuario());
        } catch (Exception e) {
            mostrarError("Error al obtener categorías: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void crearCategoria(String nombre) {
        try {
            if (verificarCategoriaExistente(nombre)) {
                mostrarError("Ya existe una categoría con el nombre: '" + nombre + "'");
                return;
            }

            boolean exito = categoriaService.crearCategoria(usuario.getIdUsuario(), nombre);

            if (exito) {
                mostrarExito("Categoría creada exitosamente");
                cargarCategorias();
            } else {
                mostrarError("Error al crear la categoría");
            }
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al crear categoría: " + e.getMessage());
        }
    }

    public void actualizarCategoria(int categoriaId, String nombre) {
        try {
            boolean exito = categoriaService.actualizarCategoria(categoriaId, nombre);

            if (exito) {
                mostrarExito("Categoría actualizada exitosamente");
                cargarCategorias();
            } else {
                mostrarError("Error al actualizar la categoría");
            }
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al actualizar categoría: " + e.getMessage());
        }
    }

    public void eliminarCategoria(int categoriaId) {
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro que desea eliminar esta categoría?",
                "Eliminar Categoría",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = categoriaService.eliminarCategoria(categoriaId);

                if (exito) {
                    mostrarExito("Categoría eliminada exitosamente");
                    cargarCategorias();
                } else {
                    mostrarError("No se pudo eliminar la categoría. Puede que tenga tareas asociadas.");
                }
            } catch (Exception e) {
                mostrarError("Error al eliminar categoría: " + e.getMessage());
            }
        }
    }

    public boolean eliminarCategoriaDirecta(int categoriaId){
        try {
            return categoriaService.eliminarCategoria(categoriaId);
        } catch (Exception e) {
            mostrarError("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarCategoriaExistente(String nombreCategoria) {
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasUsuario(usuario.getIdUsuario());
            return categorias.stream()
                    .anyMatch(cat -> cat.getNombre().equalsIgnoreCase(nombreCategoria));
        } catch (Exception e) {
            mostrarError("Error al verificar categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarCategoriaExistente(String nombreCategoria, int excludeCategoriaId) {
        try {
            List<Categoria> categorias = categoriaService.obtenerCategoriasUsuario(usuario.getIdUsuario());
            return categorias.stream()
                    .filter(cat -> cat.getId() != excludeCategoriaId)
                    .anyMatch(cat -> cat.getNombre().equalsIgnoreCase(nombreCategoria));
        } catch (Exception e) {
            mostrarError("Error al verificar categoría: " + e.getMessage());
            return false;
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
