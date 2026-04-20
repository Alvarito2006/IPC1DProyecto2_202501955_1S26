package ipc1.gamezonepro;

import ipc1.gamezonepro.service.AppContext;
import ipc1.gamezonepro.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GameZoneProApp {

    public static void main(String[] args) {
        configurarLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppContext context = new AppContext();
                MainFrame frame = new MainFrame(context);
                frame.setVisible(true);
            }
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("Button.disabledText", java.awt.Color.BLACK);
    }
}
