/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package eco.app.panel;

import eco.app.dao.CustomerDao;
import eco.app.entity.Customer;
import eco.app.entity.EntityHelper;
import eco.app.helper.Convertor;
import eco.app.helper.MessageHelper;
import eco.app.helper.NavigationHelper;
import eco.app.helper.SaveData;
import eco.app.helper.ShareData;
import eco.app.myswing.ScrollBarCustom;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Lenovo
 */
public class CustomerPanel extends javax.swing.JPanel {

    private MigLayout layout = new MigLayout("fill", "[]10[]", "0[]0");
    private NavigationHelper nav;
    private List<Customer> customers;
    private int idCustomerSelected;

    public CustomerPanel() {
        initComponents();
        init();
    }

    private void init() {

        // Change background color button
        btnInsert.setBackground(SaveData.BTN_SUCCESS);
        btnUpdate.setBackground(SaveData.BTN_WARNING);
        btnDelete.setBackground(SaveData.BTN_DANGER);

        // Set layout
        setLayout(layout);
        add(pnCustomerForm, "w 38%, h 100%");
        add(pnListCustomer, "w 62%, h 100%");

        // init navigator
        nav = new NavigationHelper(btnNew, btnInsert, btnUpdate, btnDelete);

        // Custom scroll bar
        spNote.setVerticalScrollBar(new ScrollBarCustom());
        spNote.setHorizontalScrollBar(new ScrollBarCustom());

        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
        jScrollPane1.setHorizontalScrollBar(new ScrollBarCustom());

        fillTable();
        initAutoFill();
    }

    private void addTableRow(DefaultTableModel model, Customer customer) {
        Object[] obj = EntityHelper.getData(customer,
                "id",
                "fullname",
                "rank",
                "coin",
                "spent$");
        model.addRow(obj);
    }

    private void initAutoFill() {
        txtNote.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (txtNote.getText().isBlank()) {
                    String gender = rdoMale.isSelected() ? "Anh" : "Chị";
                    String name = txtFullname.getText();
                    String phone = txtPhone.getText();
                    String address = txtAddress.getText();

                    // <gender> <name> <phone> <address>
                    String note = gender + " " + name + " " + phone + " " + address;
                    txtNote.setText(note);
                }
                txtNote.selectAll();
            }
        });

    }

    private boolean validateForm(StringBuilder sb) {
        txtFullname.check(sb, "Customer's fullname is empty! \n");
        txtEmail.check(sb, "Email is empty\n");
        txtPhone.check(sb, "Phone is incorect\n");
        txtAddress.check(sb, "Address is empty\n");
        return sb.isEmpty();
    }

    private void showForm(Customer customer) {
        txtFullname.setText(customer.getFullname());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhone());
        txtAddress.setText(customer.getAddress());
        txtNote.setText(customer.getNote());
        rdoMale.setSelected(customer.isGender());
        rdoFemale.setSelected(!customer.isGender());

        idCustomerSelected = customer.getId();
    }

    private void clearForm() {
        txtFullname.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtNote.setText("");
        rdoMale.setSelected(true);
        rdoFemale.setSelected(false);

        idCustomerSelected = 0;
    }

    private Customer readForm(StringBuilder sb) {

        if (!validateForm(sb)) {
            return null;
        }

        Customer customer = new Customer();
        customer.setFullname(txtFullname.getText());
        customer.setEmail(txtEmail.getText());
        customer.setPhone(txtPhone.getText());
        customer.setAddress(txtAddress.getText());
        customer.setGender(rdoMale.isSelected());
        customer.setNote(txtNote.getText());
        customer.setId(idCustomerSelected);

        return customer;

    }

    private void fillTable() {
        try {
            CustomerDao dao = new CustomerDao();

            customers = dao.getAll();

            DefaultTableModel model = (DefaultTableModel) tblCustomer.getModel();

            model.setRowCount(0);

            for (Customer customer : customers) {
                addTableRow(model, customer);
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnListCustomer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCustomer = new eco.app.myswing.TableCustom();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFind = new eco.app.myswing.TextFieldCustom();
        cbByFullname = new eco.app.myswing.CheckBoxCustom();
        cbById = new eco.app.myswing.CheckBoxCustom();
        buttonRandius1 = new eco.app.myswing.ButtonRandius();
        pnCustomerForm = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtFullname = new eco.app.myswing.TextFieldCustom();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        rdoMale = new javax.swing.JRadioButton();
        rdoFemale = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new eco.app.myswing.TextFieldCustom();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtPhone = new eco.app.myswing.TextFieldCustom();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtAddress = new eco.app.myswing.TextFieldCustom();
        jPanel3 = new javax.swing.JPanel();
        btnNew = new eco.app.myswing.ButtonRandius();
        btnInsert = new eco.app.myswing.ButtonRandius();
        btnUpdate = new eco.app.myswing.ButtonRandius();
        btnDelete = new eco.app.myswing.ButtonRandius();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        spNote = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        btngGender = new javax.swing.ButtonGroup();
        btngFind = new javax.swing.ButtonGroup();

        pnListCustomer.setBackground(SaveData.BG_CONTENT);

        tblCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Fullname", "Rank", "Coint", "Spent"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCustomer);

        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel1.setText("Search:");

        txtFind.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtFind.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFindFocusGained(evt);
            }
        });
        txtFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindActionPerformed(evt);
            }
        });
        txtFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFindKeyReleased(evt);
            }
        });

        btngFind.add(cbByFullname);
        cbByFullname.setSelected(true);
        cbByFullname.setText("Fullname");
        cbByFullname.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbByFullname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbByFullnameActionPerformed(evt);
            }
        });

        btngFind.add(cbById);
        cbById.setText("ID");
        cbById.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbById.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbByIdActionPerformed(evt);
            }
        });

        buttonRandius1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eco/app/icon/icons8_sync_25px.png"))); // NOI18N
        buttonRandius1.setText("Reload");
        buttonRandius1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        buttonRandius1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRandius1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(cbByFullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbById, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonRandius1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(cbByFullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbById, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonRandius1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout pnListCustomerLayout = new javax.swing.GroupLayout(pnListCustomer);
        pnListCustomer.setLayout(pnListCustomerLayout);
        pnListCustomerLayout.setHorizontalGroup(
            pnListCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnListCustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnListCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnListCustomerLayout.setVerticalGroup(
            pnListCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnListCustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnCustomerForm.setBackground(SaveData.BG_CONTENT);

        jLabel2.setFont(new java.awt.Font("Roboto", 3, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Create New Customer");

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 15));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(5, 0, 0, 10));

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel4.setText("Fullname: ");
        jLabel4.setMaximumSize(new java.awt.Dimension(150, 17));
        jLabel4.setMinimumSize(new java.awt.Dimension(150, 17));
        jLabel4.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel4.add(jLabel4, java.awt.BorderLayout.LINE_START);

        txtFullname.setCanEmpty(false);
        txtFullname.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtFullname.setRegex("[^~]+");
        txtFullname.setValidateAction(ShareData.validateAction);
        jPanel4.add(txtFullname, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4);

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setText("Gender:");
        jLabel5.setMaximumSize(new java.awt.Dimension(150, 17));
        jLabel5.setMinimumSize(new java.awt.Dimension(150, 17));
        jLabel5.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel5.add(jLabel5, java.awt.BorderLayout.LINE_START);

        jPanel10.setOpaque(false);
        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        btngGender.add(rdoMale);
        rdoMale.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        rdoMale.setSelected(true);
        rdoMale.setText("Male");
        rdoMale.setOpaque(false);
        jPanel10.add(rdoMale);

        btngGender.add(rdoFemale);
        rdoFemale.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        rdoFemale.setText("Female");
        rdoFemale.setOpaque(false);
        jPanel10.add(rdoFemale);

        jPanel5.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel6.setText("Email:");
        jLabel6.setMaximumSize(new java.awt.Dimension(150, 17));
        jLabel6.setMinimumSize(new java.awt.Dimension(150, 17));
        jLabel6.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel6.add(jLabel6, java.awt.BorderLayout.LINE_START);

        txtEmail.setCanEmpty(false);
        txtEmail.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtEmail.setRegex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        txtEmail.setValidateAction(ShareData.validateAction);
        jPanel6.add(txtEmail, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel6);

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel7.setText("Phone:");
        jLabel7.setMaximumSize(new java.awt.Dimension(150, 17));
        jLabel7.setMinimumSize(new java.awt.Dimension(150, 17));
        jLabel7.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel7.add(jLabel7, java.awt.BorderLayout.LINE_START);

        txtPhone.setCanEmpty(false);
        txtPhone.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtPhone.setRegex("[0-9. ]+");
        txtPhone.setValidateAction(ShareData.validateAction);
        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
            }
        });
        jPanel7.add(txtPhone, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel7);

        jPanel8.setOpaque(false);
        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setText("Address:");
        jLabel8.setMaximumSize(new java.awt.Dimension(150, 17));
        jLabel8.setMinimumSize(new java.awt.Dimension(150, 17));
        jLabel8.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel8.add(jLabel8, java.awt.BorderLayout.LINE_START);

        txtAddress.setCanEmpty(false);
        txtAddress.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtAddress.setRegex("[^~]+");
        txtAddress.setValidateAction(ShareData.validateAction);
        jPanel8.add(txtAddress, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel8);

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        btnNew.setText("New");
        btnNew.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel3.add(btnNew);

        btnInsert.setText("Insert");
        btnInsert.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });
        jPanel3.add(btnInsert);

        btnUpdate.setText("Update");
        btnUpdate.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel3.add(btnUpdate);

        btnDelete.setText("Delete");
        btnDelete.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel3.add(btnDelete);

        jPanel9.setOpaque(false);
        jPanel9.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Note:");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel9.setMaximumSize(new java.awt.Dimension(150, 17));
        jLabel9.setMinimumSize(new java.awt.Dimension(150, 17));
        jLabel9.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel9.add(jLabel9, java.awt.BorderLayout.LINE_START);

        txtNote.setColumns(20);
        txtNote.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtNote.setRows(5);
        spNote.setViewportView(txtNote);

        jPanel9.add(spNote, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pnCustomerFormLayout = new javax.swing.GroupLayout(pnCustomerForm);
        pnCustomerForm.setLayout(pnCustomerFormLayout);
        pnCustomerFormLayout.setHorizontalGroup(
            pnCustomerFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnCustomerFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnCustomerFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnCustomerFormLayout.setVerticalGroup(
            pnCustomerFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCustomerFormLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            Customer customer = readForm(sb);

            if (customer == null) {
                MessageHelper.showErrorMessage(this, sb + "");
            } else {
                CustomerDao dao = new CustomerDao();
                if (dao.delete(customer)) {
                    MessageHelper.showMessage(this, "Xoá thành công");
                } else {
                    MessageHelper.showMessage(this, "Xoá thất bại");
                }
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }

        fillTable();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtFindFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFindFocusGained
        txtFind.selectAll();
    }//GEN-LAST:event_txtFindFocusGained

    private void txtFindKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFindKeyReleased

        String key = txtFind.getText().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) tblCustomer.getModel();

        model.setRowCount(0);

        for (Customer customer : customers) {
            if (customer.getFullname().toLowerCase().contains(key)
                    && cbByFullname.isSelected()) {
                addTableRow(model, customer);

            } else if ((customer.getId() + "").contains(key)) {
                addTableRow(model, customer);
            }
        }

    }//GEN-LAST:event_txtFindKeyReleased

    private void cbByFullnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbByFullnameActionPerformed
        txtFindKeyReleased(null);
    }//GEN-LAST:event_cbByFullnameActionPerformed

    private void cbByIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbByIdActionPerformed
        txtFindKeyReleased(null);
    }//GEN-LAST:event_cbByIdActionPerformed

    private void buttonRandius1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRandius1ActionPerformed

        DefaultTableModel model = (DefaultTableModel) tblCustomer.getModel();

        model.setRowCount(0);

        for (Customer customer : customers) {
            addTableRow(model, customer);
        }


    }//GEN-LAST:event_buttonRandius1ActionPerformed

    private void txtFindActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFindActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFindActionPerformed

    private void cbFullnameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbFullnameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cbFullnameActionPerformed

    private void cbIdActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbIdActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cbIdActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtPhoneActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNewActionPerformed
        clearForm();
    }// GEN-LAST:event_btnNewActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnInsertActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            Customer customer = readForm(sb);

            if (customer == null) {
                MessageHelper.showErrorMessage(this, sb + "");
            } else {
                CustomerDao dao = new CustomerDao();
                if (dao.insert(customer)) {
                    MessageHelper.showMessage(this, "Thêm mới thành công");
                } else {
                    MessageHelper.showMessage(this, "Thêm mới thất bại");
                }
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }
        clearForm();
        fillTable();
    }// GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            Customer customer = readForm(sb);

            if (customer == null) {
                MessageHelper.showErrorMessage(this, sb + "");
            } else {
                CustomerDao dao = new CustomerDao();
                if (dao.update(customer)) {
                    MessageHelper.showMessage(this, "Cập nhật thành công");
                } else {
                    MessageHelper.showMessage(this, "Cập nhật thất bại");
                }
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }

        fillTable();
    }// GEN-LAST:event_btnUpdateActionPerformed

    private void tblCustomerMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblCustomerMouseClicked
        if (evt.getClickCount() == 2) {
            int index = tblCustomer.getSelectedRow();
            int id = (int) tblCustomer.getValueAt(index, 0);

            Customer customer = (Customer) EntityHelper.find(customers, id);
            showForm(customer);
            nav.isEditMode();
        }
    }// GEN-LAST:event_tblCustomerMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private eco.app.myswing.ButtonRandius btnDelete;
    private eco.app.myswing.ButtonRandius btnInsert;
    private eco.app.myswing.ButtonRandius btnNew;
    private eco.app.myswing.ButtonRandius btnUpdate;
    private javax.swing.ButtonGroup btngFind;
    private javax.swing.ButtonGroup btngGender;
    private eco.app.myswing.ButtonRandius buttonRandius1;
    private eco.app.myswing.CheckBoxCustom cbByFullname;
    private eco.app.myswing.CheckBoxCustom cbById;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnCustomerForm;
    private javax.swing.JPanel pnListCustomer;
    private javax.swing.JRadioButton rdoFemale;
    private javax.swing.JRadioButton rdoMale;
    private javax.swing.JScrollPane spNote;
    private eco.app.myswing.TableCustom tblCustomer;
    private eco.app.myswing.TextFieldCustom txtAddress;
    private eco.app.myswing.TextFieldCustom txtEmail;
    private eco.app.myswing.TextFieldCustom txtFind;
    private eco.app.myswing.TextFieldCustom txtFullname;
    private javax.swing.JTextArea txtNote;
    private eco.app.myswing.TextFieldCustom txtPhone;
    // End of variables declaration//GEN-END:variables
}
