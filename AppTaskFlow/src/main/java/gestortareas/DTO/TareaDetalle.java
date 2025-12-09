package gestortareas.DTO;

import java.util.Date;

public class TareaDetalle {
    private String nombreTarea;
    private String descripcion;
    private Date fechaLimite;
    private String prioridad;
    private String estado;
    private String nombresCompletos;
    private String Categoria;
    private int numeroArchivosAdjuntos;

    public TareaDetalle() {
    }

    public TareaDetalle(String nombreTarea, String descripcion, Date fechaLimite, String prioridad, String estado,
                        String nombresCompletos, String categoria, int numeroArchivosAdjuntos) {
        this.nombreTarea = nombreTarea;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.prioridad = prioridad;
        this.estado = estado;
        this.nombresCompletos = nombresCompletos;
        Categoria = categoria;
        this.numeroArchivosAdjuntos = numeroArchivosAdjuntos;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombresCompletos() {
        return nombresCompletos;
    }

    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public int getNumeroArchivosAdjuntos() {
        return numeroArchivosAdjuntos;
    }

    public void setNumeroArchivosAdjuntos(int numeroArchivosAdjuntos) {
        this.numeroArchivosAdjuntos = numeroArchivosAdjuntos;
    }

    @Override
    public String toString() {
        return "TareaDetalle{" +
                "nombreTarea='" + nombreTarea + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", prioridad='" + prioridad + '\'' +
                ", estado='" + estado + '\'' +
                ", nombresCompletos='" + nombresCompletos + '\'' +
                ", Categoria='" + Categoria + '\'' +
                ", numeroArchivosAdjuntos=" + numeroArchivosAdjuntos +
                '}';
    }
}
