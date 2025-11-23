package gestortareas.model;

public class Categoria {
    private Usuario usuario;
    private String nameCategory;
    private String color;

    public Categoria(Usuario usuario, String nameCategory, String color) {
        this.usuario = usuario;
        this.nameCategory = nameCategory;
        this.color = color;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "usuario=" + usuario +
                ", nameCategory='" + nameCategory + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
