package eco.app.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import eco.app.entity.Product.BillItem;
import eco.app.helper.DatabaseHelper;

/**
 *
 * @author ThinhNQ
 */
public class BillItemDao {

    public List<BillItem> getBillItemsByIdOrder(int orderId) throws Exception {

        List<BillItem> billItems = new ArrayList<>();

        String sql = "SELECT * FROM v_bill_item WHERE order_id = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, orderId);

        while (rs.next()) {
            BillItem billItem = new BillItem();

            billItem.readResultSet(rs);

            billItems.add(billItem);
        }

        return billItems;
    }
}
