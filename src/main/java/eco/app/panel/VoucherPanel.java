package eco.app.panel;

import eco.app.dao.VoucherDao;
import eco.app.dialog.MessageDialog.MessageType;
import eco.app.entity.Voucher;
import eco.app.helper.Convertor;
import eco.app.helper.MessageHelper;
import eco.app.helper.SaveData;
import eco.app.helper.ShareData;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

import eco.app.entity.EntityHelper;
import eco.app.helper.NavigationHelper;
import eco.app.myswing.ScrollBarCustom;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Lenovo
 */
public class VoucherPanel extends javax.swing.JPanel {

    private final MigLayout layout = new MigLayout("fill", "0[]10[]0", "0[]0");

    private List<Voucher> vouchers;
    private int idVoucherSelected;

    private int total = 0;
    private int totalValue = 0;
    private int totalUsed = 0;
    private int totalValueUsed = 0;

    private NavigationHelper nav;

    /**
     * Creates new form VoucherForm
     */
    public VoucherPanel() {
        initComponents();
        setLayout(layout);

        init();

    }

    public boolean validateForm(StringBuilder sb) {
        txtCode.check(sb, "Code is empty.\n");
        txtDiscount.check(sb, "Discount is empty.\n");
        txtMaxDiscount.check(sb, "Value is empty.\n");
        txtMinApply.check(sb, "Min apply is empty.\n");
        txtMaxDiscount.check(sb, "Max apply is empty.\n");
        txtExpiry.check(sb, "Expiry is empty.\n");

        return sb.isEmpty();
    }

    private Voucher readFrom(StringBuilder sb) {

        if (!validateForm(sb)) {
            return null;
        }

        Voucher voucher = new Voucher();

        voucher.setEmployeeId(ShareData.USER_LOGIN.getId());
        voucher.setCode(txtCode.getText());
        voucher.setMaxDiscount(Integer.parseInt(txtMaxDiscount.getText()));
        voucher.setMinApply(Integer.parseInt(txtMinApply.getText()));
        voucher.setDiscount(Double.parseDouble(txtDiscount.getText()));
        voucher.setDescription(txtDescription.getText());
        voucher.setIsUsed(false);
        try {
            voucher.setExpiry(Convertor.stringToDate(txtExpiry.getText()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return voucher;
    }

    private void clearForm() {
        txtCode.setText("");
        txtDiscount.setText("");
        txtMaxDiscount.setText("");
        txtMinApply.setText("");
        txtDescription.setText("");
        txtExpiry.setText(Convertor.todayString());
        nav.isCreateMode();
    }

    private void showForm(Voucher v) {

        String newCode = v.getCode();

        newCode += newCode.endsWith(v.getId() + "") ? "" : v.getId() + "";

        txtCode.setText(newCode);
        txtDiscount.setText(v.getDiscount() + "");
        txtMaxDiscount.setText(v.getMaxDiscount() + "");
        txtMinApply.setText(v.getMinApply() + "");
        txtDescription.setText(v.getDescription());
        txtClone.setText("1");
        txtExpiry.setText(Convertor.dateToString(v.getExpiry()));

        if (v.isIsUsed()) {
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }

    }

    private void init() {
        add(pnFormVoucher, "w 38%, h 100%");
        add(pnListVoucher, "w 62%, h 100%");

        nav = new NavigationHelper(btnNew, btnInsert, btnUpdate, btnDelete);
        nav.addComponentCreate(txtClone);

        spTable.setVerticalScrollBar(new ScrollBarCustom());

        txtFind.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                findVoucher();
            }
        });

        fillTable();

    }

    private void findVoucher() {
        String key = txtFind.getText().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) tblVoucher.getModel();

        model.setRowCount(0);

        for (Voucher voucher : vouchers) {
            if (voucher.getCode().toLowerCase().contains(key) && cbByCode.isSelected()) {
                addRowTable(model, voucher);
            } else if ((voucher.getId() + "").contains(key) && cbByID.isSelected()) {
                addRowTable(model, voucher);
            }
        }

    }

    /**
     * -Set <code>total, totalUsed, totalValue, totalValueUsed</code> = 0, then
     * get and show all Voucher in Database on table. -
     */
    private void fillTable() {

        total = 0;
        totalUsed = 0;
        totalValue = 0;
        totalValueUsed = 0;

        try {
            DefaultTableModel model = (DefaultTableModel) tblVoucher.getModel();

            model.setRowCount(0);

            vouchers = new VoucherDao().getAll();

            for (Voucher voucher : vouchers) {
                addRowTable(model, voucher);
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }

    }

    private void updateVoucher() {
        StringBuilder sb = new StringBuilder();
        Voucher voucher = readFrom(sb);
        voucher.setId(idVoucherSelected);

        if (!sb.isEmpty()) {
            MessageHelper.showMessage(this, sb.toString());
            return;
        }

        try {
            VoucherDao dao = new VoucherDao();

            if (dao.isExist(voucher.getCode())) {
                MessageHelper.showErrorMessage(this,
                        "To update the discount code, the new code must not be the same as the old code");
                return;
            }

            if (dao.update(voucher)) {
                MessageHelper.showMessage(this, "Update successfull.");
            } else {
                MessageHelper.showMessage(this, "Update unsuccessfull.");
            }

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }

        fillTable();

    }

    /**
     * Add new row to <code>tblVoucher</code> and reload text in label
     
     *
     * @param model
     * @param voucher
     */
    private void addRowTable(DefaultTableModel model, Voucher voucher) {
        double discount = voucher.getDiscount();
        model.addRow(new Object[] {
                voucher.getId(),
                voucher.getCode(),
                discount >= 1 ? Convertor.formatCurrency((int) discount) : (discount * 100) + "%",
                voucher.isIsUsed() ? "Đã dùng" : "Chưa dùng",
                Convertor.dateToString(voucher.getExpiry())
        });

        int maxDiscount = (int) (discount < 1 ? voucher.getMaxDiscount() : discount);

        total++;
        totalUsed += voucher.isIsUsed() ? 1 : 0;
        totalValue += maxDiscount;
        totalValueUsed += voucher.isIsUsed() ? maxDiscount : 0;

        lblTotal.setText(total + "");
        lblUsed.setText(totalUsed + "");
        lblTotalValue.setText(Convertor.formatCurrency(totalValue) + " VND");
        lblTotalValueUsed.setText(Convertor.formatCurrency(totalValueUsed) + " VND");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnFormVoucher = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnNew = new eco.app.myswing.ButtonRandius();
        btnInsert = new eco.app.myswing.ButtonRandius();
        btnUpdate = new eco.app.myswing.ButtonRandius();
        btnDelete = new eco.app.myswing.ButtonRandius();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCode = new eco.app.myswing.TextFieldCustom();
        jPanel14 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtDiscount = new eco.app.myswing.TextFieldCustom();
        jPanel21 = new javax.swing.JPanel();
        cbByVND = new eco.app.myswing.CheckBoxCustom();
        cbByPercent = new eco.app.myswing.CheckBoxCustom();
        jPanel12 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtMaxDiscount = new eco.app.myswing.TextFieldCustom();
        jPanel13 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtMinApply = new eco.app.myswing.TextFieldCustom();
        jPanel15 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtExpiry = new eco.app.myswing.TextFieldCustom();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtClone = new eco.app.myswing.TextFieldCustom();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        pnListVoucher = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtFind = new eco.app.myswing.TextFieldCustom();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonRandius2 = new eco.app.myswing.ButtonRandius();
        jPanel6 = new javax.swing.JPanel();
        cbByID = new eco.app.myswing.CheckBoxCustom();
        cbByCode = new eco.app.myswing.CheckBoxCustom();
        spTable = new javax.swing.JScrollPane();
        tblVoucher = new eco.app.myswing.TableCustom();
        jPanel1 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblTotalValue = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblUsed = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblTotalValueUsed = new javax.swing.JLabel();
        btngDiscountBy = new javax.swing.ButtonGroup();
        btngFind = new javax.swing.ButtonGroup();
        pm = new javax.swing.JPopupMenu();
        mniDelete = new javax.swing.JMenuItem();

        pnFormVoucher.setBackground(SaveData.BG_CONTENT);
        pnFormVoucher.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Roboto", 3, 30)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("List Voucher");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 1, 5, 1));
        pnFormVoucher.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 15, 5));
        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnNew.setText("New");
        btnNew.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel7.add(btnNew);

        btnInsert.setBackground(SaveData.BTN_SUCCESS);
        btnInsert.setText("Insert");
        btnInsert.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });
        jPanel7.add(btnInsert);

        btnUpdate.setBackground(SaveData.BTN_WARNING);
        btnUpdate.setText("Update");
        btnUpdate.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel7.add(btnUpdate);

        btnDelete.setBackground(SaveData.BTN_DANGER);
        btnDelete.setText("Delete");
        btnDelete.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jPanel7.add(btnDelete);

        pnFormVoucher.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        jPanel8.setOpaque(false);

        jPanel10.setOpaque(false);
        jPanel10.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel11.setOpaque(false);
        jPanel11.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel4.setText("Code");
        jLabel4.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel4.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel11.add(jLabel4, java.awt.BorderLayout.LINE_START);

        txtCode.setCanEmpty(false);
        txtCode.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtCode.setRegex("[A-Za-z0-9]+");
        txtCode.setValidateAction(ShareData.validateAction);
        jPanel11.add(txtCode, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel11);

        jPanel14.setOpaque(false);
        jPanel14.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel7.setText("Discount");
        jLabel7.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel7.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel14.add(jLabel7, java.awt.BorderLayout.LINE_START);

        txtDiscount.setCanEmpty(false);
        txtDiscount.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtDiscount.setRegex("[0-9]+[.][0-9]{1,2}|[0-9]+|[0-9]{1,2}%");
        txtDiscount.setValidateAction(ShareData.validateAction);
        txtDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountFocusLost(evt);
            }
        });
        txtDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiscountActionPerformed(evt);
            }
        });
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });
        jPanel14.add(txtDiscount, java.awt.BorderLayout.CENTER);

        jPanel21.setOpaque(false);
        jPanel21.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btngDiscountBy.add(cbByVND);
        cbByVND.setText("By VND");
        cbByVND.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jPanel21.add(cbByVND);

        btngDiscountBy.add(cbByPercent);
        cbByPercent.setSelected(true);
        cbByPercent.setText("By %");
        cbByPercent.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jPanel21.add(cbByPercent);

        jPanel14.add(jPanel21, java.awt.BorderLayout.LINE_END);

        jPanel10.add(jPanel14);

        jPanel12.setOpaque(false);
        jPanel12.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel5.setText("Max discount");
        jLabel5.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel5.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel12.add(jLabel5, java.awt.BorderLayout.LINE_START);

        txtMaxDiscount.setText("0");
        txtMaxDiscount.setCanEmpty(false);
        txtMaxDiscount.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtMaxDiscount.setRegex("[0-9]+");
        txtMaxDiscount.setValidateAction(ShareData.validateAction);
        txtMaxDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMaxDiscountFocusGained(evt);
            }
        });
        jPanel12.add(txtMaxDiscount, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel12);

        jPanel13.setOpaque(false);
        jPanel13.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel6.setText("Min apply");
        jLabel6.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel6.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel13.add(jLabel6, java.awt.BorderLayout.LINE_START);

        txtMinApply.setText("0");
        txtMinApply.setCanEmpty(false);
        txtMinApply.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtMinApply.setRegex("[0-9]+");
        txtMinApply.setValidateAction(ShareData.validateAction);
        jPanel13.add(txtMinApply, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel13);

        jPanel15.setOpaque(false);
        jPanel15.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel8.setText("Expiry date");
        jLabel8.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel8.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel15.add(jLabel8, java.awt.BorderLayout.LINE_START);

        txtExpiry.setText(Convertor.todayString());
        txtExpiry.setCanEmpty(false);
        txtExpiry.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtExpiry.setRegex(
                "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
        txtExpiry.setValidateAction(ShareData.validateAction);
        jPanel15.add(txtExpiry, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel15);

        jPanel16.setOpaque(false);
        jPanel16.setLayout(new java.awt.BorderLayout(10, 0));

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel9.setText("Clone");
        jLabel9.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel9.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel9.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel16.add(jLabel9, java.awt.BorderLayout.LINE_START);

        txtClone.setText("1");
        txtClone.setCanEmpty(false);
        txtClone.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtClone.setRegex("[0-9]+");
        txtClone.setValidateAction(ShareData.validateAction);
        jPanel16.add(txtClone, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel16);

        jPanel9.setOpaque(false);
        jPanel9.setLayout(new java.awt.BorderLayout(10, 0));

        txtDescription.setColumns(20);
        txtDescription.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        txtDescription.setRows(5);
        jScrollPane2.setViewportView(txtDescription);

        jPanel9.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel3.setText("Description");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 1, 1));
        jLabel3.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel3.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel9.add(jLabel3, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE));
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 341,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)));

        pnFormVoucher.add(jPanel8, java.awt.BorderLayout.CENTER);

        pnListVoucher.setBackground(SaveData.BG_CONTENT);
        pnListVoucher.setLayout(new java.awt.BorderLayout(0, 5));

        jLabel1.setFont(new java.awt.Font("Roboto", 3, 30)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("List Voucher");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 1, 5, 1));
        pnListVoucher.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 5));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 10, 5));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtFind.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jPanel3.add(txtFind, java.awt.BorderLayout.CENTER);

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        buttonRandius2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eco/app/icon/icons8_sync_25px.png"))); // NOI18N
        buttonRandius2.setText("Reload");
        buttonRandius2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRandius2ActionPerformed(evt);
            }
        });
        jPanel5.add(buttonRandius2, java.awt.BorderLayout.LINE_END);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        btngFind.add(cbByID);
        cbByID.setText("ID");
        cbByID.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbByID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbByIDActionPerformed(evt);
            }
        });
        jPanel6.add(cbByID);

        btngFind.add(cbByCode);
        cbByCode.setSelected(true);
        cbByCode.setText("Code");
        cbByCode.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        cbByCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbByCodeActionPerformed(evt);
            }
        });
        jPanel6.add(cbByCode);

        jPanel5.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        tblVoucher.setAutoCreateRowSorter(true);
        tblVoucher.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "Code", "Discount", "Used", "Expiry"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblVoucher.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVoucherMouseClicked(evt);
            }
        });
        spTable.setViewportView(tblVoucher);

        jPanel2.add(spTable, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 15, 15));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(2, 2, 15, 5));

        jPanel17.setOpaque(false);
        jPanel17.setLayout(new java.awt.BorderLayout());

        jLabel10.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel10.setText("Total");
        jPanel17.add(jLabel10, java.awt.BorderLayout.LINE_START);

        lblTotal.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("100");
        jPanel17.add(lblTotal, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel17);

        jPanel19.setOpaque(false);
        jPanel19.setLayout(new java.awt.BorderLayout());

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel14.setText("Total value");
        jPanel19.add(jLabel14, java.awt.BorderLayout.LINE_START);

        lblTotalValue.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        lblTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalValue.setText("100 VND");
        jPanel19.add(lblTotalValue, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel19);

        jPanel18.setOpaque(false);
        jPanel18.setLayout(new java.awt.BorderLayout());

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel12.setText("Used");
        jPanel18.add(jLabel12, java.awt.BorderLayout.LINE_START);

        lblUsed.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        lblUsed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUsed.setText("100");
        jPanel18.add(lblUsed, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel18);

        jPanel20.setOpaque(false);
        jPanel20.setLayout(new java.awt.BorderLayout());

        jLabel16.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel16.setText("Total value used");
        jPanel20.add(jLabel16, java.awt.BorderLayout.LINE_START);

        lblTotalValueUsed.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        lblTotalValueUsed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalValueUsed.setText("50VND");
        jPanel20.add(lblTotalValueUsed, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel20);

        jPanel2.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pnListVoucher.add(jPanel2, java.awt.BorderLayout.CENTER);

        pm.setBackground(new java.awt.Color(255, 204, 204));
        pm.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        pm.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 7, 3, 7));

        mniDelete.setBackground(new java.awt.Color(255, 204, 204));
        mniDelete.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        mniDelete.setText("Delete all");
        mniDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniDeleteActionPerformed(evt);
            }
        });
        pm.add(mniDelete);

        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 509, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 367, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRandius2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonRandius2ActionPerformed
        fillTable();
    }// GEN-LAST:event_buttonRandius2ActionPerformed

    private void txtDiscountActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtDiscountActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtDiscountActionPerformed

    private void tblVoucherMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblVoucherMouseClicked

        if ((evt.getClickCount() == 2) && (SwingUtilities.isLeftMouseButton(evt))) {
            nav.isEditMode();

            int index = tblVoucher.getSelectedRow();
            idVoucherSelected = (int) tblVoucher.getValueAt(index, 0);

            Voucher voucher = (Voucher) EntityHelper.find(vouchers, idVoucherSelected);

            if (voucher != null) {
                showForm(voucher);
            }
        } else if (SwingUtilities.isRightMouseButton(evt)) {
            pm.show(tblVoucher, evt.getX(), evt.getY());
        }

    }// GEN-LAST:event_tblVoucherMouseClicked

    private void txtMaxDiscountFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtMaxDiscountFocusGained
        if (cbByVND.isSelected()) {
            txtMaxDiscount.setText(txtDiscount.getText());
        }
    }// GEN-LAST:event_txtMaxDiscountFocusGained

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNewActionPerformed
        clearForm();
    }// GEN-LAST:event_btnNewActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnInsertActionPerformed
        StringBuilder sb = new StringBuilder();
        Voucher voucher = readFrom(sb);
        if (!sb.isEmpty()) {
            MessageHelper.showMessage(this, sb.toString());
            return;
        }

        try {
            VoucherDao dao = new VoucherDao();

            if (dao.isExist(voucher.getCode())) {
                MessageHelper.showErrorMessage(this, "This voucher's code has already used. Please try orther code !");
                return;
            }

            int clone = 1;
            try {
                clone = Integer.parseInt(txtClone.getText());
            } catch (Exception e) {
            }

            int counter = 0;

            for (int i = 0; i < clone; i++) {
                counter += dao.insert(voucher) ? 1 : 0;
            }

            // Thêm mới 10 thành công 1 thất bại
            MessageHelper.showMessage(this, "Thêm mới " + counter + " thành công " + (clone - counter) + " thất bại");

        } catch (Exception e) {
            MessageHelper.showException(this, e);
        }

        fillTable();

    }// GEN-LAST:event_btnInsertActionPerformed

    private void cbByIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbByIDActionPerformed
        findVoucher();
    }// GEN-LAST:event_cbByIDActionPerformed

    private void cbByCodeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbByCodeActionPerformed
        findVoucher();
    }// GEN-LAST:event_cbByCodeActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateActionPerformed
        updateVoucher();
    }// GEN-LAST:event_btnUpdateActionPerformed

    private void txtDiscountKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtDiscountKeyReleased
        if (cbByVND.isSelected()) {
            txtMaxDiscount.setText(txtDiscount.getText());
        }
    }// GEN-LAST:event_txtDiscountKeyReleased

    private void txtDiscountFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtDiscountFocusLost
        String text = txtDiscount.getText();

        if (text.matches("[0-9]+")) {
            cbByVND.setSelected(true);
            txtMaxDiscount.setText(txtDiscount.getText());
        } else if (text.matches("0.[0-9]{1,2}")) {
            cbByPercent.setSelected(true);
            txtMaxDiscount.setText("");
        } else if (text.matches("(100(\\.0{1,2})?|[1-9]?\\d(\\.\\d{1,2})?)%")) {
            text = text.replace("%", "");
            try {
                txtDiscount.setText((Double.parseDouble(text) / 100) + "");
            } catch (Exception e) {
            }
            cbByPercent.setSelected(true);
        }

    }// GEN-LAST:event_txtDiscountFocusLost

    private void mniDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mniDeleteActionPerformed
        int[] indexes = tblVoucher.getSelectedRows();

        if (indexes.length == 0) {
            MessageHelper.showMessage(this, "Please select a row to delete !");
            return;
        }
        if (MessageHelper.showConfirm(this, "Are you sure delete ?") == MessageType.YES) {
            try {
                VoucherDao dao = new VoucherDao();
                int counter = 0;
                for (int i = 0; i < indexes.length; i++) {
                    int id = (int) tblVoucher.getValueAt(indexes[i], 0);
                    Voucher voucher = (Voucher) EntityHelper.find(vouchers, id);
                    if (!voucher.isIsUsed()) {
                        counter += dao.delete(voucher) ? 1 : 0;
                    }

                }
                MessageHelper.showMessage(this,
                        "Delete " + counter + " success " + (indexes.length - counter) + " fail");
            } catch (Exception e) {
                MessageHelper.showException(this, e);
            }
        }

        fillTable();

    }// GEN-LAST:event_mniDeleteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private eco.app.myswing.ButtonRandius btnDelete;
    private eco.app.myswing.ButtonRandius btnInsert;
    private eco.app.myswing.ButtonRandius btnNew;
    private eco.app.myswing.ButtonRandius btnUpdate;
    private javax.swing.ButtonGroup btngDiscountBy;
    private javax.swing.ButtonGroup btngFind;
    private eco.app.myswing.ButtonRandius buttonRandius2;
    private eco.app.myswing.CheckBoxCustom cbByCode;
    private eco.app.myswing.CheckBoxCustom cbByID;
    private eco.app.myswing.CheckBoxCustom cbByPercent;
    private eco.app.myswing.CheckBoxCustom cbByVND;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalValue;
    private javax.swing.JLabel lblTotalValueUsed;
    private javax.swing.JLabel lblUsed;
    private javax.swing.JMenuItem mniDelete;
    private javax.swing.JPopupMenu pm;
    private javax.swing.JPanel pnFormVoucher;
    private javax.swing.JPanel pnListVoucher;
    private javax.swing.JScrollPane spTable;
    private eco.app.myswing.TableCustom tblVoucher;
    private eco.app.myswing.TextFieldCustom txtClone;
    private eco.app.myswing.TextFieldCustom txtCode;
    private javax.swing.JTextArea txtDescription;
    private eco.app.myswing.TextFieldCustom txtDiscount;
    private eco.app.myswing.TextFieldCustom txtExpiry;
    private eco.app.myswing.TextFieldCustom txtFind;
    private eco.app.myswing.TextFieldCustom txtMaxDiscount;
    private eco.app.myswing.TextFieldCustom txtMinApply;
    // End of variables declaration//GEN-END:variables
}
