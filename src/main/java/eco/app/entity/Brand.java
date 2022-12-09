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
public class Brand extends Entity{

    protected String name;
    protected byte[] image;

    public Brand() {
    }

  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    
    
    @Override
    public void readResultSet(ResultSet rs) throws Exception {

        /*
         * id
         * name
         * image
         */

        if (rs == null) {
            throw new IllegalAccessException("ResultSet is null");
        }
        this.id = rs.getInt("id");
        this.name = rs.getString("name");
        this.image = rs.getBytes("image");

    }
}
