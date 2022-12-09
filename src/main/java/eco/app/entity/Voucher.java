/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.entity;

import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author Lenovo
 */
public class Voucher extends Entity {

    protected int employeeId;
    protected String code;
    protected int maxDiscount;
    protected int minApply;
    protected double discount;
    protected String description;
    protected boolean isUsed;
    protected Date expiry;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getMinApply() {
        return minApply;
    }

    public void setMinApply(int minApply) {
        this.minApply = minApply;
    }

    public double getDiscount() {
        return discount ;
    }

    public double getDiscount(int price) {
        if (price >= minApply) {
            if (discount < 1) {
                int tempDiscount = (int) (price * this.discount);
                return tempDiscount > maxDiscount ? maxDiscount : tempDiscount;
            } else {
                return discount;
            }
        }
        return 0;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    @Override
    public void readResultSet(ResultSet rs) throws Exception {
        /*
         * manage_id
         * code
         * max_discount
         * min_apply
         * discount
         * description
         * is_used
         * expiry
         */
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet is null");
        }
        
        this.setId(rs.getInt("id"));
        this.setEmployeeId(rs.getInt("employee_id"));
        this.setCode(rs.getString("code"));
        this.setMaxDiscount(rs.getInt("max_discount"));
        this.setMinApply(rs.getInt("min_apply"));
        this.setDiscount(rs.getDouble("discount"));
        this.setDescription(rs.getString("description"));
        this.setIsUsed(rs.getBoolean("is_used"));
        this.setExpiry(rs.getTimestamp("expiry"));
    }
}
