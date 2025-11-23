package gestortareas.model;

import java.util.Date;

public class Tarea {
    private String nombre;
    private String descripcion;
    private Date fechaLimite;
    private String prioridad;
    private Usuario usuario;

    public Tarea() {
    }

    public Tarea(String descripcion, String nombre, Date fechaLimite, String prioridad) {
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.fechaLimite = fechaLimite;
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", prioridad='" + prioridad + '\'' +
                '}';
    }
}
