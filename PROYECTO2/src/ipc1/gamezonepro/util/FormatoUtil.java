package ipc1.gamezonepro.util;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FormatoUtil {

    private static final DecimalFormat MONEDA = new DecimalFormat("Q#,##0.00");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter ARCHIVO = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");

    private FormatoUtil() {
    }

    public static String moneda(double valor) {
        return MONEDA.format(valor);
    }

    public static String fechaHora(LocalDateTime fechaHora) {
        return FECHA_HORA.format(fechaHora);
    }

    public static String fechaHoraArchivo(LocalDateTime fechaHora) {
        return ARCHIVO.format(fechaHora);
    }

    public static int enteroSeguro(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    public static double decimalSeguro(String texto) {
        try {
            return Double.parseDouble(texto.trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String textoSeguro(String valor) {
        return valor == null ? "" : valor.trim();
    }

    public static String escaparHtml(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
