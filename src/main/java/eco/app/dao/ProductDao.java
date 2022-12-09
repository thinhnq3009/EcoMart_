package eco.app.dao;

import eco.app.entity.Entity;
import eco.app.entity.EntityHelper;
import eco.app.entity.Product;
import eco.app.helper.DatabaseHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ThinhNQ
 */
public class ProductDao extends EntityDao {

    private List<Product> readResultSet(ResultSet rs) throws Exception {
        List<Product> products = new ArrayList<>();

        while (rs.next()) {
            Product product = new Product();

            product.readResultSet(rs);

            products.add(product);
        }

        return products;
    }

    public List<Product> getAll() throws Exception {

        String sql = "SELECT * FROM v_product";

        ResultSet rs = DatabaseHelper.excuteQuery(sql);

        return readResultSet(rs);

    }

    public List<Product> findById(int id) throws Exception {

        String sql = "SELECT * FROM v_product WHERE id = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);

        return readResultSet(rs);

    }

    public List<Product> findByName(String name) throws Exception {

        String sql = "SELECT * FROM v_product WHERE name = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, name);

        return readResultSet(rs);

    }

    public List<Product> findByPrice(double price) throws Exception {

        String sql = "SELECT * FROM v_product WHERE price = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, price);

        return readResultSet(rs);

    }

    public List<Product> find(String key) throws Exception {

        String sql = "SELECT * FROM v_product WHERE id = ? OR name = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, key, key);

        return readResultSet(rs);

    }

    @Override
    public boolean update(Entity e) throws Exception {
        String sql = "UPDATE Product"
                + " SET "
                + " category_id = ?,"
                + " employee_id = ?,"
                + " brand_id = ?,"
                + " name = ?,"
                + " quantity = ?,"
                + " import_price = ?,"
                + " time_add = ?,"
                + " expiry = ?,"
                + " price = ?,"
                + " description = ?,"
                + " image = ?,"
                + " note = ?"
                + " WHERE id = ?";
        Object[] obj = EntityHelper.getData(e,
                "categoryId",
                "employeeId",
                "brandId",
                "name",
                "quantity",
                "importPrice",
                "timeAdd",
                "expiry",
                "price",
                "description",
                "image",
                "note",
                "id");
        System.out.println(getClass().getName() + obj.length);
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    /**
     * > Delete a product from the database
     * 
     * @param e The entity object that you want to delete.
     * @return A boolean value.
     */
    @Override
    public boolean delete(Entity e) throws Exception {
        String sql = "DELETE FROM Product WHERE id = ?";
        return DatabaseHelper.excuteUpdate(sql, e.getId());
    }

    /**
     * > Insert a new product into the database
     * 
     * @param e The entity object that contains the data to be inserted into the
     *          database.
     * @return A boolean value.
     */
    @Override
    public boolean insert(Entity e) throws Exception {
        String sql = "INSERT INTO Product"
                + " (category_id, employee_id, brand_id, name, quantity, import_price, time_add, expiry,price, image, description, note)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)";
        Object[] obj = EntityHelper.getData(e,
                "categoryId",
                "employeeId",
                "brandId",
                "name",
                "quantity",
                "importPrice",
                "timeAdd",
                "expiry",
                "price",
                "image",
                "description",
                "note");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    /**
     * If the entity is null or not a Product, throw an exception.
     * 
     * @param e The entity to be validated.
     */
    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }

        if (!(e instanceof Product)) {
            throw new Exception("Entity is not Product");
        }
    }

    /**
     * This function returns a list of products that were sold between two dates.
     * 
     * @param from the start date
     * @param to   the end date of the period
     * @return A list of products
     */
    public List<Product> getByDate(Date from, Date to) throws Exception {

        String sql = "EXEC dbo.p_statistic_product_by_sold_date ?, ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, from, to);

        return readResultSet(rs);
    }

}
