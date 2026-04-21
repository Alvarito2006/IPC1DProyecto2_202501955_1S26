package ipc1.gamezonepro.service;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.Carta;
import ipc1.gamezonepro.model.LeaderboardEntry;
import ipc1.gamezonepro.model.Logro;
import ipc1.gamezonepro.model.UsuarioProgreso;
import java.time.LocalDateTime;

public class RecompensasService {

    private final UsuarioProgreso usuario;
    private final ListaEnlazadaSimple<Logro> logros;
    private final ListaEnlazadaSimple<LeaderboardEntry> leaderboard;
    private final ListaEnlazadaSimple<String> notificaciones;

    public RecompensasService(UsuarioProgreso usuario, ListaEnlazadaSimple<Logro> logros, ListaEnlazadaSimple<LeaderboardEntry> leaderboard) {
        this.usuario = usuario;
        this.logros = logros;
        this.leaderboard = leaderboard;
        this.notificaciones = new ListaEnlazadaSimple<String>();
        sincronizarEntradaUsuario();
    }

    public void registrarInicioSesion() {
        agregarXp(10);
        sincronizarEntradaUsuario();
    }

    public void registrarCompra(double montoTotal, int cantidadJuegos) {
        usuario.sumarDineroGastado(montoTotal);
        usuario.sumarJuegosComprados(cantidadJuegos);
        agregarXp(cantidadJuegos * 50);
        evaluarLogros();
    }

    public void registrarCartaAgregada(Carta carta, boolean filaCompletaNueva) {
        usuario.incrementarCartasEnAlbum();
        if ("Legendaria".equalsIgnoreCase(carta.getRareza())) {
            usuario.incrementarCartasLegendarias();
            agregarXp(200);
        }
        if (filaCompletaNueva) {
            usuario.incrementarFilasCompletas();
            agregarXp(100);
        }
        evaluarLogros();
    }

    public void sincronizarCartasEnAlbum(int cantidad) {
        usuario.setCartasEnAlbum(cantidad);
        evaluarLogros();
    }

    public void registrarTicketComprado(String torneoId) {
        usuario.registrarTorneoSiNoExiste(torneoId);
        agregarXp(150);
        evaluarLogros();
    }

    public int getNivel() {
        int xp = usuario.getXp();
        if (xp >= 7000) {
            return 5;
        }
        if (xp >= 3500) {
            return 4;
        }
        if (xp >= 1500) {
            return 3;
        }
        if (xp >= 500) {
            return 2;
        }
        return 1;
    }

    public String getRango() {
        switch (getNivel()) {
            case 5:
                return "Leyenda";
            case 4:
                return "Maestro";
            case 3:
                return "Veterano";
            case 2:
                return "Jugador";
            default:
                return "Aprendiz";
        }
    }

    public int getXpActualNivel() {
        int xp = usuario.getXp();
        switch (getNivel()) {
            case 5:
                return xp;
            case 4:
                return xp - 3500;
            case 3:
                return xp - 1500;
            case 2:
                return xp - 500;
            default:
                return xp;
        }
    }

    public int getMetaNivel() {
        switch (getNivel()) {
            case 5:
                return usuario.getXp();
            case 4:
                return 7000;
            case 3:
                return 3500;
            case 2:
                return 1500;
            default:
                return 500;
        }
    }

    public int getInicioNivel() {
        switch (getNivel()) {
            case 5:
                return 7000;
            case 4:
                return 3500;
            case 3:
                return 1500;
            case 2:
                return 500;
            default:
                return 0;
        }
    }

    public int getPorcentajeProgresoNivel() {
        if (getNivel() == 5) {
            return 100;
        }
        int tramo = getMetaNivel() - getInicioNivel();
        if (tramo <= 0) {
            return 100;
        }
        return ((usuario.getXp() - getInicioNivel()) * 100) / tramo;
    }

    public UsuarioProgreso getUsuario() {
        return usuario;
    }

    public ListaEnlazadaSimple<Logro> getLogros() {
        return logros;
    }

    public ListaEnlazadaSimple<LeaderboardEntry> getLeaderboard() {
        sincronizarEntradaUsuario();
        return leaderboard;
    }

    public ListaEnlazadaSimple<LeaderboardEntry> obtenerRankingCompletoOrdenado() {
        sincronizarEntradaUsuario();
        ListaEnlazadaSimple<LeaderboardEntry> copia = new ListaEnlazadaSimple<LeaderboardEntry>();
        NodoSimple<LeaderboardEntry> actual = leaderboard.getCabeza();
        while (actual != null) {
            LeaderboardEntry origen = actual.getDato();
            copia.agregar(new LeaderboardEntry(origen.getNombre(), origen.getXp(), origen.isUsuarioActual()));
            actual = actual.getSiguiente();
        }
        ListaEnlazadaSimple<LeaderboardEntry> ordenado = new ListaEnlazadaSimple<LeaderboardEntry>();
        while (!copia.estaVacia()) {
            int indiceMayor = encontrarIndiceMayor(copia);
            ordenado.agregar(copia.eliminarEn(indiceMayor));
        }
        return ordenado;
    }

    public int obtenerPosicionUsuario() {
        ListaEnlazadaSimple<LeaderboardEntry> ranking = obtenerRankingCompletoOrdenado();
        for (int i = 0; i < ranking.tamanio(); i++) {
            if (ranking.obtener(i).isUsuarioActual()) {
                return i + 1;
            }
        }
        return 0;
    }

    public ListaEnlazadaSimple<String> consumirNotificaciones() {
        ListaEnlazadaSimple<String> copia = new ListaEnlazadaSimple<String>();
        NodoSimple<String> actual = notificaciones.getCabeza();
        while (actual != null) {
            copia.agregar(actual.getDato());
            actual = actual.getSiguiente();
        }
        notificaciones.limpiar();
        return copia;
    }

    private void agregarXp(int puntos) {
        usuario.sumarXp(puntos);
        sincronizarEntradaUsuario();
    }

    private void evaluarLogros() {
        desbloquearSiCorresponde("Primera Compra", usuario.getJuegosComprados() > 0);
        desbloquearSiCorresponde("Coleccionista Novato", usuario.getCartasEnAlbum() >= 10);
        desbloquearSiCorresponde("Coleccionista Experto", usuario.getFilasCompletas() >= 1);
        desbloquearSiCorresponde("Taquillero", usuario.getCantidadTorneosDistintos() >= 3);
        desbloquearSiCorresponde("Alta Rareza", usuario.getCartasLegendarias() >= 1);
        desbloquearSiCorresponde("Gamer Dedicado", usuario.getXp() >= 1000);
        desbloquearSiCorresponde("Leyenda Viviente", getNivel() >= 5);
        desbloquearSiCorresponde("Gran Gastador", usuario.getDineroGastado() > 2000);
        sincronizarEntradaUsuario();
    }

    private void desbloquearSiCorresponde(String nombre, boolean condicion) {
        if (!condicion) {
            return;
        }
        NodoSimple<Logro> actual = logros.getCabeza();
        while (actual != null) {
            Logro logro = actual.getDato();
            if (logro.getNombre().equalsIgnoreCase(nombre) && !logro.isDesbloqueado()) {
                logro.setDesbloqueado(true);
                logro.setFechaDesbloqueo(LocalDateTime.now().toString());
                notificaciones.agregar("Logro desbloqueado: " + logro.getNombre());
                return;
            }
            actual = actual.getSiguiente();
        }
    }

    private void sincronizarEntradaUsuario() {
        NodoSimple<LeaderboardEntry> actual = leaderboard.getCabeza();
        while (actual != null) {
            LeaderboardEntry entry = actual.getDato();
            entry.setUsuarioActual(false);
            if (entry.getNombre().equalsIgnoreCase(usuario.getNombre())) {
                entry.setXp(usuario.getXp());
                entry.setUsuarioActual(true);
                return;
            }
            actual = actual.getSiguiente();
        }
        leaderboard.agregar(new LeaderboardEntry(usuario.getNombre(), usuario.getXp(), true));
    }

    private int encontrarIndiceMayor(ListaEnlazadaSimple<LeaderboardEntry> lista) {
        int indiceMayor = 0;
        int mayorXp = lista.obtener(0).getXp();
        for (int i = 1; i < lista.tamanio(); i++) {
            if (lista.obtener(i).getXp() > mayorXp) {
                mayorXp = lista.obtener(i).getXp();
                indiceMayor = i;
            }
        }
        return indiceMayor;
    }
}
