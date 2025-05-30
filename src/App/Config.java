package App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Config {
    private static Connection mysqlconfig;

    public static Connection configDB() throws SQLException {
        if (mysqlconfig == null) { // Cek apakah koneksi sudah ada
            try {
                String url = "jdbc:mysql://localhost:3306/didiexs"; // Pastikan database benar
                String user = "root"; // Sesuaikan dengan user MySQL
                String pass = ""; // Sesuaikan dengan password MySQL

                // Memuat driver JDBC MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Membuka koneksi ke database
                mysqlconfig = DriverManager.getConnection(url, user, pass);
                System.out.println("Koneksi ke database berhasil!");

            } catch (ClassNotFoundException e) {
                System.err.println("Driver tidak ditemukan: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Koneksi gagal: " + e.getMessage());
            }
        }
        return mysqlconfig;
    }
}
