package ipc1.gamezonepro.ui;

import ipc1.gamezonepro.model.EstudianteInfo;
import ipc1.gamezonepro.util.AppTheme;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class EstudiantePanel extends JPanel implements RefreshablePanel {

    private final EstudianteInfo estudianteInfo;

    public EstudiantePanel(EstudianteInfo estudianteInfo) {
        this.estudianteInfo = estudianteInfo;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.FONDO);

        JLabel titulo = AppTheme.tituloSeccion("Datos del Estudiante");
        add(titulo, BorderLayout.NORTH);

        JPanel cuerpo = AppTheme.crearTarjeta();
        cuerpo.setLayout(new BorderLayout(16, 16));

        JPanel datos = new JPanel(new GridLayout(0, 1, 8, 8));
        datos.setOpaque(false);
        datos.add(crearLinea("Nombre completo: " + estudianteInfo.getNombreCompleto()));
        datos.add(crearLinea("Carnet: " + estudianteInfo.getCarnet()));
        datos.add(crearLinea("CUI: " + estudianteInfo.getCui()));
        datos.add(crearLinea("Correo: " + estudianteInfo.getCorreo()));
        datos.add(crearLinea("Seccion: " + estudianteInfo.getSeccion()));
        datos.add(crearLinea("Semestre: " + estudianteInfo.getSemestre()));
        cuerpo.add(datos, BorderLayout.NORTH);

        JTextArea descripcion = new JTextArea(estudianteInfo.getDescripcionProyecto());
        descripcion.setWrapStyleWord(true);
        descripcion.setLineWrap(true);
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        descripcion.setForeground(AppTheme.TEXTO);
        cuerpo.add(descripcion, BorderLayout.CENTER);

        add(cuerpo, BorderLayout.CENTER);
    }

    private JLabel crearLinea(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        label.setForeground(AppTheme.TEXTO);
        return label;
    }

    @Override
    public void refreshData() {
    }
}
