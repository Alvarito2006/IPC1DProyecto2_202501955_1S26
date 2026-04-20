package ipc1.gamezonepro.model;

import ipc1.gamezonepro.datastructures.Cola;

public class Torneo {

    private String id;
    private String nombre;
    private String juego;
    private String fecha;
    private String hora;
    private double precioTicket;
    private int ticketsDisponibles;
    private int ticketsIniciales;
    private int ticketsVendidos;
    private final Cola<String> colaEspera;
    private boolean ventaActiva;

    public Torneo(String id, String nombre, String juego, String fecha, String hora, double precioTicket, int ticketsDisponibles) {
        this.id = id;
        this.nombre = nombre;
        this.juego = juego;
        this.fecha = fecha;
        this.hora = hora;
        this.precioTicket = precioTicket;
        this.ticketsDisponibles = ticketsDisponibles;
        this.ticketsIniciales = ticketsDisponibles;
        this.colaEspera = new Cola<String>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getJuego() {
        return juego;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public double getPrecioTicket() {
        return precioTicket;
    }

    public int getTicketsDisponibles() {
        return ticketsDisponibles;
    }

    public void setTicketsDisponibles(int ticketsDisponibles) {
        this.ticketsDisponibles = ticketsDisponibles;
    }

    public int getTicketsIniciales() {
        return ticketsIniciales;
    }

    public void setTicketsIniciales(int ticketsIniciales) {
        this.ticketsIniciales = ticketsIniciales;
    }

    public int getTicketsVendidos() {
        return ticketsVendidos;
    }

    public void incrementarTicketsVendidos() {
        this.ticketsVendidos++;
    }

    public Cola<String> getColaEspera() {
        return colaEspera;
    }

    public boolean isVentaActiva() {
        return ventaActiva;
    }

    public void setVentaActiva(boolean ventaActiva) {
        this.ventaActiva = ventaActiva;
    }
}
