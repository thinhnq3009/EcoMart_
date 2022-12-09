package eco.app.dao;

import eco.app.entity.Entity;
import eco.app.entity.EntityHelper;
import eco.app.entity.OrderDetail;
import eco.app.helper.DatabaseHelper;

/**
 *
 * @author ThinhNQ
 */
public class OrderDetailDao extends EntityDao {

    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }
        if (!(e instanceof OrderDetail)) {
            throw new Exception("Entity is not OrderDetail");
        }
    }

    @Override
    public boolean update(Entity e) throws Exception {

        String sql = "UPDATE [dbo].[Order_Detail]"
                + " SET "
                + " order_id = ?,"
                + " product_id = ?,"
                + " quantity = ?,"
                + " note = ?"
                + " WHERE id = ?";
        Object[] obj = EntityHelper.getData(e,
                "orderId",
                "productId",
                "quantity",
                "note",
                "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean delete(Entity e) throws Exception {

        String sql = "DELETE FROM [dbo].[Order_Detail] WHERE id = ?";
        Object[] obj = EntityHelper.getData(e, "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean insert(Entity e) throws Exception {
        String sql = "INSERT INTO [dbo].[Order_Detail] (order_id, product_id, quantity)"
                + " VALUES (?,?,?)";
        Object[] obj = EntityHelper.getData(e,
                "orderId",
                "productId",
                "quantity");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

   
    
}
