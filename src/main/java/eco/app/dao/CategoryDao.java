
package eco.app.dao;

import eco.app.entity.Category;
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
public class CategoryDao extends EntityDao {

    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }
        if (!(e instanceof Category)) {
            throw new Exception("Entity is not Category");
        }
    }

    private List<Category> readResultSet(ResultSet rs) throws Exception {
        List<Category> list = new ArrayList<>();
        while (rs.next()) {
            Category c = new Category();
            c.readResultSet(rs);
            list.add(c);
        }
        return list;
    }

    public List<Category> getAll() throws Exception {
        String sql = "SELECT * FROM Category";
        ResultSet rs = DatabaseHelper.excuteQuery(sql);
        return readResultSet(rs);
    }

    public Category getById(int id) throws Exception {
        String sql = "SELECT * FROM Category WHERE id = ?";
        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);
        List<Category> list = readResultSet(rs);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public boolean update(Entity e) throws Exception {

        validate(e);

        String sql = "UPDATE [dbo].[Category]"
                + " SET "
                + " name = ?,"
                + " image = ?"
                + " WHERE id = ?";
        Object[] obj = EntityHelper.getData(e,
                "name",
                "image",
                "id");
        return DatabaseHelper.excuteUpdate(sql, obj);

    }

    @Override
    public boolean delete(Entity e) throws Exception {
        validate(e);
        String sql = "DELETE FROM [dbo].[Category] WHERE id = ?";
        Object[] obj = EntityHelper.getData(e, "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean insert(Entity e) throws Exception {
        validate(e);
        String sql = "INSERT INTO [dbo].[Category] (name, image)"
                + " VALUES (?,?)";
        Object[] obj = EntityHelper.getData(e,
                "name",
                "image");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

}
