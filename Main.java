package Finance;

import Finance.db.DBConnection;
import Finance.gui.MainFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[Main] Could not set system look-and-feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame mainFrame = new MainFrame();
                
         
                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.out.println("[Main] Closing application...");
                        DBConnection.closeConnection();
                        System.exit(0);
                    }
                });

                mainFrame.setVisible(true);
                System.out.println("[Main] Personal Finance Manager started successfully.");
                
            } catch (Exception e) {
                System.err.println("[Main] Failed to launch application: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to start the application:\n" + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DBConnection.closeConnection();
        }));
    }
}