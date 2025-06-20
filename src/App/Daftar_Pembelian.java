package App;

//import App.Config;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
//import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
//import javax.swing.table.TableColumnModel;

public class Daftar_Pembelian extends javax.swing.JFrame {

    public Daftar_Pembelian() {
        initComponents();
        setLocationRelativeTo(null);
        Tampilkan_Data();
//        configureTableResizable();
    }

    private void Tampilkan_Data() {
        DefaultTableModel model = new DefaultTableModel();
        // Set column headers for purchase data
        model.setColumnIdentifiers(new Object[]{"ID Pembelian", "Tanggal", "Total", "Supplier"});
        tbl_penjualan.setModel(model);  // Note: Consider renaming to tbl_pembelian if this is for purchases

        // Setup Indonesian Rupiah currency formatting
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            java.sql.Connection conn = Config.configDB();
            // Query to get purchase data including supplier
            String sql = "SELECT id, tanggal, total, supplier FROM pembelian ORDER BY tanggal DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Format currency values
                String totalFormatted = kursIndonesia.format(rs.getDouble("total"));

                // Get supplier value
                String supplier = rs.getString("supplier");
                // Handle possible null values
                if (supplier == null) {
                    supplier = "-";
                }

                // Add row with all values including supplier
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("tanggal"),
                    totalFormatted,
                    supplier // Make sure this is included
                });
            }

            // Auto-resize columns to fit content
            for (int i = 0; i < tbl_penjualan.getColumnCount(); i++) {
                tbl_penjualan.getColumnModel().getColumn(i).setPreferredWidth(150);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
//    private void configureTableResizable() {
//        // Nonaktifkan resize otomatis
//        tbl_penjualan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//
//        // Aktifkan resize manual kolom
//        tbl_penjualan.getTableHeader().setResizingAllowed(true);
//
//        // Atur lebar awal kolom (sesuaikan angka sesuai kebutuhan)
//        TableColumnModel columnModel = tbl_penjualan.getColumnModel();
//        columnModel.getColumn(0).setPreferredWidth(50);   // ID
//        columnModel.getColumn(1).setPreferredWidth(120);  // nama
//        columnModel.getColumn(2).setPreferredWidth(114);  // jenis
//        columnModel.getColumn(3).setPreferredWidth(200);   // jumlah
//        columnModel.getColumn(4).setPreferredWidth(120);  // no_telp
//        columnModel.getColumn(5).setPreferredWidth(120);  // no_telp
//
//        // Pastikan JScrollPane tidak menimpa pengaturan tabel
//        JScrollPane scrollPane = (JScrollPane) tbl_penjualan.getParent().getParent();
//        scrollPane.setViewportView(tbl_penjualan);
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_penjualan = new javax.swing.JTable();
        tbl_penjualan.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
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
        btn_Detail = new javax.swing.JButton();
        btn_kembali = new javax.swing.JButton();
        background = new javax.swing.JLabel();
        background1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_penjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "JENIS", "TOTAL", "TANGGAL", "CASH", "KEMBALI"
            }
        ));
        tbl_penjualan.setSelectionBackground(new java.awt.Color(0, 204, 255));
        jScrollPane1.setViewportView(tbl_penjualan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 730, 310));

        btn_Detail.setContentAreaFilled(false);
        btn_Detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DetailActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Detail, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 390, 210, 60));

        btn_kembali.setContentAreaFilled(false);
        btn_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btn_kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 390, 180, 60));

        background.setIcon(new FlatSVGIcon("UI_Svg/daftarPembelian.svg"));
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 30, 800, 410));

        background1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/login.gif"))); // NOI18N
        getContentPane().add(background1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kembaliActionPerformed
        // TODO add your handling code here:
        Daftar halamandaftar = new Daftar();
        halamandaftar.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_kembaliActionPerformed

    private void btn_DetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DetailActionPerformed
        // TODO add your handling code here:                                          
        int selectedRow = tbl_penjualan.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

// Ambil ID pembelian dari kolom pertama tabel
        int idPembelian = (int) tbl_penjualan.getValueAt(selectedRow, 0);

// Debug: Tampilkan ID yang dipilih
        System.out.println("[DEBUG] ID Pembelian yang dipilih: " + idPembelian);

        try {
            java.sql.Connection con = Config.configDB();

            // 1. Verifikasi data utama pembelian
            String verifySql = "SELECT id, tanggal, supplier, total FROM pembelian WHERE id = ?";
            PreparedStatement verifyStmt = con.prepareStatement(verifySql);
            verifyStmt.setInt(1, idPembelian);
            ResultSet rs = verifyStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Data pembelian tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Debug: Tampilkan data utama
            System.out.println("[DEBUG] Data pembelian dari database:");
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Tanggal: " + rs.getTimestamp("tanggal"));
            System.out.println("Supplier: " + rs.getString("supplier"));
            System.out.println("Total: " + rs.getBigDecimal("total"));

            // 2. Verifikasi detail pembelian
            String detailSql = "SELECT COUNT(*) as jumlah FROM detail_pembelian WHERE id_pembelian = ?";
            PreparedStatement detailStmt = con.prepareStatement(detailSql);
            detailStmt.setInt(1, idPembelian);
            ResultSet rsDetail = detailStmt.executeQuery();
            rsDetail.next();
            System.out.println("[DEBUG] Jumlah item detail: " + rsDetail.getInt("jumlah"));

            // 3. Load dan generate report
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("src/Nota/Detail_pembelian.jasper");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("idpembelian", idPembelian);

            // Debug: Tampilkan parameter yang dikirim
            System.out.println("[DEBUG] Parameter report: " + parameters);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

            // 4. Tampilkan report
            JasperViewer.viewReport(jasperPrint, false);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (JRException e) {
            JOptionPane.showMessageDialog(this, "Error report: " + e.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);

            // Debug khusus error Jasper
            System.err.println("JasperReport error details:");
            e.printStackTrace();

            // Cek file report
            File reportFile = new File("src/Nota/Detail_pembelian.jasper");
            System.out.println("Report file exists: " + reportFile.exists());
            System.out.println("Report file path: " + reportFile.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


    }//GEN-LAST:event_btn_DetailActionPerformed

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
            java.util.logging.Logger.getLogger(Daftar_Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Daftar_Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Daftar_Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Daftar_Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Daftar_Pembelian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel background1;
    private javax.swing.JButton btn_Detail;
    private javax.swing.JButton btn_kembali;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_penjualan;
    // End of variables declaration//GEN-END:variables
}
