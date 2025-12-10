package gestortareas.model;

import java.time.LocalDateTime;

public class LogNotificacion {
    private int idLog;
    private int idUsuario;
    private Integer idTarea;
    private String tipoNotificacion;
    private String emailDestino;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean enviadoCorrectamente;
    private String errorEnvio;

    // Getters y Setters
    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdTarea() { return idTarea; }
    public void setIdTarea(Integer idTarea) { this.idTarea = idTarea; }

    public String getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(String tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }

    public String getEmailDestino() { return emailDestino; }
    public void setEmailDestino(String emailDestino) { this.emailDestino = emailDestino; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public boolean isEnviadoCorrectamente() { return enviadoCorrectamente; }
    public void setEnviadoCorrectamente(boolean enviadoCorrectamente) { this.enviadoCorrectamente = enviadoCorrectamente; }

    public String getErrorEnvio() { return errorEnvio; }
    public void setErrorEnvio(String errorEnvio) { this.errorEnvio = errorEnvio; }
}
