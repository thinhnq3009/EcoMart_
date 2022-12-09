package eco.app.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import eco.app.entity.Customer;
import eco.app.entity.Entity;
import eco.app.entity.EntityHelper;
import eco.app.entity.Order;
import eco.app.entity.OrderDetail;
import eco.app.entity.Product.BillItem;
import eco.app.entity.Voucher;
import eco.app.helper.DatabaseHelper;
import eco.app.helper.ShareData;

/**
 *
 * @author ThinhNQ
 */
public class OrderDao extends EntityDao {

    private int idNewOrder = 0;

    private List<Order> readResultSet(ResultSet rs) throws Exception {
        List<Order> orders = new ArrayList<>();

        while (rs.next()) {
            Order order = new Order();

            order.readResultSet(rs);

            orders.add(order);
        }

        return orders;
    }



    @Override
    public boolean update(Entity e) throws Exception {
        throw new Exception("Can't update order !");
//        validate(e);
//        String sql = "UPDATE [dbo].[Order]"
//                + " SET "
//                + " employee_id = ?,"
//                + " voucher_id = ?,"
//                + " customer_id = ?,"
//                + " time_create = ?"
//                + " WHERE id = ?";
//        Object[] obj = EntityHelper.getData(e,
//                "employeeId",
//                "voucherId",
//                "customerId",
//                "timeCreate",
//                "id");
//
//        return DatabaseHelper.excuteUpdate(sql, obj);

    }

    public Order findById(int id) throws Exception {
        String sql = "SELECT * FROM v_order_quantity_total WHERE id = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);

        if (!rs.next()) {
            return null;
        }

        Order order = new Order();

        order.readResultSet(rs);

        return order;

    }

    public List<Order> getAll() throws Exception {
        String sql = "SELECT * FROM v_order_quantity_total";

        ResultSet rs = DatabaseHelper.excuteQuery(sql);

        return readResultSet(rs);
    }

    @Override
    public boolean delete(Entity e) throws Exception {
        validate(e);
        
        
        String sql = "EXEC dbo.p_delete_order @id = ?";
        Object[] obj = EntityHelper.getData(e, "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean insert(Entity e) throws Exception {
        validate(e);
        String sql = "EXEC dbo.p_insert_order  ?, ?, ?, ?";
        Object[] obj = EntityHelper.getData(e,
                "employeeId",
                "voucherId",
                "customerId",
                "discount");

        obj[1] = (int) obj[1] == 0 ? null : obj[1];
        obj[2] = (int) obj[2] == 0 ? null : obj[2];

        ResultSet rs = DatabaseHelper.excuteQuery(sql, obj);

        if (rs.next()) {
            idNewOrder = rs.getInt("id");
            return idNewOrder != 0;
        } else {
            return false;
        }

    }

    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }

        if (!(e instanceof Order)) {
            throw new Exception("Entity is not Order");
        }
    }

    private void destroyNewOrder() throws Exception {
        if (idNewOrder == 0) {
            return;
        }
        Order order = new Order();
        order.setId(idNewOrder);
        delete(order);
        System.out.println("Xoá " + idNewOrder);
    }

    public Order createOrder(Customer customer, Voucher voucher, List<BillItem> items, int discount) throws Exception {
        boolean isSuccess = true;
        try {

            Order order = new Order();

            order.setEmployeeId(ShareData.USER_LOGIN.getId());
            if (customer != null) {
                order.setCustomerId(customer.getId());
            }

            if (voucher != null) {
                order.setVoucherId(voucher.getId());
                new VoucherDao().usedVoucher(voucher, order, customer);
            }
            
            order.setDiscount(discount);

            if (insert(order)) {

                System.out.println("Tạo hoá đơn thành công " + idNewOrder);

                OrderDetailDao dao = new OrderDetailDao();

                for (BillItem item : items) {
                    OrderDetail detail = new OrderDetail();

                    detail.setOrderId(idNewOrder);
                    detail.setProductId(item.getProduct().getId());
                    detail.setQuantity(item.getQuantity());

                    
                    
                    if (!dao.insert(detail)) {
                        isSuccess = false;
                        break;
                    }
                }
            } else {
                System.out.println("Tạo hoá đơn thất bại");
                isSuccess = false;
            }

        } catch (Exception e) {
            isSuccess = false;
            throw e;
        } finally {
            if (!isSuccess) {
                try {
                    destroyNewOrder();
                } catch (Exception ex) {
                    throw new Exception("Can't destroy new order");
                }
            }
        }

        if (isSuccess) {
            return findById(idNewOrder);
        }
        return null;

    }
    
    
   

}
