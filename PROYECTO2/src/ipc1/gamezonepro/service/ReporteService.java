package ipc1.gamezonepro.service;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoMatriz;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.datastructures.MatrizOrtogonal;
import ipc1.gamezonepro.model.Carta;
import ipc1.gamezonepro.model.Compra;
import ipc1.gamezonepro.model.CompraDetalle;
import ipc1.gamezonepro.model.TicketVenta;
import ipc1.gamezonepro.model.Torneo;
import ipc1.gamezonepro.model.Videojuego;
import ipc1.gamezonepro.persistence.DataPaths;
import ipc1.gamezonepro.util.FormatoUtil;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class ReporteService {

    public static class ResultadoReporte {

        private final boolean exito;
        private final String mensaje;
        private final Path archivo;

        public ResultadoReporte(boolean exito, String mensaje, Path archivo) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.archivo = archivo;
        }

        public boolean isExito() {
            return exito;
        }

        public String getMensaje() {
            return mensaje;
        }

        public Path getArchivo() {
            return archivo;
        }
    }

    public ResultadoReporte generarInventario(ListaEnlazadaSimple<Videojuego> catalogo) {
        StringBuilder tabla = new StringBuilder();
        NodoSimple<Videojuego> actual = catalogo.getCabeza();
        while (actual != null) {
            Videojuego juego = actual.getDato();
            tabla.append("<tr><td>").append(FormatoUtil.escaparHtml(juego.getCodigo())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(juego.getNombre())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(juego.getGenero())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(juego.getPlataforma())).append("</td>")
                    .append("<td>").append(FormatoUtil.moneda(juego.getPrecio())).append("</td>")
                    .append("<td>").append(juego.getStock()).append("</td></tr>");
            actual = actual.getSiguiente();
        }
        return escribirReporte("Inventario", "<h1>Inventario de Tienda</h1><table><thead><tr><th>Codigo</th><th>Juego</th><th>Genero</th><th>Plataforma</th><th>Precio</th><th>Stock</th></tr></thead><tbody>"
                + tabla + "</tbody></table>");
    }

    public ResultadoReporte generarVentas(ListaEnlazadaSimple<Compra> historial) {
        StringBuilder contenido = new StringBuilder();
        NodoSimple<Compra> actual = historial.getCabeza();
        while (actual != null) {
            Compra compra = actual.getDato();
            contenido.append("<section class='card'><h2>Compra del ")
                    .append(FormatoUtil.fechaHora(compra.getFechaHora()))
                    .append("</h2><table><thead><tr><th>Codigo</th><th>Juego</th><th>Cantidad</th><th>Precio</th><th>Subtotal</th></tr></thead><tbody>");

            NodoSimple<CompraDetalle> detalle = compra.getDetalles().getCabeza();
            while (detalle != null) {
                CompraDetalle d = detalle.getDato();
                contenido.append("<tr><td>").append(FormatoUtil.escaparHtml(d.getCodigoJuego())).append("</td>")
                        .append("<td>").append(FormatoUtil.escaparHtml(d.getNombreJuego())).append("</td>")
                        .append("<td>").append(d.getCantidad()).append("</td>")
                        .append("<td>").append(FormatoUtil.moneda(d.getPrecioUnitario())).append("</td>")
                        .append("<td>").append(FormatoUtil.moneda(d.getSubtotal())).append("</td></tr>");
                detalle = detalle.getSiguiente();
            }

            contenido.append("</tbody></table><p class='total'>Total: ")
                    .append(FormatoUtil.moneda(compra.getTotal())).append("</p></section>");
            actual = actual.getSiguiente();
        }

        if (contenido.length() == 0) {
            contenido.append("<p>No hay compras registradas.</p>");
        }
        return escribirReporte("Ventas", "<h1>Reporte de Ventas</h1>" + contenido);
    }

    public ResultadoReporte generarAlbum(MatrizOrtogonal album) {
        StringBuilder tabla = new StringBuilder();
        NodoMatriz filaActual = album.getInicio();
        while (filaActual != null) {
            tabla.append("<tr>");
            NodoMatriz columnaActual = filaActual;
            while (columnaActual != null) {
                Carta carta = columnaActual.getCarta();
                if (carta == null) {
                    tabla.append("<td class='vacia'>Vacia</td>");
                } else {
                    String clase = "carta";
                    if ("Legendaria".equalsIgnoreCase(carta.getRareza())) {
                        clase += " legendaria";
                    }
                    tabla.append("<td class='").append(clase).append("'><strong>")
                            .append(FormatoUtil.escaparHtml(carta.getNombre()))
                            .append("</strong><br>").append(FormatoUtil.escaparHtml(carta.getTipo()))
                            .append("<br>").append(FormatoUtil.escaparHtml(carta.getRareza()))
                            .append("</td>");
                }
                columnaActual = columnaActual.getDerecha();
            }
            tabla.append("</tr>");
            filaActual = filaActual.getAbajo();
        }

        return escribirReporte("Album", "<h1>Estado del Album</h1><table class='album'><tbody>" + tabla + "</tbody></table>");
    }

    public ResultadoReporte generarTorneos(ListaEnlazadaSimple<Torneo> torneos, ListaEnlazadaSimple<TicketVenta> tickets) {
        StringBuilder torneosTabla = new StringBuilder();
        NodoSimple<Torneo> torneoActual = torneos.getCabeza();
        while (torneoActual != null) {
            Torneo torneo = torneoActual.getDato();
            torneosTabla.append("<tr><td>").append(FormatoUtil.escaparHtml(torneo.getId())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(torneo.getNombre())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(torneo.getJuego())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(torneo.getFecha())).append("</td>")
                    .append("<td>").append(torneo.getTicketsVendidos()).append("</td>")
                    .append("<td>").append(torneo.getTicketsDisponibles()).append("</td></tr>");
            torneoActual = torneoActual.getSiguiente();
        }

        StringBuilder ticketsTabla = new StringBuilder();
        NodoSimple<TicketVenta> ticketActual = tickets.getCabeza();
        while (ticketActual != null) {
            TicketVenta ticket = ticketActual.getDato();
            ticketsTabla.append("<tr><td>").append(FormatoUtil.fechaHora(ticket.getFechaHora())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(ticket.getTorneoNombre())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(ticket.getComprador())).append("</td>")
                    .append("<td>").append(FormatoUtil.escaparHtml(ticket.getTaquilla())).append("</td>")
                    .append("<td>").append(FormatoUtil.moneda(ticket.getPrecio())).append("</td></tr>");
            ticketActual = ticketActual.getSiguiente();
        }
        if (ticketsTabla.length() == 0) {
            ticketsTabla.append("<tr><td colspan='5'>Aun no hay tickets vendidos.</td></tr>");
        }

        String cuerpo = "<h1>Reporte de Torneos</h1>"
                + "<h2>Torneos</h2><table><thead><tr><th>ID</th><th>Nombre</th><th>Juego</th><th>Fecha</th><th>Vendidos</th><th>Disponibles</th></tr></thead><tbody>"
                + torneosTabla + "</tbody></table>"
                + "<h2>Historial de Tickets</h2><table><thead><tr><th>Fecha</th><th>Torneo</th><th>Comprador</th><th>Taquilla</th><th>Precio</th></tr></thead><tbody>"
                + ticketsTabla + "</tbody></table>";
        return escribirReporte("Torneos", cuerpo);
    }

    private ResultadoReporte escribirReporte(String tipo, String cuerpoHtml) {
        LocalDateTime ahora = LocalDateTime.now();
        String nombre = FormatoUtil.fechaHoraArchivo(ahora) + "_" + tipo + ".html";
        Path archivo = DataPaths.REPORT_DIR.resolve(nombre);
        String html = "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Reporte " + tipo + "</title>"
                + "<style>"
                + "body{font-family:Segoe UI,Tahoma,sans-serif;background:#f4f7fb;color:#1f2937;padding:32px;}"
                + "h1,h2{color:#0f766e;}table{width:100%;border-collapse:collapse;margin:16px 0;background:white;}"
                + "th,td{border:1px solid #d7dee8;padding:10px;text-align:left;}th{background:#d9f3ef;}"
                + ".card{background:#fff;border:1px solid #d7dee8;border-radius:14px;padding:18px;margin-bottom:18px;}"
                + ".total{font-weight:bold;color:#0f172a;}.album td{height:88px;width:140px;text-align:center;vertical-align:middle;}"
                + ".vacia{background:#d1d5db;color:#374151;}.legendaria{background:#fef3c7;border:2px solid #f59e0b;}"
                + ".carta{background:#eff6ff;}"
                + "</style></head><body>" + cuerpoHtml + "</body></html>";
        try {
            Files.writeString(archivo, html, StandardCharsets.UTF_8);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(archivo.toUri());
            }
            return new ResultadoReporte(true, "Reporte generado correctamente.", archivo);
        } catch (IOException ex) {
            return new ResultadoReporte(false, "No se pudo generar el reporte: " + ex.getMessage(), archivo);
        }
    }
}
