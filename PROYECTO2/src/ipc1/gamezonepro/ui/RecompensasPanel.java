package ipc1.gamezonepro.ui;

import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.LeaderboardEntry;
import ipc1.gamezonepro.model.Logro;
import ipc1.gamezonepro.model.UsuarioProgreso;
import ipc1.gamezonepro.service.RecompensasService;
import ipc1.gamezonepro.util.AppTheme;
import ipc1.gamezonepro.util.FormatoUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class RecompensasPanel extends JPanel implements RefreshablePanel {

    private final RecompensasService service;
    private final JLabel xpLabel;
    private final JLabel nivelLabel;
    private final JLabel rangoLabel;
    private final JLabel posicionLabel;
    private final JProgressBar progressBar;
    private final JPanel logrosContainer;
    private final LeaderboardTableModel leaderboardTableModel;

    public RecompensasPanel(RecompensasService service) {
        this.service = service;
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.FONDO);

        JLabel titulo = AppTheme.tituloSeccion("Recompensas y Tablero de Lideres");
        add(titulo, BorderLayout.NORTH);

        JPanel resumen = new JPanel(new GridLayout(1, 4, 12, 12));
        resumen.setOpaque(false);
        xpLabel = crearResumenCard(resumen, "XP Acumulado");
        nivelLabel = crearResumenCard(resumen, "Nivel Actual");
        rangoLabel = crearResumenCard(resumen, "Rango");
        posicionLabel = crearResumenCard(resumen, "Posicion Global");

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(AppTheme.PRINCIPAL);
        progressBar.setBackground(new Color(226, 232, 240));
        progressBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel top = new JPanel(new BorderLayout(12, 12));
        top.setOpaque(false);
        top.add(resumen, BorderLayout.NORTH);
        top.add(progressBar, BorderLayout.SOUTH);
        add(top, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new GridLayout(1, 2, 16, 16));
        inferior.setOpaque(false);

        JPanel logrosPanel = AppTheme.crearTarjeta();
        logrosPanel.setLayout(new BorderLayout(8, 8));
        JLabel logrosTitulo = new JLabel("Logros Desbloqueables");
        logrosTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        logrosTitulo.setForeground(AppTheme.TEXTO);
        logrosPanel.add(logrosTitulo, BorderLayout.NORTH);
        logrosContainer = new JPanel();
        logrosContainer.setOpaque(false);
        logrosContainer.setLayout(new BoxLayout(logrosContainer, BoxLayout.Y_AXIS));
        logrosPanel.add(new JScrollPane(logrosContainer), BorderLayout.CENTER);
        inferior.add(logrosPanel);

        JPanel leaderboardPanel = AppTheme.crearTarjeta();
        leaderboardPanel.setLayout(new BorderLayout(8, 8));
        JLabel tablaTitulo = new JLabel("Leaderboard");
        tablaTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        tablaTitulo.setForeground(AppTheme.TEXTO);
        leaderboardPanel.add(tablaTitulo, BorderLayout.NORTH);
        leaderboardTableModel = new LeaderboardTableModel();
        JTable tabla = new JTable(leaderboardTableModel);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setDefaultRenderer(Object.class, new LeaderboardRenderer());
        leaderboardPanel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        inferior.add(leaderboardPanel);

        add(inferior, BorderLayout.SOUTH);
        refreshData();
    }

    private JLabel crearResumenCard(JPanel contenedor, String titulo) {
        JPanel card = AppTheme.crearTarjeta();
        card.setLayout(new BorderLayout(4, 4));
        JLabel title = new JLabel(titulo);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        title.setForeground(AppTheme.MUTED);
        JLabel value = new JLabel("-");
        value.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        value.setForeground(AppTheme.TEXTO);
        card.add(title, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        contenedor.add(card);
        return value;
    }

    @Override
    public void refreshData() {
        UsuarioProgreso usuario = service.getUsuario();
        xpLabel.setText(String.valueOf(usuario.getXp()));
        nivelLabel.setText("Nivel " + service.getNivel());
        rangoLabel.setText(service.getRango());
        posicionLabel.setText("#" + service.obtenerPosicionUsuario());
        progressBar.setValue(service.getPorcentajeProgresoNivel());
        progressBar.setString(service.getPorcentajeProgresoNivel() + "% hacia el siguiente nivel");
        renderizarLogros();
        leaderboardTableModel.fireTableDataChanged();
    }

    private void renderizarLogros() {
        logrosContainer.removeAll();
        NodoSimple<Logro> actual = service.getLogros().getCabeza();
        while (actual != null) {
            Logro logro = actual.getDato();
            JPanel tarjeta = new JPanel(new BorderLayout(6, 6));
            tarjeta.setOpaque(true);
            tarjeta.setBackground(logro.isDesbloqueado() ? new Color(220, 252, 231) : new Color(248, 250, 252));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(logro.isDesbloqueado() ? AppTheme.PRINCIPAL : AppTheme.BORDE),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)));
            JLabel nombre = new JLabel((logro.isDesbloqueado() ? "Desbloqueado: " : "Bloqueado: ") + logro.getNombre());
            nombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            nombre.setForeground(AppTheme.TEXTO);
            JLabel descripcion = new JLabel("<html>" + logro.getDescripcion()
                    + (logro.getFechaDesbloqueo() != null ? "<br><span style='color:#0f766e'>Fecha: " + logro.getFechaDesbloqueo() + "</span>" : "") + "</html>");
            descripcion.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
            descripcion.setForeground(AppTheme.MUTED);
            tarjeta.add(nombre, BorderLayout.NORTH);
            tarjeta.add(descripcion, BorderLayout.CENTER);
            logrosContainer.add(tarjeta);
            actual = actual.getSiguiente();
        }
        logrosContainer.revalidate();
        logrosContainer.repaint();
    }

    private class LeaderboardTableModel extends AbstractTableModel {

        private final String[] columnas = {"#", "Jugador", "XP"};

        @Override
        public int getRowCount() {
            LeaderboardEntry[] ranking = service.obtenerRankingCompletoOrdenado();
            if (ranking.length <= 10) {
                return ranking.length;
            }
            return service.obtenerPosicionUsuario() > 10 ? 11 : 10;
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            LeaderboardEntry entry = obtenerEntry(rowIndex);
            int posicion = obtenerPosicion(rowIndex);
            if (columnIndex == 0) {
                return posicion;
            }
            if (columnIndex == 1) {
                return entry.getNombre();
            }
            return entry.getXp();
        }

        private LeaderboardEntry obtenerEntry(int fila) {
            LeaderboardEntry[] ranking = service.obtenerRankingCompletoOrdenado();
            if (ranking.length == 0) {
                return new LeaderboardEntry("-", 0, false);
            }
            if (fila < 10 && fila < ranking.length) {
                return ranking[fila];
            }
            int posicionUsuario = service.obtenerPosicionUsuario();
            if (posicionUsuario > 10 && posicionUsuario - 1 < ranking.length) {
                return ranking[posicionUsuario - 1];
            }
            return ranking[ranking.length - 1];
        }

        private int obtenerPosicion(int fila) {
            if (fila < 10) {
                return fila + 1;
            }
            return service.obtenerPosicionUsuario();
        }
    }

    private class LeaderboardRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            LeaderboardEntry entry = leaderboardTableModel.obtenerEntry(row);
            if (entry.isUsuarioActual()) {
                component.setBackground(new Color(224, 242, 254));
                component.setForeground(AppTheme.SECUNDARIO);
            } else {
                component.setBackground(Color.WHITE);
                component.setForeground(AppTheme.TEXTO);
            }
            return component;
        }
    }
}
