package eco.app.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eco.app.entity.Customer;
import eco.app.entity.Entity;
import eco.app.entity.EntityHelper;
import eco.app.entity.Order;
import eco.app.entity.Voucher;
import eco.app.helper.DatabaseHelper;

/**
 *
 * @author ThinhNQ
 */
public class VoucherDao extends EntityDao {

    private List<Voucher> readResultSet(ResultSet rs) throws Exception {
        List<Voucher> vouchers = new ArrayList<>();

        while (rs.next()) {
            Voucher voucher = new Voucher();

            voucher.readResultSet(rs);

            vouchers.add(voucher);
        }

        return vouchers;
    }

    public List<Voucher> getAll() throws Exception {

        String sql = "SELECT * FROM Voucher";

        ResultSet rs = DatabaseHelper.excuteQuery(sql);

        return readResultSet(rs);

    }

    public Voucher findById(int id) throws Exception {

        String sql = "SELECT * FROM Voucher WHERE id = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);

        return readResultSet(rs).get(0);

    }

    public List<Voucher> findByCode(String code) throws Exception {

        String sql = "SELECT * FROM Voucher WHERE code = ? AND is_used = 0";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, code);

        return readResultSet(rs);

    }

    @Override
    public boolean update(Entity e) throws Exception {
        validate(e);

        String sql = "EXEC dbo.p_update_voucher ?, ?, ?, ?, ?, ?, ?, ?";
        Object[] obj = EntityHelper.getData(e,
                "id",
                "employeeId",
                "code",
                "maxDiscount",
                "minApply",
                "discount",
                "description",
                "expiry");
        System.out.println(Arrays.toString(obj));
        return DatabaseHelper.excuteUpdate(sql, obj);

    }

    @Override
    public boolean delete(Entity e) throws Exception {

        validate(e);

        String sql = "DELETE FROM Voucher WHERE id = ?";
        return DatabaseHelper.excuteUpdate(sql, e.getId());
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM Voucher WHERE id = ?";
        return DatabaseHelper.excuteUpdate(sql, id);
    }

    @Override
    public boolean insert(Entity e) throws Exception {

        validate(e);

        String sql = "INSERT INTO Voucher"
                + " (employee_id, code, max_discount, min_apply, discount, description, is_used, expiry)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] obj = EntityHelper.getData(e,
                "employeeId",
                "code",
                "maxDiscount",
                "minApply",
                "discount",
                "description",
                "isUsed",
                "expiry");

        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }

        if (!(e instanceof Voucher)) {
            throw new Exception("Entity is not Voucher");
        }
    }

    public void usedVoucher(Voucher voucher, Order order, Customer customer) throws Exception {
        String id;
        if (customer != null) {
            id = customer.getId() + "";
        } else {
            id = "Unknown";
        }
        String description = "Used for order " + order.getId() + " of customer " + id;
        String sql = "EXEC dbo.p_used_voucher ?, ?";
        DatabaseHelper.excuteUpdate(sql, voucher.getId(), description);
    }

    /**
     * if <code>code</code> is exist in database will return <code>true</code>
     * else return <code>false</code>
     *
     * @param code
     * @return
     * @throws Exception
     */
    public boolean isExist(String code) throws Exception {
        String sql = "EXEC p_check_exist_code_voucher ? ";
        ResultSet rs = DatabaseHelper.excuteQuery(sql, code);

        if (rs.next()) {
            return rs.getBoolean("value");
        }

        return false;
    }

}
