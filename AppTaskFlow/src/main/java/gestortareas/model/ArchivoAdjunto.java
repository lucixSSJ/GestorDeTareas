package gestortareas.model;

import java.time.LocalDate;

public class ArchivoAdjunto {
    private Long idArchivo;
    private Long idTarea;
    private String nombreArchivo;
    private String tipoArchivo;
    private String ruta;
    private Long tamanio;
    private LocalDate fechaSubida;

    public ArchivoAdjunto(Long idArchivo, Long idTarea, String nombreArchivo, String tipoArchivo, String ruta, Long tamanio, LocalDate fechaSubida) {
        this.idArchivo = idArchivo;
        this.idTarea = idTarea;
        this.nombreArchivo = nombreArchivo;
        this.tipoArchivo = tipoArchivo;
        this.ruta = ruta;
        this.tamanio = tamanio;
        this.fechaSubida = fechaSubida;
    }

    public ArchivoAdjunto() {
    }

    public Long getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Long getTamanio() {
        return tamanio;
    }

    public void setTamanio(Long tamanio) {
        this.tamanio = tamanio;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
}
