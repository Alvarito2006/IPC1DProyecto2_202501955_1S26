package ipc1.gamezonepro.service;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.TicketVenta;
import ipc1.gamezonepro.model.Torneo;
import java.time.LocalDateTime;
import java.util.Random;
import javax.swing.SwingUtilities;

public class TorneoService {

    public interface VentaListener {

        void onCambioEstado();
    }

    private final ListaEnlazadaSimple<Torneo> torneos;
    private final ListaEnlazadaSimple<TicketVenta> ticketsVendidos;
    private final ListaEnlazadaSimple<String> logEventos;
    private final RecompensasService recompensasService;
    private final Random random;

    private volatile String estadoTaquillaUno = "Libre";
    private volatile String estadoTaquillaDos = "Libre";
    private VentaListener ventaListener;

    public TorneoService(ListaEnlazadaSimple<Torneo> torneos, ListaEnlazadaSimple<TicketVenta> ticketsVendidos, RecompensasService recompensasService) {
        this.torneos = torneos;
        this.ticketsVendidos = ticketsVendidos;
        this.recompensasService = recompensasService;
        this.logEventos = new ListaEnlazadaSimple<String>();
        this.random = new Random();
    }

    public ListaEnlazadaSimple<Torneo> getTorneos() {
        return torneos;
    }

    public ListaEnlazadaSimple<TicketVenta> getTicketsVendidos() {
        return ticketsVendidos;
    }

    public ListaEnlazadaSimple<String> getLogEventos() {
        return logEventos;
    }

    public String getEstadoTaquillaUno() {
        return estadoTaquillaUno;
    }

    public String getEstadoTaquillaDos() {
        return estadoTaquillaDos;
    }

    public void setVentaListener(VentaListener ventaListener) {
        this.ventaListener = ventaListener;
    }

    public String inscribirUsuario(Torneo torneo, String nombreUsuario) {
        if (torneo == null) {
            return "Selecciona un torneo antes de inscribirte.";
        }
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return "Ingresa el nombre del usuario para la cola.";
        }
        if (torneo.getTicketsDisponibles() <= 0) {
            return "Ese torneo ya no tiene tickets disponibles.";
        }
        torneo.getColaEspera().encolar(nombreUsuario.trim());
        agregarLog("Usuario agregado a la cola de " + torneo.getNombre() + ": " + nombreUsuario.trim());
        notificarCambio();
        return "Usuario agregado correctamente a la cola.";
    }

    public String iniciarVenta(Torneo torneo) {
        if (torneo == null) {
            return "Selecciona un torneo para iniciar la venta.";
        }
        if (torneo.isVentaActiva()) {
            return "La venta ya esta en proceso para ese torneo.";
        }
        if (torneo.getTicketsDisponibles() <= 0) {
            return "El torneo ya no tiene tickets disponibles.";
        }
        if (torneo.getColaEspera().estaVacia()) {
            return "Primero debes agregar usuarios a la cola.";
        }

        torneo.setVentaActiva(true);
        estadoTaquillaUno = "Esperando turno";
        estadoTaquillaDos = "Esperando turno";
        agregarLog("Venta iniciada para " + torneo.getNombre() + ".");
        notificarCambio();

        new TaquillaWorker("Taquilla 1", torneo).start();
        new TaquillaWorker("Taquilla 2", torneo).start();
        return "Las taquillas comenzaron a procesar la fila.";
    }

    private class TaquillaWorker extends Thread {

        private final String nombreTaquilla;
        private final Torneo torneo;

        public TaquillaWorker(String nombreTaquilla, Torneo torneo) {
            this.nombreTaquilla = nombreTaquilla;
            this.torneo = torneo;
        }

        @Override
        public void run() {
            while (torneo.isVentaActiva()) {
                String usuario = torneo.getColaEspera().desencolar();
                if (usuario == null) {
                    actualizarEstado(nombreTaquilla, "Libre");
                    finalizarVentaSiCorresponde(torneo, "La cola del torneo " + torneo.getNombre() + " quedo vacia.");
                    return;
                }

                actualizarEstado(nombreTaquilla, "Procesando a: " + usuario);
                dormirProceso();

                boolean ventaRealizada = false;
                synchronized (torneo) {
                    if (torneo.getTicketsDisponibles() > 0) {
                        torneo.setTicketsDisponibles(torneo.getTicketsDisponibles() - 1);
                        torneo.incrementarTicketsVendidos();
                        ventaRealizada = true;
                    }
                }

                if (ventaRealizada) {
                    ticketsVendidos.agregarAlInicio(new TicketVenta(
                            LocalDateTime.now(),
                            torneo.getId(),
                            torneo.getNombre(),
                            usuario,
                            nombreTaquilla,
                            torneo.getPrecioTicket()));
                    recompensasService.registrarTicketComprado(torneo.getId());
                    agregarLog("[" + nombreTaquilla + "] Ticket vendido a " + usuario + " para " + torneo.getNombre() + ".");
                } else {
                    agregarLog("[" + nombreTaquilla + "] No se pudo vender ticket a " + usuario + " porque se agotaron.");
                }

                actualizarEstado(nombreTaquilla, "Libre");

                if (torneo.getTicketsDisponibles() <= 0) {
                    finalizarVentaSiCorresponde(torneo, "Se agotaron los tickets de " + torneo.getNombre() + ".");
                    return;
                }
            }
            actualizarEstado(nombreTaquilla, "Cerrada");
        }

        private void dormirProceso() {
            try {
                Thread.sleep(800 + random.nextInt(1201));
            } catch (InterruptedException ignored) {
                interrupt();
            }
        }
    }

    private synchronized void finalizarVentaSiCorresponde(Torneo torneo, String mensaje) {
        if (!torneo.isVentaActiva()) {
            return;
        }
        if (torneo.getTicketsDisponibles() <= 0 || torneo.getColaEspera().estaVacia()) {
            torneo.setVentaActiva(false);
            estadoTaquillaUno = "Cerrada";
            estadoTaquillaDos = "Cerrada";
            agregarLog(mensaje);
            notificarCambio();
        }
    }

    private void actualizarEstado(String taquilla, String estado) {
        if ("Taquilla 1".equalsIgnoreCase(taquilla)) {
            estadoTaquillaUno = estado;
        } else {
            estadoTaquillaDos = estado;
        }
        notificarCambio();
    }

    private void agregarLog(String mensaje) {
        logEventos.agregarAlInicio(LocalDateTime.now() + " - " + mensaje);
        notificarCambio();
    }

    private void notificarCambio() {
        if (ventaListener != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ventaListener.onCambioEstado();
                }
            });
        }
    }
}
