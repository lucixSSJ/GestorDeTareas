package gestortareas.model;

import java.time.LocalDate;

public class ImagenTarea {
    private Long idTarea;
    private Long idImagen;
    private String nombreImagen;
    private String tipoImagen;
    private String rutaImagen;
    private Long tamanio;
    private boolean isPrincipal;
    private LocalDate fechaSubida;

    public ImagenTarea(Long idTarea, Long idImagen, String nombreImagen, String tipoImagen, String rutaImagen, Long tamanio, boolean isPrincipal, LocalDate fechaSubida) {
        this.idTarea = idTarea;
        this.idImagen = idImagen;
        this.nombreImagen = nombreImagen;
        this.tipoImagen = tipoImagen;
        this.rutaImagen = rutaImagen;
        this.tamanio = tamanio;
        this.isPrincipal = isPrincipal;
        this.fechaSubida = fechaSubida;
    }

    public Long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public Long getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Long idImagen) {
        this.idImagen = idImagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getTipoImagen() {
        return tipoImagen;
    }

    public void setTipoImagen(String tipoImagen) {
        this.tipoImagen = tipoImagen;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public Long getTamanio() {
        return tamanio;
    }

    public void setTamanio(Long tamanio) {
        this.tamanio = tamanio;
    }

    public boolean isPrincipal() {
        return isPrincipal;
    }

    public void setPrincipal(boolean principal) {
        isPrincipal = principal;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
}
