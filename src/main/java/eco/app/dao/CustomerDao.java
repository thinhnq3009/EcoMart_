package eco.app.dao;

import eco.app.entity.Customer;
import eco.app.entity.Employee;
import eco.app.entity.Entity;
import eco.app.entity.EntityHelper;
import eco.app.helper.DatabaseHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ThinhNQ
 */
public class CustomerDao extends EntityDao {

    private List<Customer> readResultSet(ResultSet rs) throws Exception {
        List<Customer> customers = new ArrayList<>();

        while (rs.next()) {
            Customer customer = new Customer();

            customer.readResultSet(rs);

            customers.add(customer);
        }

        return customers;
    }

    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }

        if (!(e instanceof Customer)) {
            throw new Exception("Entity is not Customer");
        }
    }

    public List<Customer> getAll() throws Exception {

        String sql = "SELECT * FROM v_customer_spent_discount";

        ResultSet rs = DatabaseHelper.excuteQuery(sql);

        return readResultSet(rs);

    }

    public List<Customer> findById(int id) throws Exception {

        String sql = "SELECT * FROM v_customer_spent_discount WHERE id = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);

        return readResultSet(rs);

    }

    public List<Customer> findByName(String fullname) throws Exception {

        String sql = "SELECT * FROM v_customer_spent_discount WHERE fullname = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, fullname);

        return readResultSet(rs);

    }

    public List<Customer> find(String key) throws Exception {

        String sql = "SELECT * FROM v_customer_spent_discount WHERE fullname = ? OR id = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, key, key);

        return readResultSet(rs);

    }

    @Override
    public boolean update(Entity e) throws Exception {

        validate(e);

        String sql = "UPDATE [dbo].[Customer]"
                + " SET fullname = ?"
                + " ,gender = ?"
                + " ,email = ?"
                + " ,phone = ?"
                + " ,address = ?"
                + " ,note = ?"
                + " WHERE id = ?";
        Object[] obj = EntityHelper.getData(e,
                "fullname",
                "gender",
                "email",
                "phone",
                "address",
                "note",
                "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean delete(Entity e) throws Exception {

        validate(e);

        String sql = "DELETE FROM Customer WHERE id = ?";
        Object[] obj = EntityHelper.getData(e, "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean insert(Entity e) throws Exception {

        validate(e);

        String sql = "INSERT INTO Customer (fullname, gender, email, phone, address, coin, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] obj = EntityHelper.getData(e,
                "fullname",
                "gender",
                "email",
                "phone",
                "address",
                "coin",
                "note");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    public void appendCoin(Customer customer, int coin) throws Exception {
        String sql = "EXEC P_append_coin_customer ?, ?";

        boolean a = DatabaseHelper.excuteUpdate(sql, customer.getId(), coin);
        System.out.println(customer.getId() + " " + coin);
        System.out.println("Thêm coin " + (a ? "thành công" : "thất bại"));
    }

//    public void getSpent(Customer customer) throws Exception {
//
//        int id = customer.getId();
//
//        String sql = "EXEC getCustomerSpent ?";
//
//        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);
//
//        if (rs.next()) {
//            int spent = rs.getInt("totalSpent");
//            int discount = rs.getInt("totalDiscount");
//
//            customer.setSpent(spent - discount);
//        }
//
//    }
}
