package ipc1.gamezonepro.service;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.MatrizOrtogonal;
import ipc1.gamezonepro.datastructures.NodoMatriz;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.Carta;

public class AlbumService {

    public static class ResultadoAlbum {

        private final boolean exito;
        private final String mensaje;

        public ResultadoAlbum(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public boolean isExito() {
            return exito;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    private final MatrizOrtogonal album;
    private final ListaEnlazadaSimple<Carta> catalogoCartas;
    private final RecompensasService recompensasService;

    public AlbumService(MatrizOrtogonal album, ListaEnlazadaSimple<Carta> catalogoCartas, RecompensasService recompensasService) {
        this.album = album;
        this.catalogoCartas = catalogoCartas;
        this.recompensasService = recompensasService;
    }

    public MatrizOrtogonal getAlbum() {
        return album;
    }

    public ListaEnlazadaSimple<Carta> getCatalogoCartas() {
        return catalogoCartas;
    }

    public ResultadoAlbum agregarCartaDesdeCatalogo(String codigo) {
        Carta carta = buscarCartaCatalogo(codigo);
        if (carta == null) {
            return new ResultadoAlbum(false, "No se encontro la carta seleccionada en el catalogo.");
        }
        return agregarCarta(carta.copiar());
    }

    public ResultadoAlbum agregarCartaManual(Carta carta) {
        return agregarCarta(carta);
    }

    public boolean intercambiar(int filaUno, int columnaUno, int filaDos, int columnaDos) {
        return album.intercambiar(filaUno, columnaUno, filaDos, columnaDos);
    }

    public int contarCartas() {
        return album.contarCartas();
    }

    public Carta buscarCartaCatalogo(String codigo) {
        NodoSimple<Carta> actual = catalogoCartas.getCabeza();
        while (actual != null) {
            if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean coincideBusqueda(Carta carta, String criterio) {
        if (carta == null) {
            return false;
        }
        String texto = criterio == null ? "" : criterio.trim().toLowerCase();
        if (texto.isEmpty()) {
            return false;
        }
        return carta.getNombre().toLowerCase().contains(texto)
                || carta.getTipo().toLowerCase().contains(texto)
                || carta.getRareza().toLowerCase().contains(texto);
    }

    private ResultadoAlbum agregarCarta(Carta carta) {
        if (carta == null || carta.getCodigo() == null || carta.getCodigo().trim().isEmpty()) {
            return new ResultadoAlbum(false, "Debes proporcionar una carta valida.");
        }
        if (album.contieneCodigo(carta.getCodigo())) {
            return new ResultadoAlbum(false, "La carta con codigo " + carta.getCodigo() + " ya existe en el album.");
        }
        boolean insertada = album.colocarEnPrimeraVacia(carta);
        if (!insertada) {
            return new ResultadoAlbum(false, "El album ya no tiene celdas vacias disponibles.");
        }
        NodoMatriz nodo = buscarNodoPorCodigo(carta.getCodigo());
        boolean filaCompleta = nodo != null && album.filaCompleta(nodo.getFila());
        recompensasService.registrarCartaAgregada(carta, filaCompleta);
        return new ResultadoAlbum(true, "Carta agregada correctamente al album.");
    }

    private NodoMatriz buscarNodoPorCodigo(String codigo) {
        NodoMatriz filaActual = album.getInicio();
        while (filaActual != null) {
            NodoMatriz columnaActual = filaActual;
            while (columnaActual != null) {
                if (columnaActual.getCarta() != null && columnaActual.getCarta().getCodigo().equalsIgnoreCase(codigo)) {
                    return columnaActual;
                }
                columnaActual = columnaActual.getDerecha();
            }
            filaActual = filaActual.getAbajo();
        }
        return null;
    }
}
