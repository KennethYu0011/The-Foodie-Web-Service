package com.example.bootiful.model;

import java.util.ArrayList;

public class Restaurant {
    private String name;
    private String address;
    private String cuisines;
    private double rating;

    public Restaurant(String name, String addr, double rating){
        this.name = name;
        this.address = addr;
        this.rating = rating;
    }


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisines() {
        return cuisines;
    }

    public double getRating() {
        return rating;
    }

    public void setCuisine(String s){
        cuisines = s;
    }
}
