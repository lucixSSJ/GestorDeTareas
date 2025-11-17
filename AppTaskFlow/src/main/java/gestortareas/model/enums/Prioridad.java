package gestortareas.model.enums;

public enum Prioridad {
    BAJA("baja"),
    MEDIA("media"),
    ALTA("alta"),
    URGENTE("urgente");

    private final String valor;

    Prioridad(String valor){
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static Prioridad fromString(String valor) {
        for (Prioridad prioridad : values()) {
            if (prioridad.valor.equalsIgnoreCase(valor)) {
                return prioridad;
            }
        }
        throw new IllegalArgumentException("Prioridad desconocida: " + valor);
    }
}
