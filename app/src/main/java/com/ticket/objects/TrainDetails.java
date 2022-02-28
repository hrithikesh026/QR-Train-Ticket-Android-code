package com.ticket.objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrainDetails {
    private String num, name, st, dt, tt;
    private String s,d;
    private List<TicketPrice> priceList ;

    public TrainDetails(String jsonString){
        try{
            priceList = new ArrayList<>();
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("ticket_price");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                this.priceList.add(new TicketPrice(obj.getString("className"),obj.getString("price")));
            }
            JSONObject jsonObject = new JSONObject(jsonString);
            this.name = jsonObject.getString("name");
            this.num = jsonObject.getString("num");
            this.st = jsonObject.getString("st");
            this.dt = jsonObject.getString("dt");
            this.tt = jsonObject.getString("tt");
            this.s = jsonObject.getString("s");
            this.d = jsonObject.getString("d");
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public List<TicketPrice> getPriceList(){
        return priceList;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public class TicketPrice{
        private String className,price;
        public TicketPrice(String className,String price){
            this.className = className;
            this.price = price;
        }
        public String toString(){
            return ""+this.className+" - ₹"+price;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}

