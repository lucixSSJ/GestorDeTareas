package gestortareas.model;


import java.util.*;
import java.util.Date;
/*
* Autor: Michael Medina
 */
public class Tarea {
    private int idTarea;
    private Usuario usuario; //este es el usuario a quien se le asigna la tarea
    private String nombre;
    private String descripcion;
    private Date fechaLimite;
    private String prioridad;
    private String estado;
    private List<ArchivoAdjunto> archivoAdjunto;
    private ImagenTarea imagenTarea;
    private int idCategoria;
    public Tarea(int idTarea, Usuario usuario, String nombre, String descripcion, Date fechaLimite, String prioridad, String estado, List<ArchivoAdjunto> archivoAdjunto, ImagenTarea imagenTarea, int idCategoria)
    {
        this.idTarea = idTarea;
        this.usuario = usuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.prioridad = prioridad;
        this.estado = estado;
        this.archivoAdjunto = archivoAdjunto;
        this.imagenTarea = imagenTarea;
        this.idCategoria = idCategoria;
    }

    public static class TareaBuilder implements Build<Tarea> {
        private int idTarea;
        private Usuario usuario; //este es el usuario a quien se le asigna la tarea
        private String nombre;
        private String descripcion;
        private Date fechaLimite;
        private String prioridad;
        private String estado;
        private List<ArchivoAdjunto> archivoAdjunto = new ArrayList<>();
        private ImagenTarea imagenTarea;
        private int idCategoria;
        public TareaBuilder() {
        }

        public TareaBuilder setIdCategoria(int idCategoria) {
            this.idCategoria = idCategoria;
            return this;
        }

        public TareaBuilder setIdTarea(int idTarea) {
            this.idTarea = idTarea;
            return this;
        }

        public TareaBuilder setUsuario(Usuario usuario) {
            this.usuario = usuario;
            return this;
        }

        public TareaBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public TareaBuilder setDescripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public TareaBuilder setFechaLimite(Date fechaLimite) {
            this.fechaLimite = fechaLimite;
            return this;
        }

        public TareaBuilder setPrioridad(String prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public TareaBuilder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public TareaBuilder setArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
            this.archivoAdjunto.add(archivoAdjunto);
            return this;
        }

        public TareaBuilder setImagenTarea(ImagenTarea imagenTarea) {
            this.imagenTarea = imagenTarea;
            return this;
        }

        @Override
        public Tarea build() {
            return new Tarea(idTarea,usuario,nombre,descripcion,fechaLimite,prioridad,estado,archivoAdjunto,imagenTarea,idCategoria);
        }
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Tarea() {
    }

    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ArchivoAdjunto> getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(List<ArchivoAdjunto> archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    public ImagenTarea getImagenTarea() {
        return imagenTarea;
    }

    public void setImagenTarea(ImagenTarea imagenTarea) {
        this.imagenTarea = imagenTarea;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "idTarea=" + idTarea +
                ", usuario=" + usuario +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", prioridad='" + prioridad + '\'' +
                ", estado='" + estado + '\'' +
                ", archivoAdjunto=" + archivoAdjunto +
                ", imagenTarea=" + imagenTarea +
                '}';
    }
}
