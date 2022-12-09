/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package eco.app.panel;

import eco.app.component.ProductItem;
import eco.app.dao.OrderDao;
import eco.app.dao.ProductDao;
import eco.app.dao.VoucherDao;
import eco.app.dialog.BillPreview;
import eco.app.dialog.SearchCustomer;
import eco.app.entity.Brand;
import eco.app.entity.Category;
import eco.app.entity.Customer;
import eco.app.entity.Order;
import eco.app.entity.Product;
import eco.app.entity.Product.BillItem;
import eco.app.entity.Voucher;
import eco.app.event.ProductItemListener;
import eco.app.helper.Convertor;
import eco.app.helper.MessageHelper;
import eco.app.helper.SaveData;
import eco.app.helper.ShareData;
import eco.app.myswing.ScrollBarCustom;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Lenovo
 */
public class SellingPanel extends javax.swing.JPanel {

    /**
     * Creates new form SellingPanel
     */
    private MigLayout layout = new MigLayout("fill", "0[]10[]0", "0[]0");
    private List<Product> products;
    private List<BillItem> productsInBill;
    private ProductItemListener itemListener;

    private Customer customer;
    private Voucher voucher;

    private int idBrandFiller = 0;
    private int idCategoryFiller = 0;
    private int totalBill = 0;
    private int discount;

    public SellingPanel() {
        initComponents();
        init();
    }

    private void init() {

        // Initialize variables
        productsInBill = new java.util.ArrayList<>();

        setBackground(new Color(255, 153, 102));

        setLayout(layout);
        add(pnFindProduct, "w 720!, h 100%");
        add(pnOrder, "w 100%, h 100%");

        // Design ScrollPanel
        ScrollBarCustom sbc = new ScrollBarCustom();
        sbc.setForeground(new Color(0, 102, 255));
        sbc.setBackground(scProduct.getBackground());
        scProduct.setVerticalScrollBar(sbc);

        //
        pnListProduct.setLayout(new MigLayout("fillx, insets ", "0[]0", "[]5[]"));

        initProductItemEvent();
        getAllProduct();
        fillComboBox();
    }

    /**
     * Initialization event when click button add product in <code>ProductItem<code> panel
     */
    private void initProductItemEvent() {
        itemListener = new ProductItemListener() {
            @Override
            public void onClick(Product product, JTextField textField) {

                try {
                    int quantity = Integer.parseInt(textField.getText());

                    // Before append to order check quantity need to append with product remain
                    if (!validateQuantityProduct(product, quantity)) {
                        String message = "Not enough products to add to the invoice. Try with less quantity";
                        MessageHelper.showErrorMessage(SellingPanel.this, message);
                        return;
                    }

                    product.sell(quantity);

                    BillItem item = new BillItem(product, quantity);

                    /* 
                    Check this product has been added
                    If it added, increase number of this product
                     */
                    for (BillItem billItem : productsInBill) {
                        if (product.equals(billItem.getProduct())) {
                            billItem.append(quantity);
                            reloadBill();
                            return;
                        }
                    }

                    productsInBill.add(item);
                    reloadBill();

                } catch (NumberFormatException e) { // Catch exception at Integer.parseInt("...");
                    MessageHelper.showException(SellingPanel.this, e);
                }

            }

        };

    }

    /**
     * Check product's quantity with number of product need to add to the order
     *
     * @param product
     * @param need
     * @return <code>true</code>if product's quantity more than number of
     * product need to add to the order. Otherwise return <code>false</code>.
     */
    private boolean validateQuantityProduct(Product product, int need) {

        int remainProduct = product.getQuantity() - product.getSold();

        return remainProduct >= need;

    }

    private void fillComboBox() {
        try {
            List<Brand> brands = ShareData.BRANDS;
            DefaultComboBoxModel brandModel = (DefaultComboBoxModel) cbbBrand.getModel();
            for (Brand brand : brands) {
                brandModel.addElement(brand);
            }

            List<Category> categories = ShareData.CATEGORIES;
            DefaultComboBoxModel categoryModel = (DefaultComboBoxModel) cbbCategory.getModel();
            for (Category category : categories) {
                categoryModel.addElement(category);
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }
    }

    /**
     * Using list <code>productInBill</code> and <code>voucher</code> to
     * calculate and show <code>totalBill</code> and <code>discount</code>
     */
    private void reloadBill() {

        totalBill = 0;

        DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
        model.setRowCount(0);
        for (BillItem item : productsInBill) {
            model.addRow(new Object[]{
                item.getProduct().getName(),
                item.getQuantity(),
                Convertor.formatCurrency(item.getProduct().getPrice()),
                Convertor.formatCurrency(item.getTotal())
            });
            totalBill += item.getTotal();
        }

        if (voucher != null) {
            txtDiscount.setText(voucher.getDiscount(totalBill) + " VND");
        }
        discount = (int) (voucher == null ? 0 : voucher.getDiscount(totalBill));
        totalBill -= discount;
        totalBill = totalBill < 0 ? 0 : totalBill;

        txtTotal.setText(Convertor.formatCurrency(totalBill) + " VND");
    }

    /**
     * Remove all <code>ProductItem</code> in <code>pnListProduct</code>, then
     * create and add new <code>ProductItem</code> using list product <code> products
     * </code>
     */
    private void getAllProduct() {
        pnListProduct.removeAll();
        try {

            ProductDao dao = new ProductDao();

            products = dao.getAll();

            for (Product product : products) {
                addProduct(product);
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }
    }

    /**
     * Initialization new <code>ProductItem</code> panel and add it to
     * <code>pnListProduct</code>. The <code>pnListProduct</code> will be update
     * display
     *
     * @param product add to <code>ProductItem</code>
     */
    public void addProduct(Product product) {
        ProductItem item = new ProductItem(product);
        pnListProduct.add(item, "wrap, w 100%");
        item.addPlusListener(itemListener);
        revalidate();
        repaint();
    }

    /**
     * Using <code>customer</code> <code>voucher</code>
     * <code>productInBill</code> to create new Order. If have exception this
     * order will be destroy
     */
    private void completeOrder() {

        if (productsInBill.isEmpty()) {
            MessageHelper.showErrorMessage(this, "The bill cannot be without products");
            return;
        }

        try {

            OrderDao dao = new OrderDao();

            Order order = dao.createOrder(customer, voucher, productsInBill, discount);

            if (customer != null) {
                customer.appendCoin(totalBill);
            }

            if (order != null) {
                BillPreview previewDialog = new BillPreview(null, true);

                previewDialog.setOrder(order);
                previewDialog.setBillItems(productsInBill);
                previewDialog.setVoucher(voucher);
                previewDialog.setVisible(true);

            } else {
                throw new IllegalArgumentException("Can't create new order now");
            }

        } catch (IllegalAccessException e) {
            MessageHelper.showException(this, e);
        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }
        clearOrder(null);
        getAllProduct();
    }

    /**
     * Using text in <code>txtVoucher</code> field to get Voucher in Database.
     * If don't have voucher with this text, global variable
     * <code>voucher</code> will be null
     */
    private void updateVoucherField() {
        try {
            String code = txtVoucher.getText();

            if (code.isBlank()) {
                return;
            }

            VoucherDao dao = new VoucherDao();

            List<Voucher> vouchers = dao.findByCode(code);

            if (!vouchers.isEmpty()) {
                voucher = vouchers.get(0);
                txtVoucher.setLineColor(SaveData.BTN_SUCCESS);
                txtVoucher.setText(voucher.getCode());

                String discount = Convertor.formatCurrency((int) voucher.getDiscount(totalBill));
                txtDiscount.setText(discount + " VND");
            } else {
                voucher = null;
                txtVoucher.setLineColor(SaveData.BTN_DANGER);
                txtDiscount.setText("0 VND");
                txtVoucher.setFocusable(true);
            }
            reloadBill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnFindProduct = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtFind = new eco.app.myswing.TextFieldCustom();
        btnReload = new eco.app.myswing.ButtonRandius();
        jLabel1 = new javax.swing.JLabel();
        cboCategory = new javax.swing.JLabel();
        cbbCategory = new eco.app.myswing.ComboBoxCustom();
        cbbBrand = new eco.app.myswing.ComboBoxCustom();
        scProduct = new javax.swing.JScrollPane();
        pnListProduct = new javax.swing.JPanel();
        pnOrder = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBill = new eco.app.myswing.TableCustom();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCustomer = new eco.app.myswing.TextFieldCustom();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtVoucher = new eco.app.myswing.TextFieldCustom();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtDiscount = new eco.app.myswing.TextFieldCustom();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new eco.app.myswing.TextFieldCustom();
        jPanel3 = new javax.swing.JPanel();
        btnDelete = new eco.app.myswing.ButtonRandius();
        btnClear = new eco.app.myswing.ButtonRandius();
        btnComplete = new eco.app.myswing.ButtonRandius();

        pnFindProduct.setBackground(SaveData.BG_CONTENT );

        jPanel2.setOpaque(false);

        txtFind.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        txtFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFindKeyReleased(evt);
            }
        });

        btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eco/app/icon/icons8_sync_25px.png"))); // NOI18N
        btnReload.setText("Reload");
        btnReload.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Brand: ");

        cboCategory.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        cboCategory.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cboCategory.setText("Category: ");

        cbbCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        cbbCategory.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbbCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbCategoryActionPerformed(evt);
            }
        });

        cbbBrand.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        cbbBrand.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbbBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbBrandActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbbBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbbCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbbBrand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(txtFind, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        pnListProduct.setOpaque(false);

        javax.swing.GroupLayout pnListProductLayout = new javax.swing.GroupLayout(pnListProduct);
        pnListProduct.setLayout(pnListProductLayout);
        pnListProductLayout.setHorizontalGroup(
            pnListProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        pnListProductLayout.setVerticalGroup(
            pnListProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 188, Short.MAX_VALUE)
        );

        scProduct.setViewportView(pnListProduct);

        javax.swing.GroupLayout pnFindProductLayout = new javax.swing.GroupLayout(pnFindProduct);
        pnFindProduct.setLayout(pnFindProductLayout);
        pnFindProductLayout.setHorizontalGroup(
            pnFindProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFindProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFindProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scProduct)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnFindProductLayout.setVerticalGroup(
            pnFindProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFindProductLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scProduct)
                .addContainerGap())
        );

        pnOrder.setBackground(pnFindProduct.getBackground());
        pnOrder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                pnOrderComponentResized(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 3, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Order Detail");

        tblBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product", "Quantity", "Price", "Total"
            }
        ));
        jScrollPane1.setViewportView(tblBill);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(4, 0, 0, 10));

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout(25, 0));

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel3.setText("Customer: ");
        jLabel3.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel3.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel4.add(jLabel3, java.awt.BorderLayout.LINE_START);

        txtCustomer.setEditable(false);
        txtCustomer.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCustomer.setText("Click to select customer");
        txtCustomer.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustomerMouseClicked(evt);
            }
        });
        jPanel4.add(txtCustomer, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4);

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout(25, 0));

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel4.setText("Voncher: ");
        jLabel4.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel4.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel5.add(jLabel4, java.awt.BorderLayout.LINE_START);

        txtVoucher.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtVoucher.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtVoucher.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVoucherFocusLost(evt);
            }
        });
        txtVoucher.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVoucherKeyReleased(evt);
            }
        });
        jPanel5.add(txtVoucher, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.BorderLayout(25, 0));

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setText("Discount: ");
        jLabel5.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel5.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel6.add(jLabel5, java.awt.BorderLayout.LINE_START);

        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscount.setText("-0 VND");
        txtDiscount.setEnabled(false);
        txtDiscount.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiscountActionPerformed(evt);
            }
        });
        jPanel6.add(txtDiscount, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6);

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.BorderLayout(25, 0));

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel6.setText("Total:");
        jLabel6.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel6.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel7.add(jLabel6, java.awt.BorderLayout.LINE_START);

        txtTotal.setEditable(false);
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.setText("0 VND");
        txtTotal.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });
        jPanel7.add(txtTotal, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel7);

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        btnDelete.setBackground(SaveData.BTN_DANGER);
        btnDelete.setText("Delete");
        btnDelete.setFont(new java.awt.Font("Roboto", 3, 14)); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel3.add(btnDelete);

        btnClear.setBackground(SaveData.BTN_WARNING
        );
        btnClear.setText("Clear");
        btnClear.setFont(new java.awt.Font("Roboto", 3, 14)); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearOrder(evt);
            }
        });
        jPanel3.add(btnClear);

        btnComplete.setBackground(SaveData.BTN_SUCCESS);
        btnComplete.setText("Complete");
        btnComplete.setFont(new java.awt.Font("Roboto", 3, 14)); // NOI18N
        btnComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteActionPerformed(evt);
            }
        });
        jPanel3.add(btnComplete);

        javax.swing.GroupLayout pnOrderLayout = new javax.swing.GroupLayout(pnOrder);
        pnOrder.setLayout(pnOrderLayout);
        pnOrderLayout.setHorizontalGroup(
            pnOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnOrderLayout.setVerticalGroup(
            pnOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnOrderLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 337, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void clearOrder(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearOrder
        productsInBill = new ArrayList<>();
        customer = null;
        voucher = null;

        txtCustomer.setText("Click to choose customer");
        txtVoucher.setText("");

        reloadBill();
    }//GEN-LAST:event_clearOrder

    private void txtVoucherFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVoucherFocusLost
        try {
            String code = txtVoucher.getText();

            if (code.isBlank()) {
                return;
            }

            VoucherDao dao = new VoucherDao();

            List<Voucher> vouchers = dao.findByCode(code);

            if (!vouchers.isEmpty()) {
                voucher = vouchers.get(0);
                txtVoucher.setLineColor(SaveData.BTN_SUCCESS);
                txtVoucher.setText(voucher.getCode());

                String discount = Convertor.formatCurrency((int) voucher.getDiscount(totalBill));
                txtDiscount.setText(discount + " VND");
            } else {
                voucher = null;
                txtVoucher.setLineColor(SaveData.BTN_DANGER);
                txtDiscount.setText("0 VND");
                // [code] is not available
                MessageHelper.showMessage(this, "This voucher is not available");
                txtVoucher.setText("");
            }

            reloadBill();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtVoucherFocusLost

    private void txtCustomerMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_txtCustomerMouseClicked
        SearchCustomer sc = new SearchCustomer(null, true);
        sc.setVisible(true);
        customer = sc.getCustomerSelected();
        if (customer != null) {
            String fullname = customer.getFullname();
            int id = customer.getId();

            txtCustomer.setText("[" + id + "] " + fullname);
        } else {
            txtCustomer.setText("");
        }

    }// GEN-LAST:event_txtCustomerMouseClicked

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTotalActionPerformed

    private void cbbCategoryActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbbCategoryActionPerformed
        Object selectedItem = cbbCategory.getSelectedItem();

        if (selectedItem instanceof Category category) {
            idCategoryFiller = category.getId();
        } else {
            idCategoryFiller = 0;
        }

        txtFindKeyReleased(null);

    }// GEN-LAST:event_cbbCategoryActionPerformed

    private void txtFindKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtFindKeyReleased
        pnListProduct.removeAll();
        String key = txtFind.getText().toLowerCase();
        for (Product product : products) {

            // Compare name with key
            if (product.getName().toLowerCase().contains(key)
                    // Compare brand
                    && (product.getBrandId() == idBrandFiller || idBrandFiller == 0)
                    // Compare category
                    && (product.getCategoryId() == idCategoryFiller || idCategoryFiller == 0)) {
                addProduct(product);
            }
        }
        revalidate();
        repaint();
    }// GEN-LAST:event_txtFindKeyReleased

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDeleteActionPerformed
        int index = tblBill.getSelectedRow();
        if (index != -1) {
            productsInBill.remove(index);
            reloadBill();
        }
    }// GEN-LAST:event_btnDeleteActionPerformed

    private void txtVoucherKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtVoucherKeyReleased
        updateVoucherField();
    }// GEN-LAST:event_txtVoucherKeyReleased

    private void btnCompleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCompleteActionPerformed
        completeOrder();
    }// GEN-LAST:event_btnCompleteActionPerformed

    private void txtDiscountActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtDiscountActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtDiscountActionPerformed

    private void cbbBrandActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbbBrandActionPerformed
        Object selectedItem = cbbBrand.getSelectedItem();

        if (selectedItem instanceof Brand brand) {
            idBrandFiller = brand.getId();
        } else {
            idBrandFiller = 0;
        }

        txtFindKeyReleased(null);
    }// GEN-LAST:event_cbbBrandActionPerformed

    private void pnOrderComponentResized(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_pnOrderComponentResized
        if (pnOrder.getWidth() <= 300) {
            // changeLayout();
        }
    }// GEN-LAST:event_pnOrderComponentResized

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnReloadActionPerformed
        
        cbbCategory.setSelectedIndex(0);
        cbbBrand.setSelectedIndex(0);
        getAllProduct();
    }// GEN-LAST:event_btnReloadActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private eco.app.myswing.ButtonRandius btnClear;
    private eco.app.myswing.ButtonRandius btnComplete;
    private eco.app.myswing.ButtonRandius btnDelete;
    private eco.app.myswing.ButtonRandius btnReload;
    private eco.app.myswing.ComboBoxCustom cbbBrand;
    private eco.app.myswing.ComboBoxCustom cbbCategory;
    private javax.swing.JLabel cboCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnFindProduct;
    private javax.swing.JPanel pnListProduct;
    private javax.swing.JPanel pnOrder;
    private javax.swing.JScrollPane scProduct;
    private eco.app.myswing.TableCustom tblBill;
    private eco.app.myswing.TextFieldCustom txtCustomer;
    private eco.app.myswing.TextFieldCustom txtDiscount;
    private eco.app.myswing.TextFieldCustom txtFind;
    private eco.app.myswing.TextFieldCustom txtTotal;
    private eco.app.myswing.TextFieldCustom txtVoucher;
    // End of variables declaration//GEN-END:variables
}
