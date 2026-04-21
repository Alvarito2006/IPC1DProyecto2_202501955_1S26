package ipc1.gamezonepro.ui;

import ipc1.gamezonepro.datastructures.MatrizOrtogonal;
import ipc1.gamezonepro.datastructures.NodoMatriz;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.model.Carta;
import ipc1.gamezonepro.service.AlbumService;
import ipc1.gamezonepro.service.AlbumService.ResultadoAlbum;
import ipc1.gamezonepro.util.AppTheme;
import ipc1.gamezonepro.util.FormatoUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AlbumPanel extends JPanel implements RefreshablePanel {

    private final AlbumService service;
    private final Runnable mutationCallback;
    private final JPanel gridPanel;
    private final JTextField buscarField;
    private final JComboBox<String> catalogoCombo;
    private final JTextArea detalleArea;
    private final JLabel seleccionLabel;

    private int firstRow = -1;
    private int firstCol = -1;
    private int secondRow = -1;
    private int secondCol = -1;
    private NodoMatriz nodoDetalle;

    public AlbumPanel(AlbumService service, Runnable mutationCallback) {
        this.service = service;
        this.mutationCallback = mutationCallback;
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.FONDO);

        JLabel titulo = AppTheme.tituloSeccion("Album de Cartas Coleccionables");
        add(titulo, BorderLayout.NORTH);

        JPanel controles = AppTheme.crearTarjeta();
        controles.setLayout(new GridLayout(2, 3, 10, 10));
        controles.add(AppTheme.subtitulo("Buscar por nombre, tipo o rareza"));
        controles.add(AppTheme.subtitulo("Agregar desde catalogo"));
        controles.add(new JLabel(""));
        buscarField = new JTextField();
        catalogoCombo = new JComboBox<String>();
        cargarOpcionesCatalogo();
        JButton agregarCatalogoButton = new JButton("Agregar Carta");
        AppTheme.estilizarBotonExito(agregarCatalogoButton);
        controles.add(buscarField);
        controles.add(catalogoCombo);
        controles.add(agregarCatalogoButton);
        add(controles, BorderLayout.SOUTH);

        gridPanel = new JPanel();
        gridPanel.setOpaque(false);
        JScrollPane albumScroll = new JScrollPane(gridPanel);
        albumScroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel lateral = AppTheme.crearTarjeta();
        lateral.setLayout(new BorderLayout(8, 8));
        JLabel lateralTitulo = new JLabel("Detalles y gestion");
        lateralTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lateralTitulo.setForeground(AppTheme.TEXTO);
        detalleArea = new JTextArea("Selecciona una celda para ver sus detalles.");
        detalleArea.setLineWrap(true);
        detalleArea.setWrapStyleWord(true);
        detalleArea.setEditable(false);
        detalleArea.setOpaque(false);
        detalleArea.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        JPanel acciones = new JPanel();
        acciones.setOpaque(false);
        acciones.setLayout(new BoxLayout(acciones, BoxLayout.Y_AXIS));
        seleccionLabel = new JLabel("Seleccionadas: ninguna");
        seleccionLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        JButton crearManualButton = new JButton("Crear Carta Manual");
        JButton intercambiarButton = new JButton("Intercambiar Seleccionadas");
        AppTheme.estilizarBotonInfo(crearManualButton);
        AppTheme.estilizarBotonAdvertencia(intercambiarButton);
        acciones.add(seleccionLabel);
        acciones.add(crearManualButton);
        acciones.add(intercambiarButton);

        lateral.add(lateralTitulo, BorderLayout.NORTH);
        lateral.add(new JScrollPane(detalleArea), BorderLayout.CENTER);
        lateral.add(acciones, BorderLayout.SOUTH);

        add(new JScrollPane(albumScroll), BorderLayout.CENTER);
        add(lateral, BorderLayout.EAST);

        agregarCatalogoButton.addActionListener(e -> agregarDesdeCatalogo());
        crearManualButton.addActionListener(e -> crearManual());
        intercambiarButton.addActionListener(e -> intercambiar());
        buscarField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                renderAlbum();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                renderAlbum();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                renderAlbum();
            }
        });

        refreshData();
    }

    private void cargarOpcionesCatalogo() {
        NodoSimple<Carta> actual = service.getCatalogoCartas().getCabeza();
        while (actual != null) {
            catalogoCombo.addItem(actual.getDato().getCodigo());
            actual = actual.getSiguiente();
        }
    }

    private void agregarDesdeCatalogo() {
        String codigo = (String) catalogoCombo.getSelectedItem();
        ResultadoAlbum resultado = service.agregarCartaDesdeCatalogo(codigo);
        mutationCallback.run();
        refreshData();
        JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Album", resultado.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private void crearManual() {
        JTextField codigoField = new JTextField();
        JTextField nombreField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<String>();
        JComboBox<String> rarezaCombo = new JComboBox<String>();
        cargarTiposCarta(tipoCombo);
        cargarRarezasCarta(rarezaCombo);
        JTextField ataqueField = new JTextField();
        JTextField defensaField = new JTextField();
        JTextField saludField = new JTextField();
        JTextField imagenField = new JTextField("sin_imagen");

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Codigo"));
        formulario.add(codigoField);
        formulario.add(new JLabel("Nombre"));
        formulario.add(nombreField);
        formulario.add(new JLabel("Tipo"));
        formulario.add(tipoCombo);
        formulario.add(new JLabel("Rareza"));
        formulario.add(rarezaCombo);
        formulario.add(new JLabel("Ataque"));
        formulario.add(ataqueField);
        formulario.add(new JLabel("Defensa"));
        formulario.add(defensaField);
        formulario.add(new JLabel("Salud"));
        formulario.add(saludField);
        formulario.add(new JLabel("Ruta de imagen"));
        formulario.add(imagenField);

        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Crear Carta Manual", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion == JOptionPane.OK_OPTION) {
            Carta carta = new Carta(
                    codigoField.getText().trim(),
                    nombreField.getText().trim(),
                    String.valueOf(tipoCombo.getSelectedItem()),
                    String.valueOf(rarezaCombo.getSelectedItem()),
                    FormatoUtil.enteroSeguro(ataqueField.getText()),
                    FormatoUtil.enteroSeguro(defensaField.getText()),
                    FormatoUtil.enteroSeguro(saludField.getText()),
                    imagenField.getText().trim());
            ResultadoAlbum resultado = service.agregarCartaManual(carta);
            mutationCallback.run();
            refreshData();
            JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Album", resultado.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarTiposCarta(JComboBox<String> tipoCombo) {
        tipoCombo.addItem("Fuego");
        tipoCombo.addItem("Agua");
        tipoCombo.addItem("Planta");
        tipoCombo.addItem("Electrico");
        tipoCombo.addItem("Psiquico");
        tipoCombo.addItem("Normal");
        tipoCombo.addItem("Oscuro");
        tipoCombo.addItem("Acero");
    }

    private void cargarRarezasCarta(JComboBox<String> rarezaCombo) {
        rarezaCombo.addItem("Comun");
        rarezaCombo.addItem("Poco Comun");
        rarezaCombo.addItem("Rara");
        rarezaCombo.addItem("Ultra Rara");
        rarezaCombo.addItem("Legendaria");
    }

    private void intercambiar() {
        if (firstRow < 0 || secondRow < 0) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar dos celdas para intercambiar.", "Album", JOptionPane.WARNING_MESSAGE);
            return;
        }
        service.intercambiar(firstRow, firstCol, secondRow, secondCol);
        mutationCallback.run();
        firstRow = -1;
        firstCol = -1;
        secondRow = -1;
        secondCol = -1;
        refreshData();
    }

    private void renderAlbum() {
        gridPanel.removeAll();
        MatrizOrtogonal album = service.getAlbum();
        gridPanel.setLayout(new GridLayout(album.getFilas(), album.getColumnas(), 8, 8));
        NodoMatriz filaActual = album.getInicio();
        while (filaActual != null) {
            NodoMatriz columnaActual = filaActual;
            while (columnaActual != null) {
                final NodoMatriz nodo = columnaActual;
                JPanel celda = new JPanel(new BorderLayout());
                celda.setOpaque(true);
                celda.setBorder(BorderFactory.createLineBorder(colorBorde(nodo)));
                Carta carta = nodo.getCarta();
                if (carta == null) {
                    celda.setBackground(new Color(226, 232, 240));
                    JLabel vacia = new JLabel("Vacia", JLabel.CENTER);
                    vacia.setForeground(AppTheme.MUTED);
                    celda.add(vacia, BorderLayout.CENTER);
                } else {
                    celda.setBackground(colorCarta(carta));
                    JLabel nombre = new JLabel("<html><center><b>" + carta.getNombre() + "</b><br>" + carta.getTipo()
                            + "<br>" + carta.getRareza() + "</center></html>", JLabel.CENTER);
                    nombre.setForeground(Color.WHITE);
                    nombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
                    celda.add(nombre, BorderLayout.CENTER);
                }
                celda.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        seleccionarNodo(nodo);
                    }
                });
                gridPanel.add(celda);
                columnaActual = columnaActual.getDerecha();
            }
            filaActual = filaActual.getAbajo();
        }
        gridPanel.revalidate();
        gridPanel.repaint();
        actualizarSeleccionLabel();
    }

    private void seleccionarNodo(NodoMatriz nodo) {
        nodoDetalle = nodo;
        if (firstRow == -1) {
            firstRow = nodo.getFila();
            firstCol = nodo.getColumna();
        } else if (firstRow == nodo.getFila() && firstCol == nodo.getColumna()) {
            firstRow = -1;
            firstCol = -1;
            secondRow = -1;
            secondCol = -1;
        } else if (secondRow == -1) {
            secondRow = nodo.getFila();
            secondCol = nodo.getColumna();
        } else {
            firstRow = nodo.getFila();
            firstCol = nodo.getColumna();
            secondRow = -1;
            secondCol = -1;
        }
        mostrarDetalles(nodo);
        renderAlbum();
    }

    private void mostrarDetalles(NodoMatriz nodo) {
        if (nodo == null || nodo.getCarta() == null) {
            detalleArea.setText("Celda (" + (nodo != null ? nodo.getFila() : "-") + ", " + (nodo != null ? nodo.getColumna() : "-")
                    + ")\nActualmente vacia.");
            return;
        }
        Carta carta = nodo.getCarta();
        detalleArea.setText("Posicion: (" + nodo.getFila() + ", " + nodo.getColumna() + ")"
                + "\nCodigo: " + carta.getCodigo()
                + "\nNombre: " + carta.getNombre()
                + "\nTipo: " + carta.getTipo()
                + "\nRareza: " + carta.getRareza()
                + "\nAtaque: " + carta.getAtaque()
                + "\nDefensa: " + carta.getDefensa()
                + "\nSalud: " + carta.getSalud()
                + "\nImagen: " + carta.getRutaImagen());
    }

    private void actualizarSeleccionLabel() {
        String primero = firstRow >= 0 ? "(" + firstRow + "," + firstCol + ")" : "-";
        String segundo = secondRow >= 0 ? "(" + secondRow + "," + secondCol + ")" : "-";
        seleccionLabel.setText("Seleccionadas: " + primero + " y " + segundo);
    }

    private Color colorCarta(Carta carta) {
        if ("Legendaria".equalsIgnoreCase(carta.getRareza())) {
            return new Color(217, 119, 6);
        }
        if ("Ultra Rara".equalsIgnoreCase(carta.getRareza())) {
            return new Color(109, 40, 217);
        }
        if ("Rara".equalsIgnoreCase(carta.getRareza())) {
            return new Color(37, 99, 235);
        }
        return new Color(15, 118, 110);
    }

    private Color colorBorde(NodoMatriz nodo) {
        if (nodoDetalle != null && nodoDetalle.getFila() == nodo.getFila() && nodoDetalle.getColumna() == nodo.getColumna()) {
            return AppTheme.SECUNDARIO;
        }
        if ((firstRow == nodo.getFila() && firstCol == nodo.getColumna())
                || (secondRow == nodo.getFila() && secondCol == nodo.getColumna())) {
            return AppTheme.PRINCIPAL;
        }
        Carta carta = nodo.getCarta();
        if (service.coincideBusqueda(carta, buscarField.getText())) {
            return AppTheme.DORADO;
        }
        return AppTheme.BORDE;
    }

    @Override
    public void refreshData() {
        renderAlbum();
        if (nodoDetalle != null) {
            mostrarDetalles(service.getAlbum().obtenerNodo(nodoDetalle.getFila(), nodoDetalle.getColumna()));
        }
    }
}
