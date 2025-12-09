package gestortareas.DTO;

import gestortareas.model.ArchivoAdjunto;
import gestortareas.model.Categoria;
import gestortareas.model.ImagenTarea;
import gestortareas.model.Usuario;

import java.util.Date;
import java.util.List;

public class TareaDTO {
    private int idTarea;
    private Usuario usuario; //este es el usuario a quien se le asigna la tarea
    private String nombre;
    private String descripcion;
    private Date fechaLimite;
    private String prioridad;
    private String estado;
    private List<ArchivoAdjunto> archivoAdjunto;
    private ImagenTarea imagenTarea;
    private Categoria categoria;
    private int recordatorio;
    private Date fechaArchivada;

    public TareaDTO() {}

    public TareaDTO(int idTarea, Usuario usuario, String nombre,
                    String descripcion, Date fechaLimite, String prioridad, String estado,
                    List<ArchivoAdjunto> archivoAdjunto, ImagenTarea imagenTarea, Categoria categoria, int recordatorio, Date fechaArchivada)
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
        this.categoria = categoria;
        this.recordatorio = recordatorio;
        this.fechaArchivada = fechaArchivada;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(int recordatorio) {
        this.recordatorio = recordatorio;
    }

    public Date getFechaArchivada() {
        return fechaArchivada;
    }

    public void setFechaArchivada(Date fechaArchivada) {
        this.fechaArchivada = fechaArchivada;
    }

    @Override
    public String toString() {
        return "TareaDTO{" +
                "idTarea=" + idTarea +
                ", usuario=" + usuario +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", prioridad='" + prioridad + '\'' +
                ", estado='" + estado + '\'' +
                ", categoria=" + categoria +
                ", fechaArchivada=" + fechaArchivada +
                '}';
    }
}
