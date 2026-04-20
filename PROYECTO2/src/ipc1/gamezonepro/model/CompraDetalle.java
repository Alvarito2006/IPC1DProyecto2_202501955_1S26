package ipc1.gamezonepro.model;

public class CompraDetalle {

    private final String codigoJuego;
    private final String nombreJuego;
    private final int cantidad;
    private final double precioUnitario;

    public CompraDetalle(String codigoJuego, String nombreJuego, int cantidad, double precioUnitario) {
        this.codigoJuego = codigoJuego;
        this.nombreJuego = nombreJuego;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getCodigoJuego() {
        return codigoJuego;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}
