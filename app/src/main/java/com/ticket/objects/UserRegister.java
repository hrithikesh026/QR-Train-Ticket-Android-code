package com.ticket.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserRegister {
    private String _id;
    private String email,first_name,last_name,password, dob;
    private int wallet_balance;
    public UserRegister(String email, String firstName, String lastName,String password,String dob){
        this.email = email;
        this.first_name = firstName;
        this.last_name =lastName;
        this .password =password;
        this.dob = dob;
        this.wallet_balance = 0;
    }
    public String getJsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public static UserRegister getObject(String json){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        System.out.println("This is malformed String:" +json);
        return gson.fromJson(json,UserRegister.class);
    }
    public String get_id(){
        return this._id;
    }
    public String getEmail(){
        return this.email;
    }
    public String getFirst_name(){
        return this.first_name;
    }
    public String getLast_name(){
        return this.last_name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getDob(){
        return this.dob;
    }
    public int getWallet_balance(){
        return this.wallet_balance;
    }

    public void setWallet_balance(int wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
