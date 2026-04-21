package ipc1.gamezonepro.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public final class AppTheme {

    public static final Color FONDO = new Color(243, 247, 251);
    public static final Color PANEL = Color.WHITE;
    public static final Color PRINCIPAL = new Color(15, 118, 110);
    public static final Color PRINCIPAL_OSCURO = new Color(13, 88, 82);
    public static final Color SECUNDARIO = new Color(14, 116, 144);
    public static final Color TEXTO = new Color(31, 41, 55);
    public static final Color MUTED = new Color(107, 114, 128);
    public static final Color BORDE = new Color(209, 219, 231);
    public static final Color ALERTA = new Color(190, 24, 93);
    public static final Color DORADO = new Color(245, 158, 11);
    public static final Color INFO = new Color(37, 99, 235);
    public static final Color PELIGRO = new Color(220, 38, 38);

    private AppTheme() {
    }

    public static Border bordePanel() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(14, 14, 14, 14));
    }

    public static Border bordeSeleccionado(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    public static JLabel tituloSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(TEXTO);
        return label;
    }

    public static JLabel subtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(MUTED);
        return label;
    }

    public static JPanel crearTarjeta() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(PANEL);
        panel.setBorder(bordePanel());
        return panel;
    }

    public static void estilizarBotonPrimario(JButton boton) {
        estilizarBotonExito(boton);
    }

    public static void estilizarBotonExito(JButton boton) {
        prepararBotonBase(boton, new Color(220, 252, 231), PRINCIPAL);
    }

    public static void estilizarBotonSecundario(JButton boton) {
        estilizarBotonNeutro(boton);
    }

    public static void estilizarBotonNeutro(JButton boton) {
        prepararBotonBase(boton, new Color(248, 250, 252), BORDE);
    }

    public static void estilizarBotonInfo(JButton boton) {
        prepararBotonBase(boton, new Color(219, 234, 254), INFO);
    }

    public static void estilizarBotonAdvertencia(JButton boton) {
        prepararBotonBase(boton, new Color(254, 243, 199), new Color(217, 119, 6));
    }

    public static void estilizarBotonPeligro(JButton boton) {
        prepararBotonBase(boton, new Color(254, 226, 226), PELIGRO);
    }

    public static void estilizarBotonMenu(JButton boton) {
        prepararBotonMenuBase(boton, new Color(239, 244, 250), new Color(191, 219, 254));
    }

    public static void estilizarBotonMenuActivo(JButton boton) {
        prepararBotonMenuBase(boton, new Color(219, 234, 254), INFO);
    }

    public static void estilizarBotonMenuPeligro(JButton boton) {
        prepararBotonMenuBase(boton, new Color(254, 226, 226), PELIGRO);
    }

    private static void prepararBotonMenuBase(JButton boton, Color fondo, Color borde) {
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        prepararBotonBase(boton, fondo, borde);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private static void prepararBotonBase(JButton boton, Color fondo, Color borde) {
        boton.setBackground(fondo);
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(true);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void prepararContenedor(JComponent component) {
        component.setBackground(FONDO);
        component.setForeground(TEXTO);
    }
}
