package ipc1.gamezonepro.service;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.CarritoItem;
import ipc1.gamezonepro.model.Compra;
import ipc1.gamezonepro.model.CompraDetalle;
import ipc1.gamezonepro.model.Videojuego;
import ipc1.gamezonepro.util.FormatoUtil;
import java.time.LocalDateTime;

public class TiendaService {

    public static class ResultadoCompra {

        private final boolean exito;
        private final String mensaje;
        private final String advertencia;

        public ResultadoCompra(boolean exito, String mensaje, String advertencia) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.advertencia = advertencia;
        }

        public boolean isExito() {
            return exito;
        }

        public String getMensaje() {
            return mensaje;
        }

        public String getAdvertencia() {
            return advertencia;
        }
    }

    private final ListaEnlazadaSimple<Videojuego> catalogo;
    private final ListaEnlazadaSimple<CarritoItem> carrito;
    private final ListaEnlazadaSimple<Compra> historial;
    private final RecompensasService recompensasService;

    public TiendaService(ListaEnlazadaSimple<Videojuego> catalogo, ListaEnlazadaSimple<Compra> historial, RecompensasService recompensasService) {
        this.catalogo = catalogo;
        this.historial = historial;
        this.recompensasService = recompensasService;
        this.carrito = new ListaEnlazadaSimple<CarritoItem>();
    }

    public ListaEnlazadaSimple<Videojuego> getCatalogo() {
        return catalogo;
    }

    public ListaEnlazadaSimple<CarritoItem> getCarrito() {
        return carrito;
    }

    public ListaEnlazadaSimple<Compra> getHistorial() {
        return historial;
    }

    public ListaEnlazadaSimple<Videojuego> filtrarCatalogo(String genero, String plataforma, String busqueda) {
        ListaEnlazadaSimple<Videojuego> filtrados = new ListaEnlazadaSimple<Videojuego>();
        String texto = FormatoUtil.textoSeguro(busqueda).toLowerCase();
        NodoSimple<Videojuego> actual = catalogo.getCabeza();
        while (actual != null) {
            Videojuego juego = actual.getDato();
            boolean coincideGenero = genero == null || genero.isEmpty() || "Todos".equalsIgnoreCase(genero) || juego.getGenero().equalsIgnoreCase(genero);
            boolean coincidePlataforma = plataforma == null || plataforma.isEmpty() || "Todas".equalsIgnoreCase(plataforma) || juego.getPlataforma().equalsIgnoreCase(plataforma);
            boolean coincideTexto = texto.isEmpty()
                    || juego.getNombre().toLowerCase().contains(texto)
                    || juego.getCodigo().toLowerCase().contains(texto);
            if (coincideGenero && coincidePlataforma && coincideTexto) {
                filtrados.agregar(juego);
            }
            actual = actual.getSiguiente();
        }
        return filtrados;
    }

    public void agregarAlCarrito(Videojuego videojuego) {
        NodoSimple<CarritoItem> actual = carrito.getCabeza();
        while (actual != null) {
            CarritoItem item = actual.getDato();
            if (item.getVideojuego().getCodigo().equalsIgnoreCase(videojuego.getCodigo())) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
            actual = actual.getSiguiente();
        }
        carrito.agregar(new CarritoItem(videojuego, 1));
    }

    public void actualizarCantidad(int indice, int cantidad) {
        if (cantidad <= 0) {
            eliminarItemCarrito(indice);
            return;
        }
        carrito.obtener(indice).setCantidad(cantidad);
    }

    public void eliminarItemCarrito(int indice) {
        if (indice >= 0 && indice < carrito.tamanio()) {
            carrito.eliminarEn(indice);
        }
    }

    public double getTotalCarrito() {
        double total = 0;
        NodoSimple<CarritoItem> actual = carrito.getCabeza();
        while (actual != null) {
            total += actual.getDato().getSubtotal();
            actual = actual.getSiguiente();
        }
        return total;
    }

    public ResultadoCompra confirmarCompra() {
        if (carrito.estaVacia()) {
            return new ResultadoCompra(false, "El carrito esta vacio.", "");
        }

        ListaEnlazadaSimple<CompraDetalle> detalles = new ListaEnlazadaSimple<CompraDetalle>();
        StringBuilder advertencias = new StringBuilder();
        NodoSimple<CarritoItem> actual = carrito.getCabeza();
        double total = 0;
        int juegosProcesados = 0;

        while (actual != null) {
            CarritoItem item = actual.getDato();
            Videojuego juego = item.getVideojuego();
            if (item.getCantidad() > 0 && juego.getStock() >= item.getCantidad()) {
                juego.setStock(juego.getStock() - item.getCantidad());
                detalles.agregar(new CompraDetalle(juego.getCodigo(), juego.getNombre(), item.getCantidad(), juego.getPrecio()));
                total += item.getSubtotal();
                juegosProcesados += item.getCantidad();
            } else {
                if (advertencias.length() > 0) {
                    advertencias.append('\n');
                }
                advertencias.append("- ").append(juego.getNombre()).append(" no pudo procesarse por stock insuficiente.");
            }
            actual = actual.getSiguiente();
        }

        carrito.limpiar();

        if (detalles.estaVacia()) {
            return new ResultadoCompra(false, "No se pudo confirmar ninguna compra.", advertencias.toString());
        }

        Compra compra = new Compra(LocalDateTime.now(), detalles, total);
        historial.agregarAlInicio(compra);
        recompensasService.registrarCompra(total, juegosProcesados);

        return new ResultadoCompra(true,
                "Compra confirmada por " + FormatoUtil.moneda(total) + ".",
                advertencias.toString());
    }
}
