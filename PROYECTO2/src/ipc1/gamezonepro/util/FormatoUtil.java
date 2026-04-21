package ipc1.gamezonepro.util;

import java.time.LocalDateTime;

public final class FormatoUtil {

    private FormatoUtil() {
    }

    public static String moneda(double valor) {
        boolean negativo = valor < 0;
        long centavos = Math.round(Math.abs(valor) * 100.0);
        long parteEntera = centavos / 100;
        long parteDecimal = centavos % 100;
        String texto = "Q" + formatearMiles(parteEntera) + "." + dosDigitos((int) parteDecimal);
        if (negativo) {
            return "-" + texto;
        }
        return texto;
    }

    public static String fechaHora(LocalDateTime fechaHora) {
        return dosDigitos(fechaHora.getDayOfMonth()) + "/"
                + dosDigitos(fechaHora.getMonthValue()) + "/"
                + fechaHora.getYear() + " "
                + dosDigitos(fechaHora.getHour()) + ":"
                + dosDigitos(fechaHora.getMinute()) + ":"
                + dosDigitos(fechaHora.getSecond());
    }

    public static String fechaHoraArchivo(LocalDateTime fechaHora) {
        return dosDigitos(fechaHora.getDayOfMonth()) + "_"
                + dosDigitos(fechaHora.getMonthValue()) + "_"
                + fechaHora.getYear() + "_"
                + dosDigitos(fechaHora.getHour()) + "_"
                + dosDigitos(fechaHora.getMinute()) + "_"
                + dosDigitos(fechaHora.getSecond());
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

    private static String dosDigitos(int valor) {
        if (valor < 10) {
            return "0" + valor;
        }
        return String.valueOf(valor);
    }

    private static String formatearMiles(long valor) {
        String texto = String.valueOf(valor);
        StringBuilder invertido = new StringBuilder();
        int contador = 0;
        for (int i = texto.length() - 1; i >= 0; i--) {
            invertido.append(texto.charAt(i));
            contador++;
            if (contador == 3 && i > 0) {
                invertido.append(',');
                contador = 0;
            }
        }
        return invertido.reverse().toString();
    }
}
