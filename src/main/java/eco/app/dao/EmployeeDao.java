/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.dao;

import eco.app.entity.Employee;
import eco.app.entity.Entity;
import eco.app.entity.EntityHelper;
import eco.app.helper.DatabaseHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class EmployeeDao extends EntityDao {

    private List<Employee> readResultSet(ResultSet rs) throws Exception {
        List<Employee> employees = new ArrayList<>();

        while (rs.next()) {
            Employee employee = new Employee();

            employee.readResultSet(rs);

            employees.add(employee);
        }

        return employees;
    }

    @Override
    protected void validate(Entity e) throws Exception {
        if (e == null) {
            throw new Exception("Entity is null");
        }

        if (!(e instanceof Employee)) {
            throw new Exception("Entity is not Employee");
        }
    }

    public List<Employee> getAll() throws Exception {
        String sql = "SELECT * FROM Employee";

        ResultSet rs = DatabaseHelper.excuteQuery(sql);

        return readResultSet(rs);
    }

    public List<Employee> findById(int id) throws Exception {
        String sql = "SELECT * FROM Employee WHERE id = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, id);

        return readResultSet(rs);
    }
    

    public Employee findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM Employee WHERE username = ?";

        ResultSet rs = DatabaseHelper.excuteQuery(sql, username);

        List<Employee> result = readResultSet(rs);
        
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public boolean update(Entity e) throws Exception {

        validate(e);

        String sql = "UPDATE Employee"
                + " SET "
                + " username = ?,"
                + " password = ?,"
                + " fullname = ?,"
                + " gender = ?,"
                + " email = ?,"
                + " phone = ?,"
                + " address = ?,"
                + " salary = ?,"
                + " sold_out = ?"
                + " WHERE id = ?";
        Object[] obj = EntityHelper.getData(e,
                "username",
                "password",
                "fullname",
                "gender",
                "email",
                "phone",
                "address",
                "salary",
                "soldOut",
                "id");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

    @Override
    public boolean delete(Entity e) throws Exception {

        validate(e);

        String sql = "DELETE FROM Employee WHERE id = ?";

        Object[] obj = EntityHelper.getData(e, "id");

        return DatabaseHelper.excuteUpdate(sql, obj);

    }

    @Override
    public boolean insert(Entity e) throws Exception {

        validate(e);

        String sql = "INSERT INTO Employee"
                + " (username,"
                + " password,"
                + " fullname,"
                + " gender,"
                + " email,"
                + " phone,"
                + " address,"
                + " salary,"
                + " sold_out, note)"
                + " VALUES"
                + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] obj = EntityHelper.getData(e,
                "username",
                "password",
                "fullname",
                "gender",
                "email",
                "phone",
                "address",
                "salary",
                "soldOut", 
                "note");
        return DatabaseHelper.excuteUpdate(sql, obj);
    }

}
