package ipc1.gamezonepro.ui;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.CarritoItem;
import ipc1.gamezonepro.model.Compra;
import ipc1.gamezonepro.model.CompraDetalle;
import ipc1.gamezonepro.model.Videojuego;
import ipc1.gamezonepro.service.TiendaService;
import ipc1.gamezonepro.service.TiendaService.ResultadoCompra;
import ipc1.gamezonepro.util.AppTheme;
import ipc1.gamezonepro.util.FormatoUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;

public class TiendaPanel extends JPanel implements RefreshablePanel {

    private final TiendaService service;
    private final Runnable mutationCallback;
    private final JComboBox<String> generoCombo;
    private final JComboBox<String> plataformaCombo;
    private final JTextField busquedaField;
    private final JPanel catalogoGrid;
    private final JTextArea detalleArea;
    private final JLabel detalleTitulo;
    private final JButton agregarButton;
    private final JLabel totalCarritoLabel;
    private final CartTableModel cartTableModel;
    private final HistoryTableModel historyTableModel;
    private Videojuego seleccionado;

    public TiendaPanel(TiendaService service, Runnable mutationCallback) {
        this.service = service;
        this.mutationCallback = mutationCallback;

        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.FONDO);

        JPanel filtros = AppTheme.crearTarjeta();
        filtros.setLayout(new GridLayout(2, 3, 10, 10));
        filtros.add(AppTheme.subtitulo("Genero"));
        filtros.add(AppTheme.subtitulo("Plataforma"));
        filtros.add(AppTheme.subtitulo("Buscar por nombre o codigo"));
        generoCombo = new JComboBox<String>();
        plataformaCombo = new JComboBox<String>();
        cargarOpcionesGenero();
        cargarOpcionesPlataforma();
        busquedaField = new JTextField();
        filtros.add(generoCombo);
        filtros.add(plataformaCombo);
        filtros.add(busquedaField);
        add(filtros, BorderLayout.NORTH);

        catalogoGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        catalogoGrid.setOpaque(false);
        JScrollPane catalogoScroll = new JScrollPane(catalogoGrid);
        catalogoScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel detallePanel = AppTheme.crearTarjeta();
        detallePanel.setLayout(new BorderLayout(8, 8));
        detalleTitulo = new JLabel("Selecciona un juego");
        detalleTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        detalleTitulo.setForeground(AppTheme.TEXTO);
        detalleArea = new JTextArea();
        detalleArea.setWrapStyleWord(true);
        detalleArea.setLineWrap(true);
        detalleArea.setEditable(false);
        detalleArea.setOpaque(false);
        detalleArea.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        agregarButton = new JButton("Agregar al carrito");
        AppTheme.estilizarBotonExito(agregarButton);
        agregarButton.setEnabled(false);
        agregarButton.addActionListener(e -> {
            if (seleccionado != null) {
                service.agregarAlCarrito(seleccionado);
                mutationCallback.run();
                refreshData();
            }
        });
        detallePanel.add(detalleTitulo, BorderLayout.NORTH);
        detallePanel.add(new JScrollPane(detalleArea), BorderLayout.CENTER);
        detallePanel.add(agregarButton, BorderLayout.SOUTH);

        JPanel carritoPanel = AppTheme.crearTarjeta();
        carritoPanel.setLayout(new BorderLayout(8, 8));
        JLabel carritoTitulo = new JLabel("Carrito");
        carritoTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        carritoTitulo.setForeground(AppTheme.TEXTO);
        cartTableModel = new CartTableModel();
        JTable carritoTable = new JTable(cartTableModel);
        carritoTable.setRowHeight(28);
        totalCarritoLabel = new JLabel("Total: Q0.00");
        totalCarritoLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        JButton eliminarButton = new JButton("Eliminar item");
        JButton confirmarButton = new JButton("Confirmar compra");
        AppTheme.estilizarBotonPeligro(eliminarButton);
        AppTheme.estilizarBotonExito(confirmarButton);
        eliminarButton.addActionListener(e -> {
            int fila = carritoTable.getSelectedRow();
            if (fila >= 0) {
                service.eliminarItemCarrito(fila);
                mutationCallback.run();
                refreshData();
            }
        });
        confirmarButton.addActionListener(e -> confirmarCompra());
        JPanel accionesCarrito = new JPanel(new GridLayout(1, 2, 8, 8));
        accionesCarrito.setOpaque(false);
        accionesCarrito.add(eliminarButton);
        accionesCarrito.add(confirmarButton);
        carritoPanel.add(carritoTitulo, BorderLayout.NORTH);
        carritoPanel.add(new JScrollPane(carritoTable), BorderLayout.CENTER);
        JPanel pieCarrito = new JPanel(new BorderLayout());
        pieCarrito.setOpaque(false);
        pieCarrito.add(totalCarritoLabel, BorderLayout.NORTH);
        pieCarrito.add(accionesCarrito, BorderLayout.SOUTH);
        carritoPanel.add(pieCarrito, BorderLayout.SOUTH);

        JPanel historialPanel = AppTheme.crearTarjeta();
        historialPanel.setLayout(new BorderLayout(8, 8));
        JLabel historialTitulo = new JLabel("Historial de Compras");
        historialTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        historialTitulo.setForeground(AppTheme.TEXTO);
        historyTableModel = new HistoryTableModel();
        JTable historialTable = new JTable(historyTableModel);
        historialTable.setRowHeight(28);
        historialPanel.add(historialTitulo, BorderLayout.NORTH);
        historialPanel.add(new JScrollPane(historialTable), BorderLayout.CENTER);

        JPanel derecha = new JPanel(new GridLayout(3, 1, 12, 12));
        derecha.setOpaque(false);
        derecha.add(detallePanel);
        derecha.add(carritoPanel);
        derecha.add(historialPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, catalogoScroll, derecha);
        splitPane.setDividerLocation(700);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        add(splitPane, BorderLayout.CENTER);

        configurarFiltros();
        refreshData();
    }

    private void cargarOpcionesGenero() {
        generoCombo.addItem("Todos");
        generoCombo.addItem("Accion");
        generoCombo.addItem("RPG");
        generoCombo.addItem("Estrategia");
        generoCombo.addItem("Deportes");
        generoCombo.addItem("Terror");
        generoCombo.addItem("Aventura");
    }

    private void cargarOpcionesPlataforma() {
        plataformaCombo.addItem("Todas");
        plataformaCombo.addItem("PC");
        plataformaCombo.addItem("PlayStation");
        plataformaCombo.addItem("Xbox");
        plataformaCombo.addItem("Nintendo Switch");
    }

    private void configurarFiltros() {
        generoCombo.addActionListener(e -> renderizarCatalogo());
        plataformaCombo.addActionListener(e -> renderizarCatalogo());
        busquedaField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                renderizarCatalogo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                renderizarCatalogo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                renderizarCatalogo();
            }
        });
    }

    private void confirmarCompra() {
        ResultadoCompra resultado = service.confirmarCompra();
        mutationCallback.run();
        refreshData();
        String mensaje = resultado.getMensaje();
        if (!resultado.getAdvertencia().isEmpty()) {
            mensaje += "\n\nAdvertencias:\n" + resultado.getAdvertencia();
        }
        JOptionPane.showMessageDialog(this, mensaje, "Tienda", resultado.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private void renderizarCatalogo() {
        catalogoGrid.removeAll();
        ListaEnlazadaSimple<Videojuego> juegos = service.filtrarCatalogo(
                (String) generoCombo.getSelectedItem(),
                (String) plataformaCombo.getSelectedItem(),
                busquedaField.getText());
        NodoSimple<Videojuego> actual = juegos.getCabeza();
        while (actual != null) {
            Videojuego juego = actual.getDato();
            JPanel card = new JPanel(new BorderLayout(6, 6));
            card.setOpaque(true);
            card.setBackground(Color.WHITE);
            boolean seleccionadoActual = seleccionado != null && seleccionado.getCodigo().equalsIgnoreCase(juego.getCodigo());
            card.setBorder(seleccionadoActual ? AppTheme.bordeSeleccionado(AppTheme.PRINCIPAL) : AppTheme.bordePanel());
            JLabel titulo = new JLabel("<html><b>" + juego.getNombre() + "</b><br><span style='color:#475569'>" + juego.getCodigo() + "</span></html>");
            titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            JTextArea resumen = new JTextArea(juego.getGenero() + " | " + juego.getPlataforma()
                    + "\nPrecio: " + FormatoUtil.moneda(juego.getPrecio())
                    + "\nStock: " + juego.getStock());
            resumen.setEditable(false);
            resumen.setOpaque(false);
            resumen.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
            resumen.setForeground(AppTheme.MUTED);
            card.add(titulo, BorderLayout.NORTH);
            card.add(resumen, BorderLayout.CENTER);
            JButton verMas = new JButton("Ver Detalles");
            AppTheme.estilizarBotonInfo(verMas);
            verMas.addActionListener(e -> seleccionarJuego(juego));
            card.add(verMas, BorderLayout.SOUTH);
            catalogoGrid.add(card);
            actual = actual.getSiguiente();
        }
        catalogoGrid.revalidate();
        catalogoGrid.repaint();
    }

    private void seleccionarJuego(Videojuego juego) {
        this.seleccionado = juego;
        detalleTitulo.setText(juego.getNombre() + " (" + juego.getCodigo() + ")");
        detalleArea.setText("Genero: " + juego.getGenero()
                + "\nPlataforma: " + juego.getPlataforma()
                + "\nPrecio: " + FormatoUtil.moneda(juego.getPrecio())
                + "\nStock actual: " + juego.getStock()
                + "\n\nDescripcion:\n" + juego.getDescripcion());
        agregarButton.setEnabled(true);
        renderizarCatalogo();
    }

    @Override
    public void refreshData() {
        renderizarCatalogo();
        cartTableModel.fireTableDataChanged();
        historyTableModel.fireTableDataChanged();
        totalCarritoLabel.setText("Total: " + FormatoUtil.moneda(service.getTotalCarrito()));
        if (seleccionado != null) {
            seleccionarJuego(seleccionado);
        }
    }

    private class CartTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return service.getCarrito().tamanio();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Codigo";
            }
            if (column == 1) {
                return "Juego";
            }
            if (column == 2) {
                return "Cantidad";
            }
            if (column == 3) {
                return "Precio";
            }
            return "Subtotal";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            CarritoItem item = service.getCarrito().obtener(rowIndex);
            if (columnIndex == 0) {
                return item.getVideojuego().getCodigo();
            }
            if (columnIndex == 1) {
                return item.getVideojuego().getNombre();
            }
            if (columnIndex == 2) {
                return item.getCantidad();
            }
            if (columnIndex == 3) {
                return FormatoUtil.moneda(item.getVideojuego().getPrecio());
            }
            return FormatoUtil.moneda(item.getSubtotal());
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 2;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                int cantidad = FormatoUtil.enteroSeguro(String.valueOf(aValue));
                service.actualizarCantidad(rowIndex, cantidad);
                mutationCallback.run();
                refreshData();
            }
        }
    }

    private class HistoryTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return service.getHistorial().tamanio();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Fecha";
            }
            if (column == 1) {
                return "Items";
            }
            if (column == 2) {
                return "Total";
            }
            return "Resumen";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Compra compra = service.getHistorial().obtener(rowIndex);
            if (columnIndex == 0) {
                return FormatoUtil.fechaHora(compra.getFechaHora());
            }
            if (columnIndex == 1) {
                return compra.getCantidadItems();
            }
            if (columnIndex == 2) {
                return FormatoUtil.moneda(compra.getTotal());
            }
            return construirResumen(compra);
        }

        private String construirResumen(Compra compra) {
            StringBuilder builder = new StringBuilder();
            NodoSimple<CompraDetalle> actual = compra.getDetalles().getCabeza();
            while (actual != null) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(actual.getDato().getNombreJuego()).append(" x").append(actual.getDato().getCantidad());
                actual = actual.getSiguiente();
            }
            return builder.toString();
        }
    }
}
