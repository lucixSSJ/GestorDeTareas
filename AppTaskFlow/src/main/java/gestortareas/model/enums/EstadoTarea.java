package gestortareas.model.enums;

public enum EstadoTarea {
    PENDIENTE("pendiente"),
    EN_PROGRESO("en_progreso"),
    COMPLETADA("completada"),
    ARCHIVADA("archivada"),
    VENCIDA("vencida");

    private final String valor;

    EstadoTarea(String valor){
        this.valor = valor;
    }

    public String getValor(){
        return valor;
    }

    public static EstadoTarea fromString(String valor) {
        for (EstadoTarea estado : values()) {
            if (estado.valor.equalsIgnoreCase(valor)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado desconocido: " + valor);
    }
}
