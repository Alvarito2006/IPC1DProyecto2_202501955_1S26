package ipc1.gamezonepro.model;

public class Carta {

    private String codigo;
    private String nombre;
    private String tipo;
    private String rareza;
    private int ataque;
    private int defensa;
    private int salud;
    private String rutaImagen;

    public Carta(String codigo, String nombre, String tipo, String rareza, int ataque, int defensa, int salud, String rutaImagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.rareza = rareza;
        this.ataque = ataque;
        this.defensa = defensa;
        this.salud = salud;
        this.rutaImagen = rutaImagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRareza() {
        return rareza;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getSalud() {
        return salud;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public Carta copiar() {
        return new Carta(codigo, nombre, tipo, rareza, ataque, defensa, salud, rutaImagen);
    }
}
