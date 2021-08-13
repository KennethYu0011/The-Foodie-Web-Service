package com.example.bootiful.model;

import java.util.ArrayList;

public class WebMessage extends ResponseMessage{
    //private String scope;
    private ArrayList<Restaurant> restaurants;

    public WebMessage (String content){
        //this.scope = content;
        this.restaurants = new ArrayList<Restaurant>();
    }

    //public String getScope() {return scope;}

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void add_restaurant(Restaurant r){
        restaurants.add(r);
    }
}
