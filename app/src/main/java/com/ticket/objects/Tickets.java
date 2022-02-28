package com.ticket.objects;

import com.google.gson.Gson;

public class Tickets {
    private  String  source,email,destination,date,train_name,tclass,price;
    int train_no;
    public Tickets(String email,String source,String destination,String date,String train_name, int train_no,String tclass,String price){
        this.date = date;
        this.destination = destination;
        this.email =email;
        this.price =price;
        this.train_name =train_name;
        this.train_no = train_no;
        this.tclass = tclass;
        this.source = source;
    }
    public String getJsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public static Tickets getObject(String json){
        Gson gson = new Gson();
        System.out.println("This is the json String:::::::::::"+json);
        return gson.fromJson(json,Tickets.class);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTclass() {
        return tclass;
    }

    public void setTclass(String tclass) {
        this.tclass = tclass;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTrain_no() {
        return train_no;
    }

    public void setTrain_no(int train_no) {
        this.train_no = train_no;
    }
}
