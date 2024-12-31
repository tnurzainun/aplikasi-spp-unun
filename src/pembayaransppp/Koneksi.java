/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pembayaransppp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/sekolah_spp"; 
        String username = "root"; 
        String password = ""; 
        return DriverManager.getConnection(url, username, password);
    }
}

