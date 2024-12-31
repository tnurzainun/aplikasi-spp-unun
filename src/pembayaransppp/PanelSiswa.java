/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pembayaransppp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class PanelSiswa extends JPanel {
    private JTextField txtNama, txtKelas;
    private ComboBoxUpdater comboBoxUpdater;
    // Constructor menerima ComboBoxUpdater untuk mengupdate ComboBox di PanelTransaksi
    public PanelSiswa(ComboBoxUpdater comboBoxUpdater) {
        this.comboBoxUpdater = comboBoxUpdater;  
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Label dan input untuk nama siswa
        JLabel lblNama = new JLabel("Nama Siswa:");
        lblNama.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblNama, gbc);
        txtNama = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(txtNama, gbc);
        JLabel lblKelas = new JLabel("Kelas:");
        lblKelas.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblKelas, gbc);
        txtKelas = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtKelas, gbc);
        // Tombol untuk simpan data siswa
        JButton btnSubmit = new JButton("Simpan");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.setBackground(new Color(33, 150, 243));
        btnSubmit.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(btnSubmit, gbc);
        // Action untuk tombol simpan
        btnSubmit.addActionListener((ActionEvent e) -> simpanDataSiswa());
    }
    private void simpanDataSiswa() {
        try (Connection conn = Koneksi.getConnection()) {
            String nama = txtNama.getText();
            String kelas = txtKelas.getText();
            if (!nama.isEmpty() && !kelas.isEmpty()) {
                String query = "INSERT INTO siswa (nama, kelas) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nama);
                stmt.setString(2, kelas);
                stmt.executeUpdate();
               JOptionPane.showMessageDialog(this, "Data Siswa Berhasil Ditambahkan!");
                // Memanggil method updateComboBox di PanelTransaksi agar ComboBox diupdate
                if (comboBoxUpdater != null) {
                    comboBoxUpdater.updateComboBox();
                }
                txtNama.setText("");
                txtKelas.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Menyimpan Data: " + e.getMessage());
        }
    }
    // Interface untuk callback agar bisa mengupdate ComboBox
    public interface ComboBoxUpdater {
        void updateComboBox();
    }
}
