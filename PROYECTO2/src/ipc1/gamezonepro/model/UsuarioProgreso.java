package ipc1.gamezonepro.model;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoSimple;

public class UsuarioProgreso {

    private String nombre;
    private String carnet;
    private int xp;
    private double dineroGastado;
    private int juegosComprados;
    private int cartasEnAlbum;
    private int cartasLegendarias;
    private int filasCompletas;
    private final ListaEnlazadaSimple<String> torneosParticipados;

    public UsuarioProgreso(String nombre, String carnet) {
        this.nombre = nombre;
        this.carnet = carnet;
        this.torneosParticipados = new ListaEnlazadaSimple<String>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getCarnet() {
        return carnet;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void sumarXp(int puntos) {
        xp += puntos;
    }

    public double getDineroGastado() {
        return dineroGastado;
    }

    public void sumarDineroGastado(double dinero) {
        dineroGastado += dinero;
    }

    public int getJuegosComprados() {
        return juegosComprados;
    }

    public void sumarJuegosComprados(int cantidad) {
        juegosComprados += cantidad;
    }

    public int getCartasEnAlbum() {
        return cartasEnAlbum;
    }

    public void setCartasEnAlbum(int cartasEnAlbum) {
        this.cartasEnAlbum = cartasEnAlbum;
    }

    public void incrementarCartasEnAlbum() {
        cartasEnAlbum++;
    }

    public int getCartasLegendarias() {
        return cartasLegendarias;
    }

    public void incrementarCartasLegendarias() {
        cartasLegendarias++;
    }

    public int getFilasCompletas() {
        return filasCompletas;
    }

    public void incrementarFilasCompletas() {
        filasCompletas++;
    }

    public ListaEnlazadaSimple<String> getTorneosParticipados() {
        return torneosParticipados;
    }

    public int getCantidadTorneosDistintos() {
        return torneosParticipados.tamanio();
    }

    public void registrarTorneoSiNoExiste(String torneoId) {
        NodoSimple<String> actual = torneosParticipados.getCabeza();
        while (actual != null) {
            if (actual.getDato().equalsIgnoreCase(torneoId)) {
                return;
            }
            actual = actual.getSiguiente();
        }
        torneosParticipados.agregar(torneoId);
    }
}
