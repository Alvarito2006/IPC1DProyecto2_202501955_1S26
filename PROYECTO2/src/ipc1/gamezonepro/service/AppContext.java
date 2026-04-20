package ipc1.gamezonepro.service;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.MatrizOrtogonal;
import ipc1.gamezonepro.model.Carta;
import ipc1.gamezonepro.model.Compra;
import ipc1.gamezonepro.model.EstudianteInfo;
import ipc1.gamezonepro.model.LeaderboardEntry;
import ipc1.gamezonepro.model.Logro;
import ipc1.gamezonepro.model.TicketVenta;
import ipc1.gamezonepro.model.Torneo;
import ipc1.gamezonepro.model.UsuarioProgreso;
import ipc1.gamezonepro.model.Videojuego;
import ipc1.gamezonepro.persistence.DataStore;

public class AppContext {

    private final EstudianteInfo estudianteInfo;
    private final ListaEnlazadaSimple<Videojuego> catalogo;
    private final ListaEnlazadaSimple<Carta> catalogoCartas;
    private final ListaEnlazadaSimple<Compra> historialCompras;
    private final MatrizOrtogonal album;
    private final ListaEnlazadaSimple<Torneo> torneos;
    private final ListaEnlazadaSimple<TicketVenta> ticketsVendidos;
    private final UsuarioProgreso usuario;
    private final ListaEnlazadaSimple<Logro> logros;
    private final ListaEnlazadaSimple<LeaderboardEntry> leaderboard;

    private final RecompensasService recompensasService;
    private final TiendaService tiendaService;
    private final AlbumService albumService;
    private final TorneoService torneoService;
    private final ReporteService reporteService;

    public AppContext() {
        EstudianteInfo porDefecto = new EstudianteInfo(
                "Alvaro Moisés Girón Morales",
                "202501955",
                "3055401650208",
                "202501955@ingenieria.usac.edu.gt",
                "D",
                "Primer semestre 2026",
                "GameZone Pro integra tienda, album coleccionable, torneos concurrentes, recompensas y reportes HTML.");

        DataStore.inicializarArchivosBase(porDefecto);
        EstudianteInfo cargado = DataStore.cargarEstudiante();
        this.estudianteInfo = cargado != null ? cargado : porDefecto;
        this.catalogo = DataStore.cargarCatalogo();
        this.catalogoCartas = DataStore.cargarCatalogoCartas();
        this.historialCompras = DataStore.cargarHistorial();
        this.album = DataStore.cargarAlbum();
        this.torneos = DataStore.cargarTorneos();
        this.ticketsVendidos = DataStore.cargarTicketsVendidos();
        this.usuario = DataStore.cargarUsuarioActual(estudianteInfo);
        this.logros = DataStore.cargarLogros();
        this.leaderboard = DataStore.cargarLeaderboard();

        this.recompensasService = new RecompensasService(usuario, logros, leaderboard);
        this.recompensasService.sincronizarCartasEnAlbum(album.contarCartas());
        this.tiendaService = new TiendaService(catalogo, historialCompras, recompensasService);
        this.albumService = new AlbumService(album, catalogoCartas, recompensasService);
        this.torneoService = new TorneoService(torneos, ticketsVendidos, recompensasService);
        this.reporteService = new ReporteService();

        this.recompensasService.registrarInicioSesion();
    }

    public TiendaService getTiendaService() {
        return tiendaService;
    }

    public AlbumService getAlbumService() {
        return albumService;
    }

    public TorneoService getTorneoService() {
        return torneoService;
    }

    public RecompensasService getRecompensasService() {
        return recompensasService;
    }

    public ReporteService getReporteService() {
        return reporteService;
    }

    public EstudianteInfo getEstudianteInfo() {
        return estudianteInfo;
    }

    public void guardarTodo() {
        DataStore.guardarCatalogo(catalogo);
        DataStore.guardarHistorial(historialCompras);
        DataStore.guardarAlbum(album);
        DataStore.guardarTorneos(torneos);
        DataStore.guardarTicketsVendidos(ticketsVendidos);
        DataStore.guardarUsuarioActual(usuario);
        DataStore.guardarLogros(logros);
        DataStore.guardarLeaderboard(leaderboard);
    }
}
