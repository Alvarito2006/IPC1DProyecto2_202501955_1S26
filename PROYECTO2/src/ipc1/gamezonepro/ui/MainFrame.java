package ipc1.gamezonepro.ui;

import ipc1.gamezonepro.datastructures.ListaEnlazadaSimple;
import ipc1.gamezonepro.datastructures.NodoSimple;
import ipc1.gamezonepro.service.AppContext;
import ipc1.gamezonepro.util.AppTheme;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {

    private final AppContext context;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final JLabel nivelLabel;
    private final JLabel xpLabel;

    private final TiendaPanel tiendaPanel;
    private final AlbumPanel albumPanel;
    private final TorneosPanel torneosPanel;
    private final RecompensasPanel recompensasPanel;
    private final ReportesPanel reportesPanel;
    private final EstudiantePanel estudiantePanel;

    public MainFrame(AppContext context) {
        this.context = context;
        setTitle("GameZone Pro");
        setSize(1440, 900);
        setMinimumSize(new Dimension(1200, 780));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(AppTheme.FONDO);
        setLayout(new BorderLayout());

        Runnable mutationCallback = new Runnable() {
            @Override
            public void run() {
                context.guardarTodo();
                refreshGlobalStatus();
                recompensasPanel.refreshData();
                mostrarNotificacionesPendientes();
            }
        };

        tiendaPanel = new TiendaPanel(context.getTiendaService(), mutationCallback);
        albumPanel = new AlbumPanel(context.getAlbumService(), mutationCallback);
        torneosPanel = new TorneosPanel(context.getTorneoService(), context.getEstudianteInfo().getNombreCompleto(), mutationCallback);
        recompensasPanel = new RecompensasPanel(context.getRecompensasService());
        reportesPanel = new ReportesPanel(context);
        estudiantePanel = new EstudiantePanel(context.getEstudianteInfo());

        JPanel sidebar = construirSidebar();
        add(sidebar, BorderLayout.WEST);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.PANEL);
        header.setBorder(AppTheme.bordePanel());
        JLabel titulo = new JLabel("GameZone Pro");
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        titulo.setForeground(AppTheme.TEXTO);
        JPanel estado = new JPanel(new BorderLayout());
        estado.setOpaque(false);
        nivelLabel = new JLabel();
        nivelLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        xpLabel = new JLabel();
        xpLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        estado.add(nivelLabel, BorderLayout.NORTH);
        estado.add(xpLabel, BorderLayout.SOUTH);
        header.add(titulo, BorderLayout.WEST);
        header.add(estado, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppTheme.FONDO);
        contentPanel.add(tiendaPanel, "TIENDA");
        contentPanel.add(albumPanel, "ALBUM");
        contentPanel.add(torneosPanel, "TORNEOS");
        contentPanel.add(recompensasPanel, "RECOMPENSAS");
        contentPanel.add(reportesPanel, "REPORTES");
        contentPanel.add(estudiantePanel, "ESTUDIANTE");
        add(contentPanel, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarAplicacion();
            }
        });

        refreshGlobalStatus();
        mostrarNotificacionesPendientes();
        mostrarPanel("TIENDA");
    }

    private JPanel construirSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new java.awt.Color(15, 23, 42));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 12, 18, 12));

        JLabel usuario = new JLabel("<html><span style='font-size:16px;color:white'><b>" + context.getEstudianteInfo().getNombreCompleto()
                + "</b></span><br><span style='color:#cbd5e1'>Carnet " + context.getEstudianteInfo().getCarnet() + "</span></html>");
        usuario.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(usuario);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(crearBotonMenu("Tienda de Videojuegos", "TIENDA"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("Album de Cartas", "ALBUM"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("Eventos Especiales", "TORNEOS"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("Recompensas y Lideres", "RECOMPENSAS"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("Reportes", "REPORTES"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(crearBotonMenu("Datos del Estudiante", "ESTUDIANTE"));
        sidebar.add(Box.createVerticalGlue());

        JButton salir = new JButton("Salir");
        AppTheme.estilizarBotonMenu(salir);
        salir.setBackground(new java.awt.Color(254, 226, 226));
        salir.addActionListener(e -> cerrarAplicacion());
        sidebar.add(salir);
        return sidebar;
    }

    private JButton crearBotonMenu(String texto, String tarjeta) {
        JButton boton = new JButton(texto);
        AppTheme.estilizarBotonMenu(boton);
        boton.addActionListener(e -> mostrarPanel(tarjeta));
        return boton;
    }

    private void mostrarPanel(String tarjeta) {
        cardLayout.show(contentPanel, tarjeta);
        tiendaPanel.refreshData();
        albumPanel.refreshData();
        torneosPanel.refreshData();
        recompensasPanel.refreshData();
        reportesPanel.refreshData();
        estudiantePanel.refreshData();
        refreshGlobalStatus();
    }

    private void refreshGlobalStatus() {
        nivelLabel.setText("Nivel " + context.getRecompensasService().getNivel() + " - " + context.getRecompensasService().getRango());
        xpLabel.setText("XP actual: " + context.getRecompensasService().getUsuario().getXp());
    }

    private void mostrarNotificacionesPendientes() {
        ListaEnlazadaSimple<String> notificaciones = context.getRecompensasService().consumirNotificaciones();
        NodoSimple<String> actual = notificaciones.getCabeza();
        while (actual != null) {
            JOptionPane.showMessageDialog(this, actual.getDato(), "Logro Desbloqueado", JOptionPane.INFORMATION_MESSAGE);
            actual = actual.getSiguiente();
        }
    }

    private void cerrarAplicacion() {
        context.guardarTodo();
        dispose();
        System.exit(0);
    }
}
