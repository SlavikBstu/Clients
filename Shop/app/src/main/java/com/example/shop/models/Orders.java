package com.example.shop.models;

/**
 * Created by Владислав on 21.05.2017.
 */

public class Orders {

    int Id;
    String Name;
    String Surname;
    String Email;
    String Address;

    public Orders(int id, String surname, String name, String email, String address) {
        Surname = surname;
        Id = id;
        Name = name;
        Email = email;
        Address = address;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
