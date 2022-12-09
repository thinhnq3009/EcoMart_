/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package eco.app.panel;

import eco.app.interfaces.ManagementPanelAction;
import eco.app.dao.BrandDao;
import eco.app.dao.ProductDao;
import eco.app.dialog.MessageDialog.MessageType;
import eco.app.entity.Brand;
import eco.app.entity.Category;
import eco.app.entity.EntityHelper;
import eco.app.entity.Product;
import eco.app.helper.ImageHelper;
import eco.app.helper.MessageHelper;
import eco.app.helper.SaveData;
import eco.app.helper.ShareData;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

import static eco.app.helper.Convertor.*;
import eco.app.helper.NavigationHelper;
import eco.app.myswing.MyChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import eco.app.helper.Convertor;

/**
 *
 * @author Lenovo
 */
public class ProductPanel extends javax.swing.JPanel implements ManagementPanelAction<Product> {

    private List<Category> categories;
    private List<Brand> brands;
    private List<Product> products;
    private NavigationHelper nav;
    private byte[] imageSelected;
    private int idProductSelected;
    private byte[] imageDefault;
    private DefaultTableModel model;

    public ProductPanel() {

        initComponents();
        init();
    }

    private void init() {

        // Initialize table model
        model = (DefaultTableModel) tblProduct.getModel();
        fillTable();

        // Set layout and add child components
        setLayout(new MigLayout("fill", "0[]10[]0", "0[]0"));
        add(pnFormProduct, "w 38%, h 100%");
        add(pnListProduct, "w 62%, h 100%");

        // Load data Combobox
        loadComboBox();

        // Initialize auto complete
        initAutoFill();

        nav = new NavigationHelper(btnNew, btnInsert, btnUpdate, btnDelete);
        initDefaultImage();

    }

    private void initDefaultImage() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/eco/app/icon/temp-product-item.png"));
            Image image = icon.getImage();
            imageDefault = ImageHelper.imageToByte(image, "png");
        } catch (IOException ex) {
            Logger.getLogger(ProductPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void clearForm() {
        txtName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtDescription.setText("");
        lblImage.setIcon(new ImageHelper().openImage("temp-product-item.png"));
        cbCategory.setSelectedIndex(0);
        cbBrand.setSelectedIndex(0);
        imageSelected = null;
        txtNote.setText("");
        txtImportPrice.setText("");
        txtExpiry.setText(dateToString(today()));

        // set background
        txtName.runValid();
        txtPrice.runValid();
        txtQuantity.runValid();
        txtDescription.runValid();
        txtImportPrice.runValid();
        txtExpiry.runValid();

    }

    @Override
    public void addTableRow(Product product) {
        model.addRow(EntityHelper.getData(product,
                "id",
                "name",
                "price$",
                "importPrice$",
                "quantity",
                "sold"));
    }

    @Override
    public void fillTable() {
        try {
            ProductDao dao = new ProductDao();
            products = dao.getAll();

            model.setRowCount(0);

            for (Product product : products) {
                addTableRow(product);
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }
    }

    @Override
    public Product readForm(StringBuilder sb) throws Exception {
        if (!validateForm(sb)) {
            throw new Exception("Invalid input data");
        }

        String name = txtName.getText();
        int price = Integer.parseInt(txtPrice.getText());
        int discount = Integer.parseInt(txtImportPrice.getText());
        int quantity = Integer.parseInt(txtQuantity.getText());
        String description = txtDescription.getText();
        Date date = stringToDate(txtExpiry.getText());
        Category category = (Category) cbCategory.getSelectedItem();
        Brand brand = (Brand) cbBrand.getSelectedItem();
        String note = txtNote.getText();

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setEmployeeId(ShareData.USER_LOGIN.getId());
        product.setImportPrice(discount);
        product.setQuantity(quantity);
        product.setTimeAdd(today());
        product.setExpiry(date);
        product.setCategoryId(category.getId());
        product.setBrandId(brand.getId());
        product.setImage(imageSelected == null ? imageDefault : imageSelected);
        product.setDescription(description);
        product.setNote(note);

        return product;

    }

    @Override
    public void fillForm(Product product) {
        idProductSelected = product.getId();
        txtName.setText(product.getName());
        txtPrice.setText(product.getPrice() + "");
        txtImportPrice.setText(product.getImportPrice() + "");
        txtQuantity.setText(product.getQuantity() + "");
        txtDescription.setText(product.getDescription());
        txtExpiry.setText(dateToString(product.getExpiry()));
        txtNote.setText(product.getNote());

        Brand brand = (Brand) EntityHelper.find(brands, product.getBrandId());
        Category category = (Category) EntityHelper.find(categories, product.getCategoryId());
        cbBrand.setSelectedItem(brand);
        cbCategory.setSelectedItem(category);

        imageSelected = product.getImage();
        try {
            Image image = ImageHelper.createImage(imageSelected, "png");
            Image resize = ImageHelper.resize(image);
            ImageIcon icon = new ImageIcon(resize);
            lblImage.setIcon(icon);
        } catch (Exception ex) {
            try {
                ImageIcon icon = new ImageHelper().openImage("temp-product-item.png");
                lblImage.setIcon(icon);
                imageSelected = ImageHelper.imageToByte(icon.getImage(), "png");
            } catch (Exception e) {
                MessageHelper.showException(this, e);
            }
        }
        nav.isEditMode();
    }

    @Override
    public boolean validateForm(StringBuilder sb) {

        txtName.check(sb, "Name can't empty.\n");
        txtPrice.check(sb, "Price is invalid.\n");
        txtQuantity.check(sb, "Quantity is invalid.\n");
        txtExpiry.check(sb, "The expiry date must be in the form (dd/MM/YYYY).\n");
        txtImportPrice.check(sb, "Discount is invalid.\n");
        txtDescription.check(sb, "Description can't empty.\n");

        return sb.isEmpty();
    }

    private void loadComboBox() {
        try {
            // Load BRANDS
            brands = new BrandDao().getAll();
            DefaultComboBoxModel brandModel = new DefaultComboBoxModel();
            for (Brand br : brands) {
                brandModel.addElement(br);
            }
            cbBrand.setModel(brandModel);

            // Load category
            categories = ShareData.CATEGORIES;
            DefaultComboBoxModel categoryModel = new DefaultComboBoxModel();
            for (Category ct : categories) {
                categoryModel.addElement(ct);
            }
            cbCategory.setModel(categoryModel);

        } catch (Exception e) {
        }

    }

    private void initAutoFill() {

        /*
         * auto fill expiry
         */
        txtExpiry.setText(todayString());

        txtExpiry.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {

                    try {

                        Date oldDate = stringToDate(txtExpiry.getText());

                        txtExpiry.setText(dateToString(plusDate(oldDate, 5, 1)));

                    } catch (Exception ex) {
                        ex.printStackTrace();

                        txtExpiry.setText(todayString());

                    }

                    return;

                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {

                    try {

                        Date oldDate = stringToDate(txtExpiry.getText());

                        txtExpiry.setText(dateToString(plusDate(oldDate, 5, -1)));

                    } catch (Exception ex) {
                        ex.printStackTrace();

                        txtExpiry.setText(todayString());

                    }

                    return;

                }

            }

        });



        /*
         * Auto fill Description
         */
        txtDescription.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String name = txtName.getText();
                String price = Convertor.formatCurrency(txtPrice.getText());
                String brand = cbBrand.getSelectedItem().toString();
                String category = cbCategory.getSelectedItem().toString();

                // name: <10.000VND> [Brand] - [Category]
                String description = name + ": <" + price + "VND> [" + brand + "] - [" + category + "]";

                txtDescription.setText(description);
                txtDescription.selectAll();

            }

        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnListProduct = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct = new eco.app.myswing.TableCustom();
        txtFindItem = new eco.app.myswing.TextFieldCustom();
        btnReload = new eco.app.myswing.ButtonRandius();
        cbByName = new eco.app.myswing.CheckBoxCustom();
        cbByID = new eco.app.myswing.CheckBoxCustom();
        pnFormProduct = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtName = new eco.app.myswing.TextFieldCustom();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtPrice = new eco.app.myswing.TextFieldCustom();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtQuantity = new eco.app.myswing.TextFieldCustom();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtExpiry = new eco.app.myswing.TextFieldCustom();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtImportPrice = new eco.app.myswing.TextFieldCustom();
        jPanel12 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbBrand = new eco.app.myswing.ComboBoxCustom();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cbCategory = new eco.app.myswing.ComboBoxCustom();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtDescription = new eco.app.myswing.TextFieldCustom();
        jPanel15 = new javax.swing.JPanel();
        btnNew = new eco.app.myswing.ButtonRandius();
        btnInsert = new eco.app.myswing.ButtonRandius();
        btnUpdate = new eco.app.myswing.ButtonRandius();
        btnDelete = new eco.app.myswing.ButtonRandius();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        gbtnDiscount = new javax.swing.ButtonGroup();
        gbtnFindItem = new javax.swing.ButtonGroup();

        pnListProduct.setBackground(SaveData.BG_CONTENT     );
        pnListProduct.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                pnListProductComponentResized(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 3, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("List Product");

        tblProduct.setAutoCreateRowSorter(true);
        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "Bánh ",  new Integer(3000),  new Double(0.3),  new Integer(300),  new Integer(12)},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Price", "Discount", "Quantity", "Sold"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProduct.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tblProduct.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduct);

        txtFindItem.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtFindItem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFindItemFocusGained(evt);
            }
        });
        txtFindItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFindItemKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFindItemKeyReleased(evt);
            }
        });

        btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eco/app/icon/icons8_sync_25px.png"))); // NOI18N
        btnReload.setText("Reload");
        btnReload.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });

        gbtnFindItem.add(cbByName);
        cbByName.setSelected(true);
        cbByName.setText("By name");
        cbByName.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        cbByName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbByNameMouseClicked(evt);
            }
        });

        gbtnFindItem.add(cbByID);
        cbByID.setText("By ID");
        cbByID.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        cbByID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbByIDMouseClicked(evt);
            }
        });
        cbByID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbByIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnListProductLayout = new javax.swing.GroupLayout(pnListProduct);
        pnListProduct.setLayout(pnListProductLayout);
        pnListProductLayout.setHorizontalGroup(
            pnListProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnListProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnListProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addGroup(pnListProductLayout.createSequentialGroup()
                        .addComponent(txtFindItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(cbByName, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbByID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnListProductLayout.setVerticalGroup(
            pnListProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnListProductLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnListProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFindItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnListProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbByName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbByID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnFormProduct.setBackground(SaveData.BG_CONTENT);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eco/app/icon/temp-product-item.png"))); // NOI18N
        lblImage.setMaximumSize(new java.awt.Dimension(200, 200));
        lblImage.setMinimumSize(new java.awt.Dimension(200, 200));
        lblImage.setPreferredSize(new java.awt.Dimension(200, 200));
        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageMouseClicked(evt);
            }
        });
        jPanel1.add(lblImage, java.awt.BorderLayout.LINE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 15));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(4, 0, 0, 10));

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel8.setText("Name: ");
        jLabel8.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel8.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel7.add(jLabel8, java.awt.BorderLayout.LINE_START);

        txtName.setCanEmpty(false);
        txtName.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtName.setRegex("[^~]+");
        txtName.setValidateAction(ShareData.validateAction);
        jPanel7.add(txtName, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel7);

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel5.setText("Price:");
        jLabel5.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel5.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel4.add(jLabel5, java.awt.BorderLayout.LINE_START);

        txtPrice.setCanEmpty(false);
        txtPrice.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtPrice.setRegex("[0-9]+");
        txtPrice.setValidateAction(ShareData.validateAction);
        jPanel4.add(txtPrice, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4);

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel6.setText("Quantity:");
        jLabel6.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel6.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel5.add(jLabel6, java.awt.BorderLayout.LINE_START);

        txtQuantity.setCanEmpty(false);
        txtQuantity.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtQuantity.setRegex("[0-9]+");
        txtQuantity.setValidateAction(ShareData.validateAction);
        jPanel5.add(txtQuantity, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel7.setText("Expiry:");
        jLabel7.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel7.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel6.add(jLabel7, java.awt.BorderLayout.LINE_START);

        txtExpiry.setCanEmpty(false);
        txtExpiry.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtExpiry.setRegex("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
        txtExpiry.setValidateAction(ShareData.validateAction);
        jPanel6.add(txtExpiry, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel6);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Create New Product");

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(3, 0, 0, 10));

        jPanel11.setOpaque(false);
        jPanel11.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel10.setText("Import price:");
        jLabel10.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel10.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel10.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel11.add(jLabel10, java.awt.BorderLayout.LINE_START);

        txtImportPrice.setCanEmpty(false);
        txtImportPrice.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtImportPrice.setRegex("[0-9]+[.][0-9]{1,2}|[0-9]+|[0-9]{1,2}%");
        txtImportPrice.setValidateAction(ShareData.validateAction);
        jPanel11.add(txtImportPrice, java.awt.BorderLayout.CENTER);

        jPanel12.setOpaque(false);
        jPanel12.setLayout(new java.awt.GridLayout(1, 0, 10, 0));
        jPanel11.add(jPanel12, java.awt.BorderLayout.LINE_END);

        jPanel3.add(jPanel11);

        jPanel8.setOpaque(false);
        jPanel8.setLayout(new java.awt.GridLayout(1, 2, 15, 0));

        jPanel9.setOpaque(false);
        jPanel9.setLayout(new java.awt.BorderLayout(15, 0));

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel9.setText("Brand:");
        jLabel9.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel9.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel9.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel9.add(jLabel9, java.awt.BorderLayout.LINE_START);

        cbBrand.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jPanel9.add(cbBrand, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel9);

        jPanel10.setOpaque(false);
        jPanel10.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel4.setText("Category: ");
        jLabel4.setMaximumSize(new java.awt.Dimension(90, 17));
        jLabel4.setMinimumSize(new java.awt.Dimension(90, 17));
        jLabel4.setPreferredSize(new java.awt.Dimension(90, 17));
        jPanel10.add(jLabel4, java.awt.BorderLayout.LINE_START);

        cbCategory.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jPanel10.add(cbCategory, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel10);

        jPanel3.add(jPanel8);

        jPanel13.setOpaque(false);
        jPanel13.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel11.setText("Description: ");
        jLabel11.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel11.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel13.add(jLabel11, java.awt.BorderLayout.LINE_START);

        txtDescription.setCanEmpty(false);
        txtDescription.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtDescription.setRegex("[^~]+");
        txtDescription.setValidateAction(ShareData.validateAction);
        jPanel13.add(txtDescription, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel13);

        jPanel15.setOpaque(false);
        jPanel15.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnNew.setText("New");
        btnNew.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel15.add(btnNew);

        btnInsert.setBackground(SaveData.BTN_SUCCESS);
        btnInsert.setText("Insert");
        btnInsert.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });
        jPanel15.add(btnInsert);

        btnUpdate.setBackground(SaveData.BTN_WARNING);
        btnUpdate.setText("Update");
        btnUpdate.setEnabled(false);
        btnUpdate.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel15.add(btnUpdate);

        btnDelete.setBackground(SaveData.BTN_DANGER);
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel15.add(btnDelete);

        jPanel14.setOpaque(false);
        jPanel14.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel12.setText("Note: ");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel12.setMaximumSize(new java.awt.Dimension(100, 17));
        jLabel12.setMinimumSize(new java.awt.Dimension(100, 17));
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 17));
        jPanel14.add(jLabel12, java.awt.BorderLayout.LINE_START);

        txtNote.setColumns(20);
        txtNote.setRows(5);
        jScrollPane2.setViewportView(txtNote);

        jPanel14.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pnFormProductLayout = new javax.swing.GroupLayout(pnFormProduct);
        pnFormProduct.setLayout(pnFormProductLayout);
        pnFormProductLayout.setHorizontalGroup(
            pnFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnFormProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnFormProductLayout.setVerticalGroup(
            pnFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormProductLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 798, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbByIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbByIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cbByIDActionPerformed

    private void pnListProductComponentResized(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_pnListProductComponentResized
        if (pnListProduct.getWidth() <= 300) {
            // changeLayout();
        }
    }// GEN-LAST:event_pnListProductComponentResized

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnInsertActionPerformed
        StringBuilder sb = new StringBuilder();
        try {

            ProductDao dao = new ProductDao();

            if (dao.insert(readForm(sb))) {
                MessageHelper.showMessage(this, "Thêm mới sản phẩm thành công");

            } else {
                MessageHelper.showMessage(this, "Thêm mới sản phẩm thát bại");
            }
            clearForm();

        } catch (Exception e) {
            e.printStackTrace();
            sb.append(e.getMessage());
            MessageHelper.showErrorMessage(this, sb.toString());
        }

        fillTable();
    }// GEN-LAST:event_btnInsertActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDeleteActionPerformed
        StringBuilder sb = new StringBuilder();
        try {

            ProductDao dao = new ProductDao();
            Product product = readForm(sb);
            product.setId(idProductSelected);

            MessageType option = MessageHelper.showConfirm(this, "Do you want delete \n" + product.getDescription());

            if (option != MessageType.YES) {
                return;
            }

            if (dao.delete(product)) {
                System.out.println("Xoá sản phẩm thành công");

            } else {
                System.out.println("Xoá sản phẩm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageHelper.showErrorMessage(this, sb.toString());
        }

        fillTable();
        clearForm();
    }// GEN-LAST:event_btnDeleteActionPerformed

    private void lblImageMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_lblImageMouseClicked

        MyChooser chooser = new MyChooser();

        chooser.addFilter("Hình ảnh (.png, .jpg)", "png", "jpg");
        chooser.addFilter("All file", "*");

        boolean option = chooser.showOpenDialog(this);
        if (!option) {
            return;
        }

        File f = chooser.getSelectedFile();
        Image image = new ImageIcon(f.getPath()).getImage();
        lblImage.setIcon(new ImageIcon(ImageHelper.resize(image)));

        try {
            imageSelected = ImageHelper.imageToByte(image, "png");
        } catch (Exception e) {

        }

    }// GEN-LAST:event_lblImageMouseClicked

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnReloadActionPerformed
        fillTable();
    }// GEN-LAST:event_btnReloadActionPerformed

    private void txtFindItemKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtFindItemKeyPressed

    }// GEN-LAST:event_txtFindItemKeyPressed

    private void txtFindItemFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFindItemFocusGained
        txtFindItem.selectAll();
    }// GEN-LAST:event_txtFindItemFocusGained

    private void txtFindItemKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtFindItemKeyReleased

        String key = txtFindItem.getText().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) tblProduct.getModel();

        model.setRowCount(0);

        for (Product product : products) {
            String name = product.getName().toLowerCase();
            String id = String.valueOf(product.getId());
            if (name.contains(key)
                    && cbByName.isSelected()) {
                model.addRow(EntityHelper.getData(product,
                        "id",
                        "name",
                        "price",
                        "discount",
                        "quantity",
                        "sold"));
            } else if (id.contains(key)
                    && cbByID.isSelected()) {
                model.addRow(EntityHelper.getData(product,
                        "id",
                        "name",
                        "price",
                        "discount",
                        "quantity",
                        "sold"));
            }
        }
    }// GEN-LAST:event_txtFindItemKeyReleased

    private void cbByIDMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_cbByIDMouseClicked
        txtFindItemKeyReleased(null);
    }// GEN-LAST:event_cbByIDMouseClicked

    private void cbByNameMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_cbByNameMouseClicked
        txtFindItemKeyReleased(null);
    }// GEN-LAST:event_cbByNameMouseClicked

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblProductMouseClicked
        if (evt.getClickCount() == 2) {
            int index = tblProduct.getSelectedRow();
            int id = (int) tblProduct.getValueAt(index, 0);
            Product product = (Product) EntityHelper.find(products, id);
            fillForm(product);
        }
    }// GEN-LAST:event_tblProductMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNewActionPerformed
        clearForm();
    }// GEN-LAST:event_btnNewActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateActionPerformed
        StringBuilder sb = new StringBuilder();
        try {

            ProductDao dao = new ProductDao();
            Product product = readForm(sb);
            product.setId(idProductSelected);
            if (dao.update(product)) {
                MessageHelper.showMessage(this, "Update product success");
            } else {
                MessageHelper.showMessage(this, "Update product failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageHelper.showErrorMessage(this, sb.toString());
        }

        fillTable();

    }// GEN-LAST:event_btnUpdateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private eco.app.myswing.ButtonRandius btnDelete;
    private eco.app.myswing.ButtonRandius btnInsert;
    private eco.app.myswing.ButtonRandius btnNew;
    private eco.app.myswing.ButtonRandius btnReload;
    private eco.app.myswing.ButtonRandius btnUpdate;
    private eco.app.myswing.ComboBoxCustom cbBrand;
    private eco.app.myswing.CheckBoxCustom cbByID;
    private eco.app.myswing.CheckBoxCustom cbByName;
    private eco.app.myswing.ComboBoxCustom cbCategory;
    private javax.swing.ButtonGroup gbtnDiscount;
    private javax.swing.ButtonGroup gbtnFindItem;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblImage;
    private javax.swing.JPanel pnFormProduct;
    private javax.swing.JPanel pnListProduct;
    private eco.app.myswing.TableCustom tblProduct;
    private eco.app.myswing.TextFieldCustom txtDescription;
    private eco.app.myswing.TextFieldCustom txtExpiry;
    private eco.app.myswing.TextFieldCustom txtFindItem;
    private eco.app.myswing.TextFieldCustom txtImportPrice;
    private eco.app.myswing.TextFieldCustom txtName;
    private javax.swing.JTextArea txtNote;
    private eco.app.myswing.TextFieldCustom txtPrice;
    private eco.app.myswing.TextFieldCustom txtQuantity;
    // End of variables declaration//GEN-END:variables

}
