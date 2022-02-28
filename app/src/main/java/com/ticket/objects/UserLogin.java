package com.ticket.objects;

import com.google.gson.Gson;

public class UserLogin {
    private String email,password;
    public UserLogin(String email,String password){
        this.email = email;
        this.password =password;

    }
    public String getJsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public static UserRegister getObject(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,UserRegister.class);
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setPassword(String password){
        this.password=  password;
    }
    public String getPassword(String password){
        return this.password;
    }
}
