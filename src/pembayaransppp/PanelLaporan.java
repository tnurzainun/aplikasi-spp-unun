/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pembayaransppp;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
public class PanelLaporan extends JPanel {
    private JTable tableLaporan;
    private JScrollPane scrollPane;
    private JTextArea totalPendapatanArea;

    public PanelLaporan() {
        setLayout(new BorderLayout());
        // Label untuk Laporan
        JLabel lblLaporan = new JLabel("Laporan Transaksi", JLabel.CENTER);
        lblLaporan.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblLaporan, BorderLayout.NORTH);
        // Tabel untuk menampilkan laporan transaksi
        String[] columnNames = {"Nama Siswa", "Bulan", "Jumlah Bayar", "Status Lunas", "Tanggal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableLaporan = new JTable(model);
        scrollPane = new JScrollPane(tableLaporan);
        add(scrollPane, BorderLayout.CENTER);
        // Area untuk menampilkan Total Pendapatan di bawah tabel
        totalPendapatanArea = new JTextArea(5, 20);
        totalPendapatanArea.setEditable(false);
        totalPendapatanArea.setFont(new Font("Arial", Font.PLAIN, 14));
        totalPendapatanArea.setText("Total Pendapatan: Rp 0");
        add(totalPendapatanArea, BorderLayout.SOUTH);
        // Mengambil data transaksi dan menampilkannya di tabel
        loadLaporanTransaksi(model);
    }
    private void loadLaporanTransaksi(DefaultTableModel model) {
        double totalPendapatan = 0; // Variabel untuk menyimpan total pendapatan
        try (Connection conn = Koneksi.getConnection()) {
            // Query untuk mengambil transaksi dan menggabungkannya dengan tabel siswa
            String query = "SELECT siswa.nama, transaksi.bulan, transaksi.jumlah_bayar, transaksi.status_lunas, transaksi.tanggal " +
                    "FROM transaksi " +
                    "JOIN siswa ON transaksi.siswa_id = siswa.id " +
                    "ORDER BY transaksi.tanggal DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            // Menambahkan data ke tabel laporan
            while (rs.next()) {
                String namaSiswa = rs.getString("nama");
                String bulan = rs.getString("bulan");
                double jumlahBayar = rs.getDouble("jumlah_bayar");
                boolean statusLunas = rs.getBoolean("status_lunas");
                Date tanggal = rs.getDate("tanggal");
                // Format untuk status lunas (True menjadi "Lunas", False menjadi "Belum Lunas")
                String status = statusLunas ? "Lunas" : "Belum Lunas";
                // Menambah data ke model tabel
                model.addRow(new Object[]{namaSiswa, bulan, "Rp " + jumlahBayar, status, tanggal});
                // Menambahkan jumlah bayar ke total pendapatan
                totalPendapatan += jumlahBayar;
            }
            // Update total pendapatan pada JTextArea
            totalPendapatanArea.setText("Total Pendapatan: Rp " + totalPendapatan);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat laporan transaksi: " + e.getMessage());
        }
    }
}
