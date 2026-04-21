package ipc1.gamezonepro.persistence;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoMatriz;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.datastructures.MatrizOrtogonal;
import ipc1.gamezonepro.model.Carta;
import ipc1.gamezonepro.model.Compra;
import ipc1.gamezonepro.model.CompraDetalle;
import ipc1.gamezonepro.model.EstudianteInfo;
import ipc1.gamezonepro.model.LeaderboardEntry;
import ipc1.gamezonepro.model.Logro;
import ipc1.gamezonepro.model.TicketVenta;
import ipc1.gamezonepro.model.Torneo;
import ipc1.gamezonepro.model.UsuarioProgreso;
import ipc1.gamezonepro.model.Videojuego;
import ipc1.gamezonepro.util.FormatoUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Scanner;

public final class DataStore {

    private DataStore() {
    }

    public static void inicializarArchivosBase(EstudianteInfo estudiante) {
        DataPaths.asegurarDirectorios();
        crearSiNoExiste(DataPaths.CATALOGO, catalogoInicial());
        crearSiNoExiste(DataPaths.CARTAS, cartasIniciales());
        crearSiNoExiste(DataPaths.HISTORIAL, "");
        crearSiNoExiste(DataPaths.ALBUM, "6|6");
        crearSiNoExiste(DataPaths.TORNEOS, torneosIniciales());
        crearSiNoExiste(DataPaths.TICKETS, "");
        crearSiNoExiste(DataPaths.LEADERBOARD, leaderboardInicial());
        crearSiNoExiste(DataPaths.PERFIL, perfilInicial(estudiante));
        crearSiNoExiste(DataPaths.LOGROS, logrosIniciales());
        crearSiNoExiste(DataPaths.ESTUDIANTE, estudianteInicial(estudiante));
    }

    public static ListaEnlazadaSimple<Videojuego> cargarCatalogo() {
        ListaEnlazadaSimple<Videojuego> catalogo = new ListaEnlazadaSimple<Videojuego>();
        Scanner scanner = crearScanner(DataPaths.CATALOGO);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 7) {
                catalogo.agregar(new Videojuego(
                        partes.obtener(0),
                        partes.obtener(1),
                        partes.obtener(2),
                        FormatoUtil.decimalSeguro(partes.obtener(3)),
                        partes.obtener(4),
                        FormatoUtil.enteroSeguro(partes.obtener(5)),
                        partes.obtener(6)));
            }
        }
        scanner.close();
        return catalogo;
    }

    public static void guardarCatalogo(ListaEnlazadaSimple<Videojuego> catalogo) {
        PrintWriter writer = crearWriter(DataPaths.CATALOGO);
        NodoSimple<Videojuego> actual = catalogo.getCabeza();
        while (actual != null) {
            Videojuego juego = actual.getDato();
            writer.println(juego.getCodigo() + "|" + juego.getNombre() + "|" + juego.getGenero() + "|"
                    + juego.getPrecio() + "|" + juego.getPlataforma() + "|" + juego.getStock() + "|" + juego.getDescripcion());
            actual = actual.getSiguiente();
        }
        writer.close();
    }

    public static ListaEnlazadaSimple<Carta> cargarCatalogoCartas() {
        ListaEnlazadaSimple<Carta> cartas = new ListaEnlazadaSimple<Carta>();
        Scanner scanner = crearScanner(DataPaths.CARTAS);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 8) {
                cartas.agregar(new Carta(
                        partes.obtener(0),
                        partes.obtener(1),
                        partes.obtener(2),
                        partes.obtener(3),
                        FormatoUtil.enteroSeguro(partes.obtener(4)),
                        FormatoUtil.enteroSeguro(partes.obtener(5)),
                        FormatoUtil.enteroSeguro(partes.obtener(6)),
                        partes.obtener(7)));
            }
        }
        scanner.close();
        return cartas;
    }

    public static ListaEnlazadaSimple<Compra> cargarHistorial() {
        ListaEnlazadaSimple<Compra> historial = new ListaEnlazadaSimple<Compra>();
        Scanner scanner = crearScanner(DataPaths.HISTORIAL);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|', 3);
            if (partes.tamanio() >= 3) {
                ListaEnlazadaSimple<CompraDetalle> detalles = new ListaEnlazadaSimple<CompraDetalle>();
                ListaEnlazadaSimple<String> detallePartes = separar(partes.obtener(2), ';');
                for (int i = 0; i < detallePartes.tamanio(); i++) {
                    String detalleTexto = detallePartes.obtener(i).trim();
                    if (detalleTexto.isEmpty()) {
                        continue;
                    }
                    ListaEnlazadaSimple<String> campos = separar(detalleTexto, '~');
                    if (campos.tamanio() >= 4) {
                        detalles.agregar(new CompraDetalle(
                                campos.obtener(0),
                                campos.obtener(1),
                                FormatoUtil.enteroSeguro(campos.obtener(2)),
                                FormatoUtil.decimalSeguro(campos.obtener(3))));
                    }
                }
                historial.agregar(new Compra(LocalDateTime.parse(partes.obtener(0)), detalles, FormatoUtil.decimalSeguro(partes.obtener(1))));
            }
        }
        scanner.close();
        return historial;
    }

    public static void guardarHistorial(ListaEnlazadaSimple<Compra> historial) {
        PrintWriter writer = crearWriter(DataPaths.HISTORIAL);
        NodoSimple<Compra> actual = historial.getCabeza();
        while (actual != null) {
            Compra compra = actual.getDato();
            StringBuilder detalles = new StringBuilder();
            NodoSimple<CompraDetalle> detalleActual = compra.getDetalles().getCabeza();
            while (detalleActual != null) {
                CompraDetalle detalle = detalleActual.getDato();
                if (detalles.length() > 0) {
                    detalles.append(';');
                }
                detalles.append(detalle.getCodigoJuego()).append('~')
                        .append(detalle.getNombreJuego()).append('~')
                        .append(detalle.getCantidad()).append('~')
                        .append(detalle.getPrecioUnitario());
                detalleActual = detalleActual.getSiguiente();
            }
            writer.println(compra.getFechaHora() + "|" + compra.getTotal() + "|" + detalles);
            actual = actual.getSiguiente();
        }
        writer.close();
    }

    public static MatrizOrtogonal cargarAlbum() {
        Scanner scanner = crearScanner(DataPaths.ALBUM);
        int filas = 6;
        int columnas = 6;
        if (scanner.hasNextLine()) {
            String dimensiones = scanner.nextLine().trim();
            if (!dimensiones.isEmpty()) {
                ListaEnlazadaSimple<String> partes = separar(dimensiones, '|');
                if (partes.tamanio() >= 2) {
                    filas = FormatoUtil.enteroSeguro(partes.obtener(0));
                    columnas = FormatoUtil.enteroSeguro(partes.obtener(1));
                }
            }
        }

        MatrizOrtogonal album = new MatrizOrtogonal(filas, columnas);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 10) {
                int fila = FormatoUtil.enteroSeguro(partes.obtener(0));
                int columna = FormatoUtil.enteroSeguro(partes.obtener(1));
                Carta carta = new Carta(
                        partes.obtener(2),
                        partes.obtener(3),
                        partes.obtener(4),
                        partes.obtener(5),
                        FormatoUtil.enteroSeguro(partes.obtener(6)),
                        FormatoUtil.enteroSeguro(partes.obtener(7)),
                        FormatoUtil.enteroSeguro(partes.obtener(8)),
                        partes.obtener(9));
                album.colocarCarta(fila, columna, carta);
            }
        }
        scanner.close();
        return album;
    }

    public static void guardarAlbum(MatrizOrtogonal album) {
        PrintWriter writer = crearWriter(DataPaths.ALBUM);
        writer.println(album.getFilas() + "|" + album.getColumnas());
        NodoMatriz filaActual = album.getInicio();
        while (filaActual != null) {
            NodoMatriz columnaActual = filaActual;
            while (columnaActual != null) {
                Carta carta = columnaActual.getCarta();
                if (carta != null) {
                    writer.println(columnaActual.getFila() + "|" + columnaActual.getColumna() + "|"
                            + carta.getCodigo() + "|" + carta.getNombre() + "|" + carta.getTipo() + "|"
                            + carta.getRareza() + "|" + carta.getAtaque() + "|" + carta.getDefensa() + "|"
                            + carta.getSalud() + "|" + carta.getRutaImagen());
                }
                columnaActual = columnaActual.getDerecha();
            }
            filaActual = filaActual.getAbajo();
        }
        writer.close();
    }

    public static ListaEnlazadaSimple<Torneo> cargarTorneos() {
        ListaEnlazadaSimple<Torneo> torneos = new ListaEnlazadaSimple<Torneo>();
        Scanner scanner = crearScanner(DataPaths.TORNEOS);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 7) {
                torneos.agregar(new Torneo(
                        partes.obtener(0),
                        partes.obtener(1),
                        partes.obtener(2),
                        partes.obtener(3),
                        partes.obtener(4),
                        FormatoUtil.decimalSeguro(partes.obtener(5)),
                        FormatoUtil.enteroSeguro(partes.obtener(6))));
            }
        }
        scanner.close();
        return torneos;
    }

    public static void guardarTorneos(ListaEnlazadaSimple<Torneo> torneos) {
        PrintWriter writer = crearWriter(DataPaths.TORNEOS);
        NodoSimple<Torneo> actual = torneos.getCabeza();
        while (actual != null) {
            Torneo torneo = actual.getDato();
            writer.println(torneo.getId() + "|" + torneo.getNombre() + "|" + torneo.getJuego() + "|"
                    + torneo.getFecha() + "|" + torneo.getHora() + "|" + torneo.getPrecioTicket() + "|"
                    + torneo.getTicketsDisponibles());
            actual = actual.getSiguiente();
        }
        writer.close();
    }

    public static ListaEnlazadaSimple<TicketVenta> cargarTicketsVendidos() {
        ListaEnlazadaSimple<TicketVenta> tickets = new ListaEnlazadaSimple<TicketVenta>();
        Scanner scanner = crearScanner(DataPaths.TICKETS);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 6) {
                tickets.agregarAlInicio(new TicketVenta(
                        LocalDateTime.parse(partes.obtener(0)),
                        partes.obtener(1),
                        partes.obtener(2),
                        partes.obtener(3),
                        partes.obtener(4),
                        FormatoUtil.decimalSeguro(partes.obtener(5))));
            }
        }
        scanner.close();
        return tickets;
    }

    public static void guardarTicketsVendidos(ListaEnlazadaSimple<TicketVenta> tickets) {
        PrintWriter writer = crearWriter(DataPaths.TICKETS);
        NodoSimple<TicketVenta> actual = tickets.getCabeza();
        while (actual != null) {
            TicketVenta ticket = actual.getDato();
            writer.println(ticket.getFechaHora() + "|" + ticket.getTorneoId() + "|" + ticket.getTorneoNombre()
                    + "|" + ticket.getComprador() + "|" + ticket.getTaquilla() + "|" + ticket.getPrecio());
            actual = actual.getSiguiente();
        }
        writer.close();
    }

    public static ListaEnlazadaSimple<LeaderboardEntry> cargarLeaderboard() {
        ListaEnlazadaSimple<LeaderboardEntry> leaderboard = new ListaEnlazadaSimple<LeaderboardEntry>();
        Scanner scanner = crearScanner(DataPaths.LEADERBOARD);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 2) {
                leaderboard.agregar(new LeaderboardEntry(partes.obtener(0), FormatoUtil.enteroSeguro(partes.obtener(1)), false));
            }
        }
        scanner.close();
        return leaderboard;
    }

    public static void guardarLeaderboard(ListaEnlazadaSimple<LeaderboardEntry> leaderboard) {
        PrintWriter writer = crearWriter(DataPaths.LEADERBOARD);
        NodoSimple<LeaderboardEntry> actual = leaderboard.getCabeza();
        while (actual != null) {
            LeaderboardEntry entry = actual.getDato();
            writer.println(entry.getNombre() + "|" + entry.getXp());
            actual = actual.getSiguiente();
        }
        writer.close();
    }

    public static UsuarioProgreso cargarUsuarioActual(EstudianteInfo estudiante) {
        UsuarioProgreso usuario = new UsuarioProgreso(estudiante.getNombreCompleto(), estudiante.getCarnet());
        Scanner scanner = crearScanner(DataPaths.PERFIL);
        if (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 8) {
                usuario = new UsuarioProgreso(partes.obtener(0), partes.obtener(1));
                usuario.setXp(FormatoUtil.enteroSeguro(partes.obtener(2)));
                usuario.sumarDineroGastado(FormatoUtil.decimalSeguro(partes.obtener(3)));
                usuario.sumarJuegosComprados(FormatoUtil.enteroSeguro(partes.obtener(4)));
                usuario.setCartasEnAlbum(FormatoUtil.enteroSeguro(partes.obtener(5)));
                int legendarias = FormatoUtil.enteroSeguro(partes.obtener(6));
                int filas = FormatoUtil.enteroSeguro(partes.obtener(7));
                for (int i = 0; i < legendarias; i++) {
                    usuario.incrementarCartasLegendarias();
                }
                for (int i = 0; i < filas; i++) {
                    usuario.incrementarFilasCompletas();
                }
                if (partes.tamanio() >= 9 && !partes.obtener(8).isEmpty()) {
                    ListaEnlazadaSimple<String> torneos = separar(partes.obtener(8), ',');
                    for (int i = 0; i < torneos.tamanio(); i++) {
                        if (!torneos.obtener(i).trim().isEmpty()) {
                            usuario.registrarTorneoSiNoExiste(torneos.obtener(i).trim());
                        }
                    }
                }
            }
        }
        scanner.close();
        return usuario;
    }

    public static void guardarUsuarioActual(UsuarioProgreso usuario) {
        PrintWriter writer = crearWriter(DataPaths.PERFIL);
        StringBuilder torneos = new StringBuilder();
        NodoSimple<String> actual = usuario.getTorneosParticipados().getCabeza();
        while (actual != null) {
            if (torneos.length() > 0) {
                torneos.append(',');
            }
            torneos.append(actual.getDato());
            actual = actual.getSiguiente();
        }
        writer.println(usuario.getNombre() + "|" + usuario.getCarnet() + "|" + usuario.getXp() + "|"
                + usuario.getDineroGastado() + "|" + usuario.getJuegosComprados() + "|"
                + usuario.getCartasEnAlbum() + "|" + usuario.getCartasLegendarias() + "|"
                + usuario.getFilasCompletas() + "|" + torneos);
        writer.close();
    }

    public static ListaEnlazadaSimple<Logro> cargarLogros() {
        ListaEnlazadaSimple<Logro> base = crearLogrosBase();
        Scanner scanner = crearScanner(DataPaths.LOGROS);
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 4) {
                Logro logro = buscarLogro(base, partes.obtener(0));
                if (logro != null) {
                    logro.setDesbloqueado(Boolean.parseBoolean(partes.obtener(2)));
                    logro.setFechaDesbloqueo(partes.obtener(3).isEmpty() ? null : partes.obtener(3));
                }
            }
        }
        scanner.close();
        return base;
    }

    public static void guardarLogros(ListaEnlazadaSimple<Logro> logros) {
        PrintWriter writer = crearWriter(DataPaths.LOGROS);
        NodoSimple<Logro> actual = logros.getCabeza();
        while (actual != null) {
            Logro logro = actual.getDato();
            writer.println(logro.getNombre() + "|" + logro.getDescripcion() + "|" + logro.isDesbloqueado() + "|"
                    + (logro.getFechaDesbloqueo() == null ? "" : logro.getFechaDesbloqueo()));
            actual = actual.getSiguiente();
        }
        writer.close();
    }

    public static EstudianteInfo cargarEstudiante() {
        Scanner scanner = crearScanner(DataPaths.ESTUDIANTE);
        EstudianteInfo estudiante = null;
        if (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            ListaEnlazadaSimple<String> partes = separar(linea, '|');
            if (partes.tamanio() >= 7) {
                estudiante = new EstudianteInfo(partes.obtener(0), partes.obtener(1), partes.obtener(2), partes.obtener(3), partes.obtener(4), partes.obtener(5), partes.obtener(6));
            }
        }
        scanner.close();
        return estudiante;
    }

    private static Logro buscarLogro(ListaEnlazadaSimple<Logro> logros, String nombre) {
        NodoSimple<Logro> actual = logros.getCabeza();
        while (actual != null) {
            if (actual.getDato().getNombre().equalsIgnoreCase(nombre)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    private static ListaEnlazadaSimple<Logro> crearLogrosBase() {
        ListaEnlazadaSimple<Logro> logros = new ListaEnlazadaSimple<Logro>();
        logros.agregar(new Logro("Primera Compra", "Realiza tu primera compra en la tienda."));
        logros.agregar(new Logro("Coleccionista Novato", "Agrega 10 cartas a tu album."));
        logros.agregar(new Logro("Coleccionista Experto", "Completa una fila del album."));
        logros.agregar(new Logro("Taquillero", "Compra tickets para 3 torneos distintos."));
        logros.agregar(new Logro("Alta Rareza", "Obtiene una carta de rareza Legendaria."));
        logros.agregar(new Logro("Gamer Dedicado", "Acumula 1,000 XP."));
        logros.agregar(new Logro("Leyenda Viviente", "Alcanza el nivel 5."));
        logros.agregar(new Logro("Gran Gastador", "Gasta mas de Q2,000 en la tienda."));
        return logros;
    }

    private static Scanner crearScanner(Path ruta) {
        try {
            return new Scanner(ruta.toFile(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer el archivo: " + ruta, ex);
        }
    }

    private static PrintWriter crearWriter(Path ruta) {
        try {
            return new PrintWriter(Files.newBufferedWriter(ruta, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo escribir el archivo: " + ruta, ex);
        }
    }

    private static void crearSiNoExiste(Path ruta, String contenido) {
        if (Files.exists(ruta)) {
            return;
        }
        try {
            Files.writeString(ruta, contenido, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo crear el archivo inicial: " + ruta, ex);
        }
    }

    private static ListaEnlazadaSimple<String> separar(String texto, char separador) {
        return separar(texto, separador, 0);
    }

    private static ListaEnlazadaSimple<String> separar(String texto, char separador, int limite) {
        ListaEnlazadaSimple<String> partes = new ListaEnlazadaSimple<String>();
        StringBuilder acumulado = new StringBuilder();
        int cortes = 1;
        for (int i = 0; i < texto.length(); i++) {
            char actual = texto.charAt(i);
            if (actual == separador && (limite <= 0 || cortes < limite)) {
                partes.agregar(acumulado.toString());
                acumulado.setLength(0);
                cortes++;
            } else {
                acumulado.append(actual);
            }
        }
        partes.agregar(acumulado.toString());
        return partes;
    }

    private static String catalogoInicial() {
        return "G001|The Last Hero|Accion|499.99|PC|10|Campana futurista con combates tacticos.\n"
                + "G002|Mystic Arena|RPG|399.50|PlayStation|8|Explora reinos magicos y mejora heroes.\n"
                + "G003|Galaxy Racers|Aventura|289.99|Xbox|12|Carreras espaciales con pistas extremas.\n"
                + "G004|Pixel Strikers|Deportes|179.00|Nintendo Switch|15|Futbol arcade con estilo retro.\n"
                + "G005|Tactical Nexus|Estrategia|459.75|PC|6|Gestiona escuadrones y defiende colonias.\n"
                + "G006|Shadow Dojo|Accion|349.25|PlayStation|9|Combate sigiloso con arte ninja.\n"
                + "G007|Dungeon Pulse|Terror|269.40|Xbox|7|Sobrevive a un laberinto lleno de trampas.\n"
                + "G008|Sky Odyssey|Aventura|299.99|PC|14|Viaja entre islas flotantes y resuelve misterios.";
    }

    private static String cartasIniciales() {
        return "CARTA-001|Draco Solar|Fuego|Legendaria|320|260|280|sin_imagen\n"
                + "CARTA-002|Tortugo Marino|Agua|Rara|180|240|310|sin_imagen\n"
                + "CARTA-003|Guardian Raiz|Planta|Poco Comun|170|220|330|sin_imagen\n"
                + "CARTA-004|Rayo Lynx|Electrico|Rara|260|140|190|sin_imagen\n"
                + "CARTA-005|Vision Arcana|Psiquico|Ultra Rara|290|200|210|sin_imagen\n"
                + "CARTA-006|Lobo Nocturno|Oscuro|Comun|150|150|180|sin_imagen\n"
                + "CARTA-007|Centinela Acero|Acero|Ultra Rara|240|320|260|sin_imagen\n"
                + "CARTA-008|Leon Boreal|Normal|Comun|140|120|160|sin_imagen\n"
                + "CARTA-009|Fenix Carmesi|Fuego|Ultra Rara|310|210|220|sin_imagen\n"
                + "CARTA-010|Kraken Azul|Agua|Legendaria|340|280|350|sin_imagen\n"
                + "CARTA-011|Treant Titan|Planta|Rara|210|260|340|sin_imagen\n"
                + "CARTA-012|Oraculo Prisma|Psiquico|Legendaria|330|230|250|sin_imagen";
    }

    private static String torneosIniciales() {
        return "T001|Copa Heroica|The Last Hero|2026-05-03|18:00|75.00|10\n"
                + "T002|Mystic Masters|Mystic Arena|2026-05-10|15:30|85.00|8\n"
                + "T003|Liga Pixel|Pixel Strikers|2026-05-18|11:00|50.00|12\n"
                + "T004|Desafio Nexus|Tactical Nexus|2026-05-24|16:45|95.00|6";
    }

    private static String leaderboardInicial() {
        return "Luna Vega|4200\n"
                + "Marco Ruiz|3600\n"
                + "Karla Mendez|3200\n"
                + "Axel Prado|2800\n"
                + "Dylan Ortiz|2450\n"
                + "Nadia Soto|2100\n"
                + "Erick Ponce|1750\n"
                + "Paola Lima|1400\n"
                + "Noe Cardona|950\n"
                + "Sara Lopez|720";
    }

    private static String perfilInicial(EstudianteInfo estudiante) {
        return estudiante.getNombreCompleto() + "|" + estudiante.getCarnet() + "|0|0|0|0|0|0|";
    }

    private static String logrosIniciales() {
        ListaEnlazadaSimple<Logro> logros = crearLogrosBase();
        StringBuilder builder = new StringBuilder();
        NodoSimple<Logro> actual = logros.getCabeza();
        while (actual != null) {
            builder.append(actual.getDato().getNombre()).append('|')
                    .append(actual.getDato().getDescripcion()).append('|')
                    .append("false|\n");
            actual = actual.getSiguiente();
        }
        return builder.toString();
    }

    private static String estudianteInicial(EstudianteInfo estudiante) {
        return estudiante.getNombreCompleto() + "|" + estudiante.getCarnet() + "|" + estudiante.getCui() + "|"
                + estudiante.getCorreo() + "|" + estudiante.getSeccion() + "|" + estudiante.getSemestre() + "|"
                + estudiante.getDescripcionProyecto();
    }
}
