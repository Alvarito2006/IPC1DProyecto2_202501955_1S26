package ipc1.gamezonepro.model;

public class CarritoItem {

    private final Videojuego videojuego;
    private int cantidad;

    public CarritoItem(Videojuego videojuego, int cantidad) {
        this.videojuego = videojuego;
        this.cantidad = cantidad;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return videojuego.getPrecio() * cantidad;
    }
}
