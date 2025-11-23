package gestortareas.model;

import java.time.LocalDateTime;

public class Categoria {
    private int id;
    private int usuarioId;
    private String nombre;
    private LocalDateTime fechaCreacion;
    private int numeroTareas;

    public Categoria() {}

    public Categoria(int usuarioId, String nombre) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.numeroTareas = 0;
    }

    public Categoria(int id, int usuarioId, String nombre, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public int getNumeroTareas() {
        return numeroTareas;
    }

    public void setNumeroTareas(int numeroTareas) {
        this.numeroTareas = numeroTareas;
    }

    @Override
    public String toString() {
        return nombre + " (" + numeroTareas + " tareas)";
    }
}
