package gestortareas.model;

import java.time.LocalDateTime;

public class Usuario {
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String username;
    private String passwordHash;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoLogin;
    private boolean notificacionesVencimiento;
    private int diasAntesVencimiento;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(String nombres, String apellidos, String email,
                   String username, String passwordHash) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.notificacionesVencimiento = true;
        this.diasAntesVencimiento = 1;
        this.activo = true;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public boolean isNotificacionesVencimiento() {
        return notificacionesVencimiento;
    }

    public void setNotificacionesVencimiento(boolean notificacionesVencimiento) {
        this.notificacionesVencimiento = notificacionesVencimiento;
    }

    public int getDiasAntesVencimiento() {
        return diasAntesVencimiento;
    }

    public void setDiasAntesVencimiento(int diasAntesVencimiento) {
        this.diasAntesVencimiento = diasAntesVencimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return this.nombres + " " + this.apellidos;
    }
}