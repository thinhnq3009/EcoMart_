/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eco.app.entity;

import java.sql.ResultSet;

/**
 *
 * @author Lenovo
 */
public class OrderDetail extends Entity {

    
    protected int orderId;
    protected int productId;
    protected int quantity;

    public OrderDetail() {
    }

  

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void readResultSet(ResultSet rs) throws Exception {
        /*
         * id
         * orderId
         * productId
         * quantity
         * note
         */

        if (rs == null) {
            throw new IllegalAccessException("ResultSet is null");
        }

        this.id = rs.getInt("id");
        this.orderId = rs.getInt("order_id");
        this.productId = rs.getInt("product_id");
        this.quantity = rs.getInt("quantity");
    }

}
