package eco.app.dao;

import eco.app.entity.Brand;
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
public class BrandDao extends EntityDao {

    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }
        if (!(e instanceof Brand)) {
            throw new Exception("Entity is not Brand");
        }
    }

    private List<Brand> readResultSet(ResultSet rs) throws Exception {
        List<Brand> brands = new ArrayList<>();

        while (rs.next()) {
            Brand brand = new Brand();

            brand.readResultSet(rs);

            brands.add(brand);
        }

        return brands;
    }

    public List<Brand> getAll() throws Exception {
        String sql = "SELECT * FROM Brand";

        ResultSet rs = DatabaseHelper.excuteQuery(sql);

        return readResultSet(rs);
    }
    
    public List<Brand> findById(int id) throws Exception {
        String sql = "SELECT * FROM Brand WHERE id = ? ";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);

        return readResultSet(rs);
    }
    

    @Override
    public boolean update(Entity e) throws Exception {
        validate(e);
        String sql = "UPDATE [dbo].[Brand]"
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
        String sql = "DELETE FROM [dbo].[Brand] WHERE id = ?";
        Object[] obj = EntityHelper.getData(e, "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean insert(Entity e) throws Exception {
        validate(e);
        String sql = "INSERT INTO [dbo].[Brand] (name, image)"
                + " VALUES (?,?)";
        Object[] obj = EntityHelper.getData(e,
                "name",
                "image");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

}
