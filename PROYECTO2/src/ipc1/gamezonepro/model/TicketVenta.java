package ipc1.gamezonepro.model;

import java.time.LocalDateTime;

public class TicketVenta {

    private final LocalDateTime fechaHora;
    private final String torneoId;
    private final String torneoNombre;
    private final String comprador;
    private final String taquilla;
    private final double precio;

    public TicketVenta(LocalDateTime fechaHora, String torneoId, String torneoNombre, String comprador, String taquilla, double precio) {
        this.fechaHora = fechaHora;
        this.torneoId = torneoId;
        this.torneoNombre = torneoNombre;
        this.comprador = comprador;
        this.taquilla = taquilla;
        this.precio = precio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getTorneoId() {
        return torneoId;
    }

    public String getTorneoNombre() {
        return torneoNombre;
    }

    public String getComprador() {
        return comprador;
    }

    public String getTaquilla() {
        return taquilla;
    }

    public double getPrecio() {
        return precio;
    }
}
