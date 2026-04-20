package ipc1.gamezonepro.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DataPaths {

    public static final Path PROJECT_DIR = resolverDirectorioProyecto();
    public static final Path DATA_DIR = PROJECT_DIR.resolve("data");
    public static final Path REPORT_DIR = resolverDirectorioReportes();

    public static final Path CATALOGO = DATA_DIR.resolve("catalogo.txt");
    public static final Path CARTAS = DATA_DIR.resolve("cartas_catalogo.txt");
    public static final Path HISTORIAL = DATA_DIR.resolve("historial.txt");
    public static final Path ALBUM = DATA_DIR.resolve("album.txt");
    public static final Path TORNEOS = DATA_DIR.resolve("torneos.txt");
    public static final Path TICKETS = DATA_DIR.resolve("tickets_vendidos.txt");
    public static final Path LEADERBOARD = DATA_DIR.resolve("leaderboard.txt");
    public static final Path PERFIL = DATA_DIR.resolve("perfil_usuario.txt");
    public static final Path LOGROS = DATA_DIR.resolve("logros.txt");
    public static final Path ESTUDIANTE = DATA_DIR.resolve("estudiante.txt");

    private DataPaths() {
    }

    private static Path resolverDirectorioProyecto() {
        Path actual = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        if (actual.getFileName() != null && "PROYECTO2".equalsIgnoreCase(actual.getFileName().toString())) {
            return actual;
        }
        Path candidato = actual.resolve("PROYECTO2");
        if (Files.exists(candidato)) {
            return candidato;
        }
        return actual;
    }

    private static Path resolverDirectorioReportes() {
        Path padre = PROJECT_DIR.getParent();
        if (padre != null) {
            return padre.resolve("INFORMES");
        }
        return PROJECT_DIR.resolve("informes_generados");
    }

    public static void asegurarDirectorios() {
        try {
            Files.createDirectories(DATA_DIR);
            Files.createDirectories(REPORT_DIR);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudieron crear los directorios base del proyecto.", ex);
        }
    }
}
