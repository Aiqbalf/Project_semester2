package App;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

public class product extends javax.swing.JFrame {

    private BufferedImage barcodeImage;

    public product() {
        initComponents();
        setLocationRelativeTo(null);
        Tampilkan_Products();
        loadData();

        txt_Cari.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String query = txt_Cari.getText().trim();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_products.getModel());
                tbl_products.setRowSorter(sorter);

                if (query.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 2));
                }
            }
        });
    }

    public void loadData() {
        DefaultTableModel model = (DefaultTableModel) tbl_products.getModel();
        model.setRowCount(0); // Hapus data lama di tabel

        // Format Mata Uang Rupiah
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/didiexs", "root", "");
            String sql = "SELECT * FROM products"; // Pastikan nama tabel benar
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Format harga ke Rupiah
                String hargaRupiah = kursIndonesia.format(rs.getDouble("harga"));

                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("barcode"),
                    rs.getString("jenis"),
                    rs.getInt("stok"),
                    hargaRupiah // Harga dalam format Rupiah
                };
                model.addRow(row); // Tambahkan data baru ke tabel
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Tampilkan_Products() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Barcode", "Jenis", "Stok", "Harga"});
        tbl_products.setModel(model);

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
                String nama = rs.getString("jenis");
                String barcode = rs.getString("barcode");
                int stok = rs.getInt("stok");
                double harga = rs.getDouble("harga");

                // Format harga ke Rupiah
                String hargaRupiah = kursIndonesia.format(harga);

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

        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
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
        btn_Hapus = new javax.swing.JButton();
        btn_Edit = new javax.swing.JButton();
        btn_Tambah = new javax.swing.JButton();
        txt_Cari = new javax.swing.JTextField();
        btn_barcode = new javax.swing.JButton();
        btn_product = new javax.swing.JButton();
        btn_penjualan = new javax.swing.JButton();
        btn_pembelian = new javax.swing.JButton();
        btn_preorder = new javax.swing.JButton();
        btn_daftar = new javax.swing.JButton();
        btn_catatan = new javax.swing.JButton();
        btn_keluar = new javax.swing.JButton();
        bar_bawah = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/hapus.png"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 54, 55));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/barcode.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 250, 54, 55));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/edit.png"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 140, 54, 55));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/tambah.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 54, 55));

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
        jScrollPane1.setViewportView(tbl_products);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 530, 340));

        btn_Hapus.setContentAreaFilled(false);
        btn_Hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_HapusActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 120, 90));

        btn_Edit.setContentAreaFilled(false);
        btn_Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EditActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 120, 90));

        btn_Tambah.setContentAreaFilled(false);
        btn_Tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TambahActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 120, 90));

        txt_Cari.setBorder(null);
        txt_Cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CariActionPerformed(evt);
            }
        });
        getContentPane().add(txt_Cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 60, 140, 17));

        btn_barcode.setContentAreaFilled(false);
        btn_barcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_barcodeActionPerformed(evt);
            }
        });
        getContentPane().add(btn_barcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 120, 90));

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

        bar_bawah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI_Png/bawah.png"))); // NOI18N
        getContentPane().add(bar_bawah, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 465, 1000, 90));

        jLabel1.setIcon(new FlatSVGIcon("UI_Svg/product.svg"));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 36, 880, 413));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/login.gif"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 555));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private product_tambah tambah = null;
    private product_edit edit = null;

    private void btn_EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EditActionPerformed
        // TODO add your handling code here:

        int selectedRow = tbl_products.getSelectedRow(); // Ambil baris yang dipilih

        if (selectedRow != -1) { // Pastikan ada baris yang dipilih
            // Ambil data dari tabel
            int id = Integer.parseInt(tbl_products.getValueAt(selectedRow, 0).toString());
            String barcode = tbl_products.getValueAt(selectedRow, 1).toString();
            String nama = tbl_products.getValueAt(selectedRow, 2).toString();
            int stok = Integer.parseInt(tbl_products.getValueAt(selectedRow, 3).toString());

            // Ambil harga dalam format Rupiah (contoh: "Rp 10.000")
            String hargaRupiah = tbl_products.getValueAt(selectedRow, 4).toString();

            // Hapus simbol "Rp " dan tanda pemisah ribuan "."
            String hargaTanpaRp = hargaRupiah.replace("Rp ", "").replace(".", "").replace(",", ".");

            // Konversi ke tipe double
            double harga = Double.parseDouble(hargaTanpaRp);

            // Tutup jendela tambah jika masih terbuka
            if (tambah != null && tambah.isDisplayable()) {
                tambah.dispose();
                tambah = null;
            }

            // Jika jendela edit belum dibuat atau sudah ditutup, buat baru
            if (edit == null || !edit.isDisplayable()) {
                edit = new product_edit(this, id, nama, stok, harga);
                edit.setVisible(true);
                edit.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } else {
                edit.toFront();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
        }
    }//GEN-LAST:event_btn_EditActionPerformed


    private void btn_TambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TambahActionPerformed
        // TODO add your handling code here:

        if (edit != null && edit.isDisplayable()) {
            edit.dispose();
            edit = null;
        }

        // Jika jendela tambah belum dibuat atau sudah ditutup, buat baru
        if (tambah == null || !tambah.isDisplayable()) {
            tambah = new product_tambah(this); // Kirim referensi ke ManajemenProducts
            tambah.setVisible(true);
            tambah.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            tambah.pack();
            tambah.setLocationRelativeTo(null);
        } else {
            tambah.toFront();
        }
    }//GEN-LAST:event_btn_TambahActionPerformed


    private void btn_HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HapusActionPerformed
        // TODO add your handling code here:       
        int selectedRow = tbl_products.getSelectedRow(); // Mendapatkan baris yang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String id = tbl_products.getValueAt(selectedRow, 0).toString(); // Ambil ID dari kolom pertama

                java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/didiexs", "root", "");
                String sql = "DELETE FROM products WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id);

                int rowsDeleted = pst.executeUpdate();

                if (rowsDeleted > 0) {
                    DefaultTableModel model = (DefaultTableModel) tbl_products.getModel();
                    model.removeRow(selectedRow); // Hapus dari tabel GUI
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data.");
                }

                pst.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_HapusActionPerformed

    private void txt_CariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CariActionPerformed
        // TODO add your handling code here:

        String query = txt_Cari.getText().trim();

        // Pastikan model tabel menggunakan DefaultTableModel
        DefaultTableModel model = (DefaultTableModel) tbl_products.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_products.setRowSorter(sorter);

        if (query.isEmpty()) {
            sorter.setRowFilter(null); // Tampilkan semua data jika input kosong
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 2)); // Filter berdasarkan kolom "Nama"
        }
    }//GEN-LAST:event_txt_CariActionPerformed

    private void printBarcodeImage(String barcodeText, String productName) {
        // Create a dialog to display and print the barcode
        JDialog printDialog = new JDialog(this, "Cetak Barcode", true);
        printDialog.setSize(300, 200);

        JPanel panel = new JPanel(new BorderLayout());

        // Display barcode image
        JLabel barcodeLabel = new JLabel(new ImageIcon(barcodeImage));
        panel.add(barcodeLabel, BorderLayout.CENTER);

        // Display product info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(new JLabel(productName, JLabel.CENTER));
        infoPanel.add(new JLabel(barcodeText, JLabel.CENTER));
        panel.add(infoPanel, BorderLayout.SOUTH);

        // Print button with proper printing implementation
        JButton btnPrint = new JButton("Print");
        btnPrint.addActionListener(e -> {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Cetak Barcode: " + productName);

            // Set printable content
            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Draw barcode image
                    int x = (int) (pageFormat.getImageableWidth() - barcodeImage.getWidth()) / 2;
                    int y = 20;
                    g2d.drawImage(barcodeImage, x, y, null);

                    // Draw product info below barcode
                    Font font = new Font("Arial", Font.PLAIN, 12);
                    g2d.setFont(font);

                    // Draw product name
                    String name = productName;
                    int nameWidth = g2d.getFontMetrics().stringWidth(name);
                    x = (int) (pageFormat.getImageableWidth() - nameWidth) / 2;
                    y += barcodeImage.getHeight() + 20;
                    g2d.drawString(name, x, y);

                    // Draw barcode text
                    String code = barcodeText;
                    int codeWidth = g2d.getFontMetrics().stringWidth(code);
                    x = (int) (pageFormat.getImageableWidth() - codeWidth) / 2;
                    y += 20;
                    g2d.drawString(code, x, y);

                    return Printable.PAGE_EXISTS;
                }
            });

            // Show print dialog
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(printDialog,
                            "Gagal mencetak: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(btnPrint, BorderLayout.NORTH);
        printDialog.add(panel);
        printDialog.setLocationRelativeTo(this);
        printDialog.setVisible(true);
    }


    private void btn_barcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_barcodeActionPerformed
        // TODO add your handling code here:
        int selectedRow = tbl_products.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih produk terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get barcode from selected row (assuming barcode is in column 1)
        String barcodeText = tbl_products.getValueAt(selectedRow, 1).toString();
        String productName = tbl_products.getValueAt(selectedRow, 2).toString();

        try {
            // Generate barcode
            Barcode barcode = BarcodeFactory.createCode128(barcodeText);
            barcode.setBarWidth(2);
            barcode.setBarHeight(50);

            // Create image from barcode
            barcodeImage = BarcodeImageHandler.getImage(barcode);

            // Show print dialog
            printBarcodeImage(barcodeText, productName);

        } catch (BarcodeException | OutputException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal membuat barcode: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_barcodeActionPerformed

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
        int dialogResult = JOptionPane.showConfirmDialog(this, "apakah anda yakin ingin keluar?","PERINGATAN",dialogBtn);

        if(dialogResult == 0) {
            Login n = new Login();
            n.setVisible(true);
            this.dispose();
        }else{

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
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new product().setVisible(true);
            }
        });
    }

    JTable getTblProducts() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JLabel bar_bawah;
    private javax.swing.JButton btn_Edit;
    private javax.swing.JButton btn_Hapus;
    private javax.swing.JButton btn_Tambah;
    private javax.swing.JButton btn_barcode;
    private javax.swing.JButton btn_catatan;
    private javax.swing.JButton btn_daftar;
    private javax.swing.JButton btn_keluar;
    private javax.swing.JButton btn_pembelian;
    private javax.swing.JButton btn_penjualan;
    private javax.swing.JButton btn_preorder;
    private javax.swing.JButton btn_product;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_products;
    private javax.swing.JTextField txt_Cari;
    // End of variables declaration//GEN-END:variables
}
