/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pembayaransppp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
public class PanelTransaksi extends JPanel {
    private JComboBox<String> comboBoxSiswa;
    private JCheckBox[] bulanCheckBoxes;
    private JLabel lblSPP, lblTotalBayar;
    private JButton btnSubmit;
    public PanelTransaksi() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;       
        // Memberikan ketahanan agar komponen bisa mengisi ruang lebih baik
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;        
        // Label dan input untuk nama siswa
        JLabel lblSiswa = new JLabel("Pilih Siswa:");
        lblSiswa.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblSiswa, gbc);
        comboBoxSiswa = new JComboBox<>();
        loadSiswaToComboBox(); // Memuat siswa ke ComboBox
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  // Memastikan ComboBox lebih lebar
        add(comboBoxSiswa, gbc);
        // Menampilkan SPP yang tetap
        lblSPP = new JLabel("SPP Per Bulan: Rp 360.000");
        lblSPP.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(lblSPP, gbc);
        // Checkbox untuk memilih bulan
        JLabel lblBulan = new JLabel("Pilih Bulan Pembayaran:");
        lblBulan.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        add(lblBulan, gbc);
        bulanCheckBoxes = new JCheckBox[12];
        String[] bulanList = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        for (int i = 0; i < 12; i++) {
            bulanCheckBoxes[i] = new JCheckBox(bulanList[i]);
            gbc.gridx = i % 3;
            gbc.gridy = 3 + (i / 3);
            gbc.gridwidth = 1;  // Membatasi lebar checkbox
            add(bulanCheckBoxes[i], gbc);
        }
        // Label untuk total bayar
        lblTotalBayar = new JLabel("Total Pembayaran: Rp 0");
        lblTotalBayar.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;  // Menyebar di dua kolom
        add(lblTotalBayar, gbc);
// Tombol untuk simpan transaksi
btnSubmit = new JButton("Simpan Transaksi");
btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
btnSubmit.setBackground(new Color(33, 150, 243));
btnSubmit.setForeground(Color.WHITE);
// Mengatur GridBagConstraints untuk menempatkan tombol di tengah
gbc.gridx = 0;  // Posisi horizontal di kolom pertama
gbc.gridy = 7;  // Posisi vertikal setelah checkbox bulan
gbc.gridwidth = 3;  // Tombol meluas ke 3 kolom, memanfaatkan seluruh ruang yang ada
gbc.weightx = 1.0;  // Memberikan bobot horizontal agar tombol bisa berada di tengah
add(btnSubmit, gbc);
        btnSubmit.addActionListener((ActionEvent e) -> simpanTransaksi());
        // Membungkus panel ini dalam JScrollPane agar bisa digulir
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setPreferredSize(new Dimension(600, 400)); // Sesuaikan ukuran scroll pane dengan aplikasi
    }
    private void loadSiswaToComboBox() {
        try (Connection conn = Koneksi.getConnection()) {  // Pastikan kelas Koneksi sudah ada
            String query = "SELECT * FROM siswa"; // Query untuk mengambil data siswa
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            // Menambahkan siswa ke ComboBox
            while (rs.next()) {
                comboBoxSiswa.addItem(rs.getString("nama"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data siswa: " + e.getMessage());
        }
    }
    private void simpanTransaksi() {
        String namaSiswa = (String) comboBoxSiswa.getSelectedItem();
        double totalBayar = 0;
        // Menambahkan pembayaran untuk bulan yang dipilih
        for (JCheckBox checkBox : bulanCheckBoxes) {
            if (checkBox.isSelected()) {
                totalBayar += 360000; // Menambahkan SPP per bulan
            }
        }
        if (totalBayar > 0) {
            try (Connection conn = Koneksi.getConnection()) {
                // Menyimpan transaksi ke database
                String querySiswa = "SELECT id FROM siswa WHERE nama = ?";
                PreparedStatement stmtSiswa = conn.prepareStatement(querySiswa);
                stmtSiswa.setString(1, namaSiswa);
                ResultSet rsSiswa = stmtSiswa.executeQuery();
                int siswaId = 0;
                if (rsSiswa.next()) {
                    siswaId = rsSiswa.getInt("id");
                }
                // Menyimpan transaksi
                String queryTransaksi = "INSERT INTO transaksi (siswa_id, bulan, jumlah_bayar, status_lunas, tanggal) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmtTransaksi = conn.prepareStatement(queryTransaksi);
                for (JCheckBox checkBox : bulanCheckBoxes) {
                    if (checkBox.isSelected()) {
                        stmtTransaksi.setInt(1, siswaId);
                        stmtTransaksi.setString(2, checkBox.getText());
                        stmtTransaksi.setDouble(3, 360000);
                        stmtTransaksi.setBoolean(4, true); // Anggap lunas
                        stmtTransaksi.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                        stmtTransaksi.addBatch();  // Menambahkan batch query
                    }
                }
                stmtTransaksi.executeBatch(); // Eksekusi batch query
                JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
                lblTotalBayar.setText("Total Pembayaran: Rp " + totalBayar);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih bulan yang akan dibayar!");
        }
    }
}
