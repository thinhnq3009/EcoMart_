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
public class Employee extends Entity {

    protected String username;
    protected String password;
    protected String fullname;
    protected boolean gender;
    protected String email;
    protected String phone;
    protected String address;
    protected int salary;
    protected int soldOut;
    protected String note;
    protected boolean isAdministrator;

    public Employee() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(int soldOut) {
        this.soldOut = soldOut;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isIsAdministrator() {
        return isAdministrator;
    }

    public void setIsAdministrator(boolean isAdministrator) {
        this.isAdministrator = isAdministrator;
    }

    @Override
    public void readResultSet(ResultSet rs) throws Exception {

        if (rs == null) {
            throw new IllegalAccessException("ResultSet is null");
        }

        this.id = rs.getInt("id");
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.fullname = rs.getString("fullname");
        this.gender = rs.getBoolean("gender");
        this.email = rs.getString("email");
        this.phone = rs.getString("phone");
        this.address = rs.getString("address");
        this.salary = rs.getInt("salary");
        this.note = rs.getString("note");
        this.soldOut = rs.getInt("sold_out");
        this.isAdministrator = rs.getBoolean("is_administrator");

    }

}
