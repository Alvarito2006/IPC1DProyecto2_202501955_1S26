package ipc1.gamezonepro.model;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoSimple;
import java.time.LocalDateTime;

public class Compra {

    private final LocalDateTime fechaHora;
    private final ListaEnlazadaSimple<CompraDetalle> detalles;
    private final double total;

    public Compra(LocalDateTime fechaHora, ListaEnlazadaSimple<CompraDetalle> detalles, double total) {
        this.fechaHora = fechaHora;
        this.detalles = detalles;
        this.total = total;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public ListaEnlazadaSimple<CompraDetalle> getDetalles() {
        return detalles;
    }

    public double getTotal() {
        return total;
    }

    public int getCantidadItems() {
        int cantidad = 0;
        NodoSimple<CompraDetalle> actual = detalles.getCabeza();
        while (actual != null) {
            cantidad += actual.getDato().getCantidad();
            actual = actual.getSiguiente();
        }
        return cantidad;
    }
}
