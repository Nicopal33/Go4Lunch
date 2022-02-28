package com.example.goforlunch.model.restaurants;

public class Restaurant {

    String id;
    String name;
    String address;

    private boolean select = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSelect() {return select;}

    public void setSelect(boolean select) {this.select=select;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Restaurant (String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }



}
