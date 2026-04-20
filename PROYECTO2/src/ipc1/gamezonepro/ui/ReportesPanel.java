package ipc1.gamezonepro.ui;

import ipc1.gamezonepro.service.AppContext;
import ipc1.gamezonepro.service.ReporteService.ResultadoReporte;
import ipc1.gamezonepro.util.AppTheme;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ReportesPanel extends JPanel implements RefreshablePanel {

    private final AppContext context;
    private final JLabel ultimaRutaLabel;
    private final JTextArea descripcionArea;

    public ReportesPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.FONDO);

        JLabel titulo = AppTheme.tituloSeccion("Reportes HTML");
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(2, 2, 16, 16));
        centro.setOpaque(false);
        centro.add(crearTarjetaReporte("Inventario de Tienda", "Genera un inventario con stock, precio y plataforma.", "inventario"));
        centro.add(crearTarjetaReporte("Reporte de Ventas", "Resume compras confirmadas, fechas y totales.", "ventas"));
        centro.add(crearTarjetaReporte("Reporte del Album", "Renderiza el album actual y destaca cartas legendarias.", "album"));
        centro.add(crearTarjetaReporte("Reporte de Torneos", "Incluye torneos y tickets vendidos.", "torneos"));
        add(centro, BorderLayout.CENTER);

        JPanel pie = AppTheme.crearTarjeta();
        pie.setLayout(new BorderLayout(8, 8));
        ultimaRutaLabel = new JLabel("Ultimo reporte: aun no se ha generado ninguno.");
        ultimaRutaLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        ultimaRutaLabel.setForeground(AppTheme.MUTED);
        descripcionArea = new JTextArea("Los reportes se guardan en la carpeta INFORMES del repositorio y se intentan abrir automaticamente en el navegador.");
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setLineWrap(true);
        descripcionArea.setEditable(false);
        descripcionArea.setOpaque(false);
        descripcionArea.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        pie.add(descripcionArea, BorderLayout.CENTER);
        pie.add(ultimaRutaLabel, BorderLayout.SOUTH);
        add(pie, BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaReporte(String titulo, String descripcion, String tipo) {
        JPanel tarjeta = AppTheme.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(8, 8));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));
        tituloLabel.setForeground(AppTheme.TEXTO);
        tarjeta.add(tituloLabel, BorderLayout.NORTH);

        JTextArea descripcionLabel = new JTextArea(descripcion);
        descripcionLabel.setWrapStyleWord(true);
        descripcionLabel.setLineWrap(true);
        descripcionLabel.setOpaque(false);
        descripcionLabel.setEditable(false);
        descripcionLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        descripcionLabel.setForeground(AppTheme.MUTED);
        tarjeta.add(descripcionLabel, BorderLayout.CENTER);

        JButton boton = new JButton("Generar Reporte");
        AppTheme.estilizarBotonPrimario(boton);
        boton.addActionListener(e -> generar(tipo));
        tarjeta.add(boton, BorderLayout.SOUTH);
        return tarjeta;
    }

    private void generar(String tipo) {
        ResultadoReporte resultado;
        if ("inventario".equals(tipo)) {
            resultado = context.getReporteService().generarInventario(context.getTiendaService().getCatalogo());
        } else if ("ventas".equals(tipo)) {
            resultado = context.getReporteService().generarVentas(context.getTiendaService().getHistorial());
        } else if ("album".equals(tipo)) {
            resultado = context.getReporteService().generarAlbum(context.getAlbumService().getAlbum());
        } else {
            resultado = context.getReporteService().generarTorneos(context.getTorneoService().getTorneos(), context.getTorneoService().getTicketsVendidos());
        }

        ultimaRutaLabel.setText("Ultimo reporte: " + (resultado.getArchivo() != null ? resultado.getArchivo().toAbsolutePath() : "sin archivo"));
        JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Reportes", resultado.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void refreshData() {
    }
}
