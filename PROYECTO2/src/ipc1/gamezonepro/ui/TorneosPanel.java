package ipc1.gamezonepro.ui;

import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.TicketVenta;
import ipc1.gamezonepro.model.Torneo;
import ipc1.gamezonepro.service.TorneoService;
import ipc1.gamezonepro.util.AppTheme;
import ipc1.gamezonepro.util.FormatoUtil;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

public class TorneosPanel extends JPanel implements RefreshablePanel {

    private final TorneoService service;
    private final Runnable mutationCallback;
    private final TorneosTableModel torneosTableModel;
    private final TicketsTableModel ticketsTableModel;
    private final JTable torneosTable;
    private final JTextArea detalleArea;
    private final JTextArea colaArea;
    private final JTextArea logArea;
    private final JLabel taquillaUnoLabel;
    private final JLabel taquillaDosLabel;
    private final JTextField nombreUsuarioField;
    private boolean actualizandoSeleccion;

    public TorneosPanel(TorneoService service, String nombreActual, Runnable mutationCallback) {
        this.service = service;
        this.mutationCallback = mutationCallback;
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.FONDO);

        JLabel titulo = AppTheme.tituloSeccion("Eventos Especiales y Taquillas Concurrentes");
        add(titulo, BorderLayout.NORTH);

        torneosTableModel = new TorneosTableModel();
        torneosTable = new JTable(torneosTableModel);
        torneosTable.setRowHeight(28);
        torneosTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        torneosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !actualizandoSeleccion) {
                actualizarTorneoSeleccionado();
            }
        });

        JPanel lateral = AppTheme.crearTarjeta();
        lateral.setLayout(new BorderLayout(8, 8));
        detalleArea = new JTextArea();
        detalleArea.setEditable(false);
        detalleArea.setOpaque(false);
        detalleArea.setWrapStyleWord(true);
        detalleArea.setLineWrap(true);
        detalleArea.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        nombreUsuarioField = new JTextField(nombreActual);
        JButton inscribirButton = new JButton("Inscribirse a la Cola");
        JButton iniciarButton = new JButton("Iniciar Taquillas");
        AppTheme.estilizarBotonSecundario(inscribirButton);
        AppTheme.estilizarBotonPrimario(iniciarButton);
        JPanel acciones = new JPanel(new GridLayout(0, 1, 8, 8));
        acciones.setOpaque(false);
        acciones.add(new JLabel("Nombre del participante"));
        acciones.add(nombreUsuarioField);
        acciones.add(inscribirButton);
        acciones.add(iniciarButton);
        taquillaUnoLabel = new JLabel("Taquilla 1: Libre");
        taquillaDosLabel = new JLabel("Taquilla 2: Libre");
        acciones.add(taquillaUnoLabel);
        acciones.add(taquillaDosLabel);
        lateral.add(new JScrollPane(detalleArea), BorderLayout.CENTER);
        lateral.add(acciones, BorderLayout.SOUTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 16, 16));
        centro.setOpaque(false);
        centro.add(new JScrollPane(torneosTable));
        centro.add(lateral);
        add(centro, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new GridLayout(1, 2, 16, 16));
        inferior.setOpaque(false);

        JPanel colaPanel = AppTheme.crearTarjeta();
        colaPanel.setLayout(new BorderLayout(8, 8));
        colaPanel.add(new JLabel("Cola restante"), BorderLayout.NORTH);
        colaArea = new JTextArea();
        colaArea.setEditable(false);
        colaArea.setWrapStyleWord(true);
        colaArea.setLineWrap(true);
        colaPanel.add(new JScrollPane(colaArea), BorderLayout.CENTER);
        inferior.add(colaPanel);

        JPanel logPanel = AppTheme.crearTarjeta();
        logPanel.setLayout(new BorderLayout(8, 8));
        logPanel.add(new JLabel("Log de ventas y tickets"), BorderLayout.NORTH);
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setWrapStyleWord(true);
        logArea.setLineWrap(true);
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        ticketsTableModel = new TicketsTableModel();
        JTable ticketsTable = new JTable(ticketsTableModel);
        ticketsTable.setRowHeight(26);
        logPanel.add(new JScrollPane(ticketsTable), BorderLayout.SOUTH);
        inferior.add(logPanel);

        add(inferior, BorderLayout.SOUTH);

        inscribirButton.addActionListener(e -> inscribir());
        iniciarButton.addActionListener(e -> iniciarVenta());

        service.setVentaListener(new TorneoService.VentaListener() {
            @Override
            public void onCambioEstado() {
                mutationCallback.run();
                refreshData();
            }
        });

        if (torneosTableModel.getRowCount() > 0) {
            torneosTable.setRowSelectionInterval(0, 0);
        }
        refreshData();
    }

    private Torneo getSeleccionado() {
        int fila = torneosTable.getSelectedRow();
        if (fila < 0 || fila >= service.getTorneos().tamanio()) {
            return null;
        }
        return service.getTorneos().obtener(fila);
    }

    private void inscribir() {
        String mensaje = service.inscribirUsuario(getSeleccionado(), nombreUsuarioField.getText());
        mutationCallback.run();
        refreshData();
        JOptionPane.showMessageDialog(this, mensaje, "Torneos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void iniciarVenta() {
        String mensaje = service.iniciarVenta(getSeleccionado());
        mutationCallback.run();
        refreshData();
        JOptionPane.showMessageDialog(this, mensaje, "Torneos", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void refreshData() {
        int filaSeleccionada = torneosTable.getSelectedRow();
        torneosTableModel.fireTableDataChanged();
        ticketsTableModel.fireTableDataChanged();
        restaurarSeleccion(filaSeleccionada);
        actualizarTorneoSeleccionado();
        taquillaUnoLabel.setText("Taquilla 1: " + service.getEstadoTaquillaUno());
        taquillaDosLabel.setText("Taquilla 2: " + service.getEstadoTaquillaDos());
        logArea.setText(construirLog());
    }

    private void restaurarSeleccion(int filaSeleccionada) {
        int totalTorneos = service.getTorneos().tamanio();
        if (totalTorneos <= 0) {
            actualizandoSeleccion = true;
            torneosTable.clearSelection();
            actualizandoSeleccion = false;
            return;
        }

        int filaObjetivo = filaSeleccionada;
        if (filaObjetivo < 0) {
            filaObjetivo = 0;
        }
        if (filaObjetivo >= totalTorneos) {
            filaObjetivo = totalTorneos - 1;
        }

        actualizandoSeleccion = true;
        torneosTable.getSelectionModel().setSelectionInterval(filaObjetivo, filaObjetivo);
        actualizandoSeleccion = false;
    }

    private void actualizarTorneoSeleccionado() {
        Torneo torneo = getSeleccionado();
        if (torneo != null) {
            detalleArea.setText("ID: " + torneo.getId()
                    + "\nNombre: " + torneo.getNombre()
                    + "\nJuego: " + torneo.getJuego()
                    + "\nFecha: " + torneo.getFecha()
                    + "\nHora: " + torneo.getHora()
                    + "\nPrecio Ticket: " + FormatoUtil.moneda(torneo.getPrecioTicket())
                    + "\nDisponibles: " + torneo.getTicketsDisponibles()
                    + "\nVendidos: " + torneo.getTicketsVendidos());
            colaArea.setText(torneo.getColaEspera().representarComoTexto());
        } else {
            detalleArea.setText("Selecciona un torneo.");
            colaArea.setText("Sin torneo seleccionado.");
        }
    }

    private String construirLog() {
        StringBuilder builder = new StringBuilder();
        NodoSimple<String> actual = service.getLogEventos().getCabeza();
        while (actual != null) {
            builder.append(actual.getDato()).append('\n');
            actual = actual.getSiguiente();
        }
        return builder.toString();
    }

    private class TorneosTableModel extends AbstractTableModel {

        private final String[] columnas = {"ID", "Torneo", "Juego", "Fecha", "Precio", "Disponibles"};

        @Override
        public int getRowCount() {
            return service.getTorneos().tamanio();
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
            Torneo torneo = service.getTorneos().obtener(rowIndex);
            if (columnIndex == 0) {
                return torneo.getId();
            }
            if (columnIndex == 1) {
                return torneo.getNombre();
            }
            if (columnIndex == 2) {
                return torneo.getJuego();
            }
            if (columnIndex == 3) {
                return torneo.getFecha() + " " + torneo.getHora();
            }
            if (columnIndex == 4) {
                return FormatoUtil.moneda(torneo.getPrecioTicket());
            }
            return torneo.getTicketsDisponibles();
        }
    }

    private class TicketsTableModel extends AbstractTableModel {

        private final String[] columnas = {"Fecha", "Torneo", "Comprador", "Taquilla", "Precio"};

        @Override
        public int getRowCount() {
            return service.getTicketsVendidos().tamanio();
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
            TicketVenta ticket = service.getTicketsVendidos().obtener(rowIndex);
            if (columnIndex == 0) {
                return FormatoUtil.fechaHora(ticket.getFechaHora());
            }
            if (columnIndex == 1) {
                return ticket.getTorneoNombre();
            }
            if (columnIndex == 2) {
                return ticket.getComprador();
            }
            if (columnIndex == 3) {
                return ticket.getTaquilla();
            }
            return FormatoUtil.moneda(ticket.getPrecio());
        }
    }
}
