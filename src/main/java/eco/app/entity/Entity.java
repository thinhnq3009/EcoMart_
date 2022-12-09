/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package eco.app.entity;

import java.sql.ResultSet;

/**
 *
 * @author Lenovo
 */
public abstract class Entity {

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void readResultSet(ResultSet rs) throws Exception;

    @Override
    public String toString() {
        return  this.getClass().getName() + " " + getId();
    }

    
    
}
