package App;

import Class.Session;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PreOrder extends javax.swing.JFrame {

    public PreOrder() {
        initComponents();
        setLocationRelativeTo(null);
        Tampilkan_Products();
    }

    private void Tampilkan_Products() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Nama", "Stok", "Harga"});
        tbl_product.setModel(model);

        // Format untuk mata uang Rupiah
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        try {
            java.sql.Connection conn = Config.configDB();
            String sql = "SELECT * FROM products";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("jenis");
                int stok = rs.getInt("stok");
                double harga = rs.getDouble("harga");

                // Konversi harga ke format Rupiah
                String hargaRupiah = rupiahFormat.format(harga);

                model.addRow(new Object[]{id, nama, stok, hargaRupiah});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_product = new javax.swing.JTable();
        tbl_product.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

                label.setBackground(new Color(0x35AEFF));
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
                return label;
            }
        });
        txt_jumlah = new javax.swing.JTextField();
        txt_keterangan = new javax.swing.JTextField();
        txt_jenis = new javax.swing.JTextField();
        txt_nomer = new javax.swing.JTextField();
        txt_nama = new javax.swing.JTextField();
        btn_tambah = new javax.swing.JButton();
        btn_penjualan = new javax.swing.JButton();
        btn_product = new javax.swing.JButton();
        btn_pembelian = new javax.swing.JButton();
        btn_preorder = new javax.swing.JButton();
        btn_daftar = new javax.swing.JButton();
        btn_catatan = new javax.swing.JButton();
        btn_keluar = new javax.swing.JButton();
        bar_bawah1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        bar_bawah = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_product.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_product.setSelectionBackground(new java.awt.Color(0, 204, 255));
        tbl_product.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_productMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_product);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 480, 320));

        txt_jumlah.setBorder(null);
        getContentPane().add(txt_jumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 210, 20));

        txt_keterangan.setBorder(null);
        getContentPane().add(txt_keterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, 210, 20));

        txt_jenis.setBorder(null);
        getContentPane().add(txt_jenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 142, 210, 20));

        txt_nomer.setBorder(null);
        getContentPane().add(txt_nomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 259, 210, 20));

        txt_nama.setBorder(null);
        getContentPane().add(txt_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 86, 210, 20));

        btn_tambah.setContentAreaFilled(false);
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });
        getContentPane().add(btn_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 130, 30));

        btn_penjualan.setContentAreaFilled(false);
        btn_penjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_penjualanActionPerformed(evt);
            }
        });
        getContentPane().add(btn_penjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 460, 90, 60));

        btn_product.setContentAreaFilled(false);
        btn_product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_productActionPerformed(evt);
            }
        });
        getContentPane().add(btn_product, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, 90, 60));

        btn_pembelian.setContentAreaFilled(false);
        btn_pembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pembelianActionPerformed(evt);
            }
        });
        getContentPane().add(btn_pembelian, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 460, 90, 60));

        btn_preorder.setContentAreaFilled(false);
        btn_preorder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_preorderActionPerformed(evt);
            }
        });
        getContentPane().add(btn_preorder, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 460, 90, 60));

        btn_daftar.setContentAreaFilled(false);
        btn_daftar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_daftarActionPerformed(evt);
            }
        });
        getContentPane().add(btn_daftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 460, 90, 60));

        btn_catatan.setContentAreaFilled(false);
        btn_catatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_catatanActionPerformed(evt);
            }
        });
        getContentPane().add(btn_catatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 460, 90, 60));

        btn_keluar.setContentAreaFilled(false);
        btn_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_keluarActionPerformed(evt);
            }
        });
        getContentPane().add(btn_keluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 460, 90, 60));

        bar_bawah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/bawah.png"))); // NOI18N
        getContentPane().add(bar_bawah1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 465, 1000, 90));

        jLabel1.setIcon(new FlatSVGIcon("UI_Svg/preorder.svg"));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 35, 880, 413));

        bar_bawah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/dashboard.png"))); // NOI18N
        getContentPane().add(bar_bawah, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 465, 1000, 90));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/login.gif"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 555));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_productMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_productMouseClicked
        // TODO add your handling code here:

        int selectedRow = tbl_product.getSelectedRow();

        // Pastikan ada baris yang dipilih
        if (selectedRow != -1) {
            // Ambil data dari kolom "Nama" (kolom ke-1, indeks dimulai dari 0)
            String jenisIkan = tbl_product.getValueAt(selectedRow, 1).toString();

            // Set teks ke dalam txt_jenis_ikan
            txt_jenis.setText(jenisIkan);
        }

    }//GEN-LAST:event_tbl_productMouseClicked

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        // TODO add your handling code here:

        String nama = txt_nama.getText();
        String jenis = txt_jenis.getText();
        String jumlah = txt_jumlah.getText();
        String nomer = txt_nomer.getText();
        String keterangan = txt_keterangan.getText();

        try {
            int jumlahInt = Integer.parseInt(jumlah);
            int idAkun = Session.getIdAkun(); // Ambil ID akun dari sesi login

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/didiexs", "root", "");
            String sql = "INSERT INTO preorder (id_akun, atas_nama, jenis_ikan, jumlah, no_telp, keterangan) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, idAkun);
            stmt.setString(2, nama);
            stmt.setString(3, jenis);
            stmt.setInt(4, jumlahInt);
            stmt.setString(5, nomer); // tetap String, bukan Int
            stmt.setString(6, keterangan);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data Berhasil Di Tambahkan!");

                // Kosongkan field input
                txt_nama.setText("");
                txt_jenis.setText("");
                txt_jumlah.setText("");
                txt_nomer.setText("");
                txt_keterangan.setText("");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_penjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_penjualanActionPerformed
        // TODO add your handling code here:
        Penjualan halamanpenjualan = new Penjualan();
        halamanpenjualan.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_penjualanActionPerformed

    private void btn_productActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_productActionPerformed
        // TODO add your handling code here:
        product halamanproduct = new product();
        halamanproduct.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_productActionPerformed

    private void btn_pembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pembelianActionPerformed
        // TODO add your handling code here:
        Timer timer = new Timer(5, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    timer.stop();
                    Pembelian halamanpembelian = new Pembelian();
                    halamanpembelian.setVisible(true);
                    dispose(); // Tutup login window setelah fade-out
                } else {
                    setOpacity(opacity); // Kurangi opacity
                }
            }
        });
        timer.start();
    }//GEN-LAST:event_btn_pembelianActionPerformed

    private void btn_preorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_preorderActionPerformed
        // TODO add your handling code here:
        Timer timer = new Timer(5, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    timer.stop();
                    PreOrder halamanpo = new PreOrder();
                    halamanpo.setVisible(true);
                    dispose(); // Tutup login window setelah fade-out
                } else {
                    setOpacity(opacity); // Kurangi opacity
                }
            }
        });
        timer.start();
    }//GEN-LAST:event_btn_preorderActionPerformed

    private void btn_daftarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_daftarActionPerformed
        // TODO add your handling code here:
        Timer timer = new Timer(20, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    timer.stop();
                    Daftar halamandaftar = new Daftar();
                    halamandaftar.setVisible(true);
                    dispose(); // Tutup login window setelah fade-out
                } else {
                    setOpacity(opacity); // Kurangi opacity
                }
            }
        });
        timer.start();
    }//GEN-LAST:event_btn_daftarActionPerformed

    private void btn_catatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_catatanActionPerformed
        // TODO add your handling code here:
        Timer timer = new Timer(20, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    timer.stop();
                    Catatan halamancatatan = new Catatan();
                    halamancatatan.setVisible(true);
                    dispose(); // Tutup login window setelah fade-out
                } else {
                    setOpacity(opacity); // Kurangi opacity
                }
            }
        });
        timer.start();
    }//GEN-LAST:event_btn_catatanActionPerformed

    private void btn_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_keluarActionPerformed
        // TODO add your handling code here:
        int dialogBtn = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "apakah anda yakin ingin keluar?", "PERINGATAN", dialogBtn);

        if (dialogResult == 0) {
            Login n = new Login();
            n.setVisible(true);
            this.dispose();
        } else {

        }
    }//GEN-LAST:event_btn_keluarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PreOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel bar_bawah;
    private javax.swing.JLabel bar_bawah1;
    private javax.swing.JButton btn_catatan;
    private javax.swing.JButton btn_daftar;
    private javax.swing.JButton btn_keluar;
    private javax.swing.JButton btn_pembelian;
    private javax.swing.JButton btn_penjualan;
    private javax.swing.JButton btn_preorder;
    private javax.swing.JButton btn_product;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_product;
    private javax.swing.JTextField txt_jenis;
    private javax.swing.JTextField txt_jumlah;
    private javax.swing.JTextField txt_keterangan;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JTextField txt_nomer;
    // End of variables declaration//GEN-END:variables
}
