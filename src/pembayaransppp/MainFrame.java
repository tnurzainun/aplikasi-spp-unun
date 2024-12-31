/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pembayaransppp;
import javax.swing.*;
import java.awt.*;
public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Aplikasi Pembayaran SPP");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        // Panel Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243)); // Biru lembut
        headerPanel.setPreferredSize(new Dimension(600, 50));     
        JLabel lblTitle = new JLabel(" Pembayaran SPP SD Tatiana", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        // TabbedPane untuk panel
        JTabbedPane tabbedPane = new JTabbedPane();
        PanelSiswa.ComboBoxUpdater comboBoxUpdater = () -> {
        };
        tabbedPane.addTab("🏫 Siswa", new PanelSiswa(comboBoxUpdater)); 
        tabbedPane.addTab("💰 Transaksi", new PanelTransaksi());
        tabbedPane.addTab("📊 Laporan", new PanelLaporan());

        // Menambahkan header dan tabbedPane ke frame
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
