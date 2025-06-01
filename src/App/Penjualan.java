package App;

import Class.Session;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Penjualan extends javax.swing.JFrame {

    public Penjualan() {
        initComponents();
        setLocationRelativeTo(null);
        Tampilkan_Products();
        initBarcodeComponents();
        configureTableResizable();
//        configtabel();

        txt_cash.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                formatCashInput();  // Perbaiki format input tanpa berulang
                hitungKembalian();  // Hitung kembalian otomatis
            }
        });

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Barcode", "Jenis", "Harga", "Jumlah", "Subtotal"});
        tbl_penjualan.setModel(model);
    }

    private void initBarcodeComponents() {
        txt_barcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    prosesBarcode();
                }
            }
        });
    }

    private void prosesBarcode() {
        String barcode = txt_barcode.getText().trim();

        if (barcode.isEmpty()) {
            return;
        }

        try {
            java.sql.Connection conn = Config.configDB();
            String sql = "SELECT id, barcode, jenis, harga, stok FROM products WHERE barcode = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, barcode);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String jenis = rs.getString("jenis");
                int harga = rs.getInt("harga");
                int stok = rs.getInt("stok");

                if (stok > 0) {
                    // Format harga ke format Indonesia (Rp)
                    DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                    DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                    formatRp.setCurrencySymbol("Rp ");
                    formatRp.setMonetaryDecimalSeparator(',');
                    formatRp.setGroupingSeparator('.');
                    kursIndonesia.setDecimalFormatSymbols(formatRp);
                    String hargaFormatted = kursIndonesia.format(harga);

                    // Isi text fields saja, tidak menambahkan ke tabel
                    txt_id.setText(id);
                    txt_barcode.setText(barcode);
                    txt_jenis.setText(jenis);
                    txt_harga.setText(hargaFormatted);
//                txt_jumlah.setText("1"); // Default jumlah 1
                } else {
                    JOptionPane.showMessageDialog(this, "Stok habis!");
                    clearTextFields();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Produk tidak ditemukan");
                clearTextFields();
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            clearTextFields();
        }

        txt_barcode.requestFocusInWindow();
        txt_barcode.selectAll(); // Agar teks sebelumnya bisa langsung tergantikan
    }

// Tambahkan fungsi ini untuk membersihkan field jika error
    private void clearTextFields() {
        txt_id.setText("");
        txt_barcode.setText("");
        txt_jenis.setText("");
        txt_harga.setText("");
//    txt_jumlah.setText("");
    }

    private void formatCashInput() {
        try {
            // Ambil posisi kursor sebelum perubahan
            int posisiCursor = txt_cash.getCaretPosition();

            // Ambil teks tanpa "Rp" dan karakter selain angka
            String input = txt_cash.getText().replaceAll("[^\\d]", "");

            // Jika input kosong atau hanya "0", set ke Rp0
            if (input.isEmpty() || input.equals("0")) {
                txt_cash.setText("Rp0");
                txt_cash.setCaretPosition(txt_cash.getText().length()); // Kursor di akhir
                return;
            }

            // Hapus nol di depan angka agar tidak menjadi "0005", cukup "5"
            input = input.replaceFirst("^0+(?!$)", "");

            // Konversi ke angka
            long angka = Long.parseLong(input);

            // Format ke rupiah tanpa simbol default
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("id", "ID"));
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

            formatRp.setCurrencySymbol("Rp");
            formatRp.setGroupingSeparator('.');

            kursIndonesia.setDecimalFormatSymbols(formatRp);
            kursIndonesia.setMaximumFractionDigits(0); // Tidak ada angka di belakang koma

            // Terapkan format
            String hasilFormat = kursIndonesia.format(angka);
            txt_cash.setText(hasilFormat);

            // Hitung panjang tambahan karakter (karena ada tambahan "Rp" dan titik pemisah ribuan)
            int panjangTambahan = hasilFormat.length() - input.length();

            // Pastikan kursor tetap di akhir angka
            txt_cash.setCaretPosition(hasilFormat.length());

        } catch (NumberFormatException e) {
            txt_cash.setText("Rp0"); // Jika input tidak valid, reset ke Rp0
            txt_cash.setCaretPosition(txt_cash.getText().length()); // Atur kursor di akhir
        }
    }

    private void hitungKembalian() {
        try {
            // Ambil nilai dari txt_cash dan txt_subtotalHarga
            String cashStr = txt_cash.getText().replace("Rp", "").replace(".", "").replace(",", ".").trim();
            String totalStr = txt_totalHarga.getText().replace("Rp", "").replace(".", "").replace(",", ".").trim();

            double cash = Double.parseDouble(cashStr);
            double totalHarga = Double.parseDouble(totalStr);

            if (cash >= totalHarga) {
                double kembalian = cash - totalHarga;
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                txt_kembali.setText(formatRupiah.format(kembalian));
            } else {
                txt_kembali.setText("Uang kurang!");
            }
        } catch (NumberFormatException e) {
            txt_kembali.setText("0");
        }
    }

    private void updateTotalHarga() {
        double totalHarga = 0.0;
        DefaultTableModel model = (DefaultTableModel) tbl_penjualan.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            // Ambil nilai subtotal dari kolom ke-6 (index 5)
            String subtotalStr = model.getValueAt(i, 5).toString()
                    .replace("Rp", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();

            try {
                totalHarga += Double.parseDouble(subtotalStr);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Optional: bantu debug kalau ada error format
            }
        }

        // Format ke mata uang Rupiah
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        txt_totalHarga.setText(formatRupiah.format(totalHarga));
    }

    private int getStokProduk(String idProduk) {
        int stok = 0;

        // Loop melalui tabel produk untuk mencari stok berdasarkan ID
        for (int i = 0; i < tbl_products.getRowCount(); i++) {
            String idTabel = tbl_products.getValueAt(i, 0).toString(); // Ambil ID dari tabel

            if (idProduk.equals(idTabel)) { // Jika ID cocok
                stok = Integer.parseInt(tbl_products.getValueAt(i, 3).toString()); // Ambil stok
                break;
            }
        }
        return stok;
    }

    private void updateStokProduk(String idProduk, int stokBaru) {
        // Loop melalui tabel produk untuk mencari dan mengupdate stok berdasarkan ID
        for (int i = 0; i < tbl_products.getRowCount(); i++) {
            String idTabel = tbl_products.getValueAt(i, 0).toString(); // Ambil ID dari tabel

            if (idProduk.equals(idTabel)) { // Jika ID cocok
                tbl_products.setValueAt(stokBaru, i, 3); // Update stok di kolom stok
                break;
            }
        }
    }

    private void Tampilkan_Products() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Barcode", "Jenis", "Stok", "Harga"});
        tbl_products.setModel(model);
        txt_barcode.requestFocus();

        // Format Mata Uang Rupiah
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            java.sql.Connection conn = Config.configDB();
            String sql = "SELECT * FROM products";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String barcode = rs.getString("barcode");
                String nama = rs.getString("jenis");
                int stok = rs.getInt("stok");
                double harga = rs.getDouble("harga");

                // Format harga ke Rupiah
                String hargaRupiah = kursIndonesia.format(harga);

                model.addRow(new Object[]{id, barcode, nama, stok, hargaRupiah});
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
        tbl_products = new javax.swing.JTable();
        tbl_products.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
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
        jScrollPane3 = new javax.swing.JScrollPane();
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
        txt_id = new javax.swing.JTextField();
        txt_barcode = new javax.swing.JTextField();
        txt_jenis = new javax.swing.JTextField();
        txt_harga = new javax.swing.JTextField();
        txt_total = new javax.swing.JTextField();
        btn_ADD = new javax.swing.JButton();
        btn_riset = new javax.swing.JButton();
        txt_totalHarga = new javax.swing.JTextField();
        txt_cash = new javax.swing.JTextField();
        txt_kembali = new javax.swing.JTextField();
        btn_bayar = new javax.swing.JButton();
        btn_product = new javax.swing.JButton();
        btn_penjualan = new javax.swing.JButton();
        btn_pembelian = new javax.swing.JButton();
        btn_preorder = new javax.swing.JButton();
        btn_daftar = new javax.swing.JButton();
        btn_catatan = new javax.swing.JButton();
        btn_keluar = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        bar_bawah = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_products.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_products.setSelectionBackground(new java.awt.Color(0, 204, 255));
        tbl_products.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_productsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_products);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 370, 420));

        tbl_penjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "BARCODE", "JENIS", "HARGA", "JUMLAH"
            }
        ));
        tbl_penjualan.setSelectionBackground(new java.awt.Color(0, 204, 255));
        jScrollPane3.setViewportView(tbl_penjualan);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 500, 360));

        txt_id.setVisible(false);
        getContentPane().add(txt_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 130, -1));

        txt_barcode.setBorder(null);
        getContentPane().add(txt_barcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 80, 20));

        txt_jenis.setBorder(null);
        getContentPane().add(txt_jenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 80, 20));

        txt_harga.setBorder(null);
        getContentPane().add(txt_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 60, 80, 20));

        txt_total.setBorder(null);
        getContentPane().add(txt_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 60, 80, 20));

        btn_ADD.setContentAreaFilled(false);
        btn_ADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ADDActionPerformed(evt);
            }
        });
        getContentPane().add(btn_ADD, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 90, 30));

        btn_riset.setContentAreaFilled(false);
        btn_riset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_risetActionPerformed(evt);
            }
        });
        getContentPane().add(btn_riset, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, 80, 30));

        txt_totalHarga.setBorder(null);
        txt_totalHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalHargaActionPerformed(evt);
            }
        });
        getContentPane().add(txt_totalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 144, 90, 20));

        txt_cash.setBorder(null);
        getContentPane().add(txt_cash, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 206, 90, 20));

        txt_kembali.setBorder(null);
        getContentPane().add(txt_kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(902, 268, 90, 20));

        btn_bayar.setContentAreaFilled(false);
        btn_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bayarActionPerformed(evt);
            }
        });
        getContentPane().add(btn_bayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 310, 90, 40));

        btn_product.setContentAreaFilled(false);
        btn_product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_productActionPerformed(evt);
            }
        });
        getContentPane().add(btn_product, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, 90, 60));

        btn_penjualan.setContentAreaFilled(false);
        btn_penjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_penjualanActionPerformed(evt);
            }
        });
        getContentPane().add(btn_penjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 460, 90, 60));

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

        btn_hapus.setContentAreaFilled(false);
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });
        getContentPane().add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 363, 90, 30));

        bar_bawah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/bawah.png"))); // NOI18N
        getContentPane().add(bar_bawah, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 465, 1000, 90));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/penjualan.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 447));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/login.gif"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 555));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ADDActionPerformed
        // TODO add your handling code here:

        // Ambil data dari JTextField
        String id = txt_id.getText();
        String barcode = txt_barcode.getText();
        String nama = txt_jenis.getText();
        String hargaStr = txt_harga.getText().replace("Rp ", "").replace(".", "").replace(",", ".");
        String jumlahStr = txt_total.getText(); // Jumlah yang dibeli

// Pastikan semua input terisi
        if (id.isEmpty() || barcode.isEmpty() || nama.isEmpty() || hargaStr.isEmpty() || jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Konversi harga dan jumlah ke tipe angka
            double harga = Double.parseDouble(hargaStr);
            int jumlah = Integer.parseInt(jumlahStr);

            // Ambil stok yang tersedia dari tabel tbl_products
            int stokTersedia = getStokProduk(id);

            // Validasi stok
            if (jumlah > stokTersedia) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + stokTersedia, "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_penjualan.getModel();
            boolean produkDitemukan = false;

            // Cek apakah produk sudah ada di tabel
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equals(id)) { // Jika ID produk sama
                    int jumlahLama = Integer.parseInt(model.getValueAt(i, 4).toString());
                    int jumlahBaru = jumlahLama + jumlah;

                    // Pastikan stok mencukupi setelah ditambah
                    if (jumlahBaru > stokTersedia) {
                        JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + stokTersedia, "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Hitung subtotal baru
                    double subtotalBaru = harga * jumlahBaru;
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                    // Update jumlah dan subtotal di tabel
                    model.setValueAt(jumlahBaru, i, 4);
                    model.setValueAt(formatRupiah.format(subtotalBaru), i, 5);

                    produkDitemukan = true;
                    break;
                }
            }

            // Jika produk belum ada di tabel, tambahkan sebagai baris baru
            if (!produkDitemukan) {
                double subtotal = harga * jumlah;
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                String hargaFormatted = formatRupiah.format(harga);
                String subtotalFormatted = formatRupiah.format(subtotal);

                model.addRow(new Object[]{id, barcode, nama, hargaFormatted, jumlah, subtotalFormatted});
            }

            // Kurangi stok di tabel products
            updateStokProduk(id, stokTersedia - jumlah);

            // Bersihkan input setelah ditambahkan
            txt_id.setText("");
            txt_barcode.setText("");
            txt_jenis.setText("");
            txt_harga.setText("");
            txt_total.setText("");

            updateTotalHarga();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid! Pastikan harga dan jumlah adalah angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_ADDActionPerformed

    private void btn_risetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_risetActionPerformed
        // TODO add your handling code here:

        txt_id.setText("");
        txt_barcode.setText("");
        txt_jenis.setText("");
        txt_harga.setText("");
        txt_total.setText("");

    }//GEN-LAST:event_btn_risetActionPerformed

    private void btn_bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bayarActionPerformed
        // TODO add your handling code here:

        try {
            // Cek apakah input kosong sebelum diproses
            if (txt_totalHarga.getText().trim().isEmpty()
                    || txt_cash.getText().trim().isEmpty()
                    || txt_kembali.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return; // Hentikan proses jika ada input kosong
            }

            // Fungsi untuk membersihkan input dari format rupiah
            String totalHargaText = formatAngka(txt_totalHarga.getText());
            String cashText = formatAngka(txt_cash.getText());
            String kembaliText = formatAngka(txt_kembali.getText());

            // Parsing angka ke double
            double totalHarga = Double.parseDouble(totalHargaText);
            double cash = Double.parseDouble(cashText);
            double kembali = Double.parseDouble(kembaliText);

            // Cek apakah cash cukup untuk total harga
            if (cash < totalHarga) {
                JOptionPane.showMessageDialog(this, "Uang tidak cukup untuk pembayaran!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Koneksi ke database
            java.sql.Connection conn = Config.configDB();
            conn.setAutoCommit(false); // Mulai transaksi

            // Insert ke tabel penjualan
            String sqlPenjualan = "INSERT INTO penjualan (id_akun, total, tanggal, cash, kembali) VALUES (?, ?, NOW(), ?, ?)";
            PreparedStatement pstPenjualan = conn.prepareStatement(sqlPenjualan, java.sql.Statement.RETURN_GENERATED_KEYS);

            pstPenjualan.setInt(1, Session.getIdAkun()); // Ambil ID akun dari sesi login
            pstPenjualan.setDouble(2, totalHarga);
            pstPenjualan.setDouble(3, cash);
            pstPenjualan.setDouble(4, kembali);
            pstPenjualan.executeUpdate();

            // Ambil ID penjualan yang baru saja dibuat
            ResultSet rs = pstPenjualan.getGeneratedKeys();
            int idPenjualan = 0;
            if (rs.next()) {
                idPenjualan = rs.getInt(1);
            }
            rs.close();
            pstPenjualan.close();

            // Insert ke tabel detail_penjualan dan update stok produk
            String sqlDetail = "INSERT INTO detail_penjualan (id_penjualan, id_products, jumlah, harga_satuan, subtotal) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);

            String sqlUpdateStok = "UPDATE products SET stok = stok - ? WHERE id = ?";
            PreparedStatement pstUpdateStok = conn.prepareStatement(sqlUpdateStok);

            DefaultTableModel model = (DefaultTableModel) tbl_penjualan.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                int idProduct = Integer.parseInt(model.getValueAt(i, 0).toString()); // ID produk
                int jumlah = Integer.parseInt(model.getValueAt(i, 4).toString()); // Jumlah barang
                double hargaSatuan = Double.parseDouble(formatAngka(model.getValueAt(i, 3).toString())); // Harga satuan
                double subtotal = Double.parseDouble(formatAngka(model.getValueAt(i, 5).toString())); // Subtotal

                // Insert ke detail_penjualan
                pstDetail.setInt(1, idPenjualan);
                pstDetail.setInt(2, idProduct);
                pstDetail.setInt(3, jumlah);
                pstDetail.setDouble(4, hargaSatuan);
                pstDetail.setDouble(5, subtotal);
                pstDetail.executeUpdate();

                // Update stok produk
                pstUpdateStok.setInt(1, jumlah);
                pstUpdateStok.setInt(2, idProduct);
                pstUpdateStok.executeUpdate();
            }

            pstDetail.close();
            pstUpdateStok.close();

            // Commit transaksi
            conn.commit();
            conn.setAutoCommit(true);

            JOptionPane.showMessageDialog(null, "Pembayaran berhasil!.");

// Tambahkan popup konfirmasi cetak nota
            int pilihan = JOptionPane.showConfirmDialog(this, "Apakah Anda ingin mencetak nota?", "Cetak Nota", JOptionPane.YES_NO_OPTION);

            if (pilihan == JOptionPane.YES_OPTION) {
                try {
                    // Buat koneksi ke database
                    java.sql.Connection con = Config.configDB();

                    // Load file jrxml atau jasper
                    JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("src/Nota/notaDidiexs.jasper");

                    // Atau kalau sudah file .jasper:
//                     JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile("src/report/notaDidiexs.jasper");
                    // Buat parameter ke report
                    // Buat parameter ke report
                    Map<String, Object> param = new HashMap<>();
                    param.put("idpenjualan", idPenjualan); // Sesuaikan nama parameter

                    // Isi laporan dengan data
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, param, con);

                    // Tampilkan laporan
                    JasperViewer.viewReport(print, false);
                    System.out.println("ID Penjualan: " + idPenjualan);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mencetak nota: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Reset tabel (hapus semua baris)
            model.setRowCount(0);

            // Reset text field
            txt_totalHarga.setText("");
            txt_cash.setText("");
            txt_kembali.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harap masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_bayarActionPerformed

    private void tbl_productsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_productsMouseClicked
        // TODO add your handling code here:

        // Mendapatkan baris yang diklik
        int row = tbl_products.getSelectedRow();

        // Pastikan ada baris yang dipilih
        if (row != -1) {
            // Tampilkan popup konfirmasi
            int option = JOptionPane.showConfirmDialog(this, "Tambahkan produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            // Jika user memilih "Yes"
            if (option == JOptionPane.YES_OPTION) {
                // Ambil data dari tabel
                String id = tbl_products.getValueAt(row, 0).toString(); // Kolom ID
                String barcode = tbl_products.getValueAt(row, 1).toString(); // Kolom barcode
                String nama = tbl_products.getValueAt(row, 2).toString(); // Kolom Nama
                String harga = tbl_products.getValueAt(row, 4).toString(); // Kolom Harga

                // Masukkan data ke JTextField
                txt_id.setText(id);
                txt_barcode.setText(barcode);
                txt_jenis.setText(nama);
                txt_harga.setText(harga);

                txt_total.requestFocus();
            }
        }

    }//GEN-LAST:event_tbl_productsMouseClicked

    private void txt_totalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalHargaActionPerformed

    private void btn_productActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_productActionPerformed
        // TODO add your handling code here:
        Timer timer = new Timer(5, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    timer.stop();
                    product halamanproduct = new product();
                    halamanproduct.setVisible(true);
                    dispose(); // Tutup login window setelah fade-out
                } else {
                    setOpacity(opacity); // Kurangi opacity
                }
            }
        });
        timer.start();
    }//GEN-LAST:event_btn_productActionPerformed

    private void btn_penjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_penjualanActionPerformed
        // TODO add your handling code here:
        Timer timer = new Timer(5, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    timer.stop();
                    Penjualan halamanpenjualan = new Penjualan();
                    halamanpenjualan.setVisible(true);
                    dispose(); // Tutup login window setelah fade-out
                } else {
                    setOpacity(opacity); // Kurangi opacity
                }
            }
        });
        timer.start();
    }//GEN-LAST:event_btn_penjualanActionPerformed

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

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
        
         DefaultTableModel model = (DefaultTableModel) tbl_penjualan.getModel();

        int selectedRow = tbl_penjualan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih baris yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus item ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(null, "Item berhasil dihapus.");

                // Hitung ulang total harga
                double total = 0.0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    String subtotalStr = model.getValueAt(i, 5).toString(); // Ambil nilai subtotal
                    subtotalStr = formatAngka(subtotalStr); // Hapus format Rp, jika perlu
                    double subtotal = Double.parseDouble(subtotalStr);
                    total += subtotal;
                }

                // Tampilkan kembali total baru
                txt_totalHarga.setText("");
                txt_cash.setText("");
                txt_kembali.setText("");
            }
        }
        
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void configureTableResizable() {
        // Nonaktifkan resize otomatis
        tbl_products.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Aktifkan resize manual kolom
        tbl_products.getTableHeader().setResizingAllowed(true);

        // Atur lebar awal kolom (sesuaikan angka sesuai kebutuhan)
        TableColumnModel columnModel = tbl_products.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(44);   // ID
        columnModel.getColumn(1).setPreferredWidth(70);  // Barcode
        columnModel.getColumn(2).setPreferredWidth(100);  // Nama
        columnModel.getColumn(3).setPreferredWidth(50);   // Stok
        columnModel.getColumn(4).setPreferredWidth(100);  // Harga

        // Pastikan JScrollPane tidak menimpa pengaturan tabel
        JScrollPane scrollPane = (JScrollPane) tbl_products.getParent().getParent();
        scrollPane.setViewportView(tbl_products);
    }
//    private void configtabel() {
//    // Nonaktifkan resize otomatis
//    tbl_penjualan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//    
//    // Aktifkan resize manual kolom
//    tbl_penjualan.getTableHeader().setResizingAllowed(true);
//    
//    // Atur lebar awal kolom (sesuaikan angka sesuai kebutuhan)
//    TableColumnModel columModel = tbl_penjualan.getColumnModel();
//    columModel.getColumn(0).setPreferredWidth(20);   // ID
//    columModel.getColumn(1).setPreferredWidth(70);  // Barcode
//    columModel.getColumn(2).setPreferredWidth(100);  // Nama
//    columModel.getColumn(3).setPreferredWidth(50);   // Stok
//    columModel.getColumn(4).setPreferredWidth(100);  // Harga
//    columModel.getColumn(5).setPreferredWidth(100);
//    
//    // Pastikan JScrollPane tidak menimpa pengaturan tabel
//    JScrollPane scrollPane = (JScrollPane) tbl_penjualan.getParent().getParent();
//    scrollPane.setViewportView(tbl_penjualan);
//}

    private String formatAngka(String text) {
        // Hapus "Rp", hapus titik pemisah ribuan, ganti koma dengan titik
        text = text.replaceAll("[^0-9,]", "").replace(",", ".");

        // Jika ada desimal, cek apakah angka setelah koma nol
        if (text.contains(".")) {
            String[] parts = text.split("\\.");
            if (parts.length > 1 && parts[1].equals("00")) {
                return parts[0]; // Hanya ambil angka sebelum koma jika setelah koma nol
            }
        }
        return text;
    }

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
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Penjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel bar_bawah;
    private javax.swing.JButton btn_ADD;
    private javax.swing.JButton btn_bayar;
    private javax.swing.JButton btn_catatan;
    private javax.swing.JButton btn_daftar;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_keluar;
    private javax.swing.JButton btn_pembelian;
    private javax.swing.JButton btn_penjualan;
    private javax.swing.JButton btn_preorder;
    private javax.swing.JButton btn_product;
    private javax.swing.JButton btn_riset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbl_penjualan;
    private javax.swing.JTable tbl_products;
    private javax.swing.JTextField txt_barcode;
    private javax.swing.JTextField txt_cash;
    private javax.swing.JTextField txt_harga;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_jenis;
    private javax.swing.JTextField txt_kembali;
    private javax.swing.JTextField txt_total;
    private javax.swing.JTextField txt_totalHarga;
    // End of variables declaration//GEN-END:variables
}
