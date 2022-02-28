package com.ticket.ServerConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ticket.MainActivity;

import com.ticket.RechargeSuccessListener;
import com.ticket.RegisterActivity;
import com.ticket.TrainListSuccessEventListener;
import com.ticket.login_success;
import com.ticket.objects.TrainDetails;
import com.ticket.objects.UserRegister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AsyncTaskClass  {
//    RegisterListenerCallback registerListenerCallback;
    private ProgressDialog progDialog;
    private static String dataToSend,dataRecieved = "";
    private Context context;
    private String route;
    TrainListSuccessEventListener eventListener = null;
    RechargeSuccessListener rechargeSuccessListener = null;
    //    private Context
    public AsyncTaskClass(Context constext,String jsonString) throws MalformedURLException {

        this.dataToSend = jsonString;
        this.context = constext;
        progDialog = new ProgressDialog(this.context);
        progDialog.setCanceledOnTouchOutside(true);
        progDialog.setMessage("Please wait");
        progDialog.setTitle("Loading");
    }
    public void setTrainListSucessListner(TrainListSuccessEventListener listner){
        this.eventListener = listner;
    }
    public void setRechargeSuccessListener(RechargeSuccessListener listener){
        this.rechargeSuccessListener = listener;
    }


    public void send(String route){
        this.route = route;
        System.out.println("route : "+route);
            try {
                new TaskAsync().execute(new URL("https://qr-train-ticket.herokuapp.com/"+route));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
    }

    private class TaskAsync extends AsyncTask<URL, Integer, Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progDialog.show( context, "Loading", "Wait while loading...");
            dataRecieved="";
//            progDialog.setMax();
            progDialog.show();

        }

        protected Long doInBackground(URL... urls) {
// code that will run in the background
            int statusCode = 0;
            try {

                HttpURLConnection conn = (HttpURLConnection) urls[0].openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                String input = dataToSend;

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
                statusCode = conn.getResponseCode();
                System.out.println(statusCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n"+br.toString());
                while ((output = br.readLine()) != null) {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n"+output);
                    dataRecieved = dataRecieved+output;
                }

                conn.disconnect();

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }
            return Long.valueOf(statusCode);
        }

        protected void onProgressUpdate(Integer... progress) {
// receive progress updates from doInBackground
            System.out.println("Progress--------"+progress);
        }

        protected void onPostExecute(Long result) {

            System.out.println(route);
            if(eventListener!=null){
                progDialog.dismiss();
                if(result.intValue() == 500){
                    eventListener.onFailed();
                }
                else{
                    ArrayList<TrainDetails> tdList = new ArrayList<TrainDetails>();
                    try {
                        JSONArray jsonArray = new JSONArray(dataRecieved);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            System.out.println(obj.toString());
                            tdList.add(new TrainDetails(obj.toString()));
                        }
//                        System.out.println("This is price of first train   ===  " +tdList.get(0).getPriceList().get(0).getPrice());
                        eventListener.onSuccess(tdList);
                    }
                    catch (JSONException e) {
                        System.out.println("Error in json array");
                        e.printStackTrace();
                    }
                }


            }
            else if(route == "recharge"){
                progDialog.dismiss();
                if(result.intValue() == 200){
                    rechargeSuccessListener.onSuccess(dataRecieved);
                }
                else{
                    rechargeSuccessListener.onFailed();

                }

            }
            else if(route == "buyticket"){
                System.out.println(route);
                progDialog.dismiss();
                if(result.intValue() == 201){
                    Toast.makeText(context,"Not Enough Balance",Toast.LENGTH_SHORT).show();
                }
                else if(result.intValue() == 200){
                    Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"Server Error",Toast.LENGTH_SHORT).show();
                }

            }

            else if(context.getClass().getSimpleName().equals("RegisterActivity")){
                RegisterActivity registerActivity = (RegisterActivity)context;
                progDialog.dismiss();
                System.out.println(result);
                if(result.intValue() == 200){
                    Toast.makeText(context,"Registration Successful. Please Login",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                }
                else if(result.intValue() == 201){
                    Toast.makeText(context,"Email Already Registered. Please Login",Toast.LENGTH_LONG).show();
                }
                else if(result.intValue() == 202){
                    Toast.makeText(context,"You need to be atleast 18 years old to register",Toast.LENGTH_LONG).show();
                }
                else if(result.intValue()==500){
                    Toast.makeText(context,"Internal error",Toast.LENGTH_LONG).show();
                }
            }
            else if(context.getClass().getSimpleName().equals("MainActivity")){

                progDialog.dismiss();
                System.out.println(result);
                if(result.intValue() == 200){
                    System.out.println("REcieved===="+dataRecieved);
                    UserRegister user = UserRegister.getObject(dataRecieved);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("User_Details", Context.MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
                    myEdit.putString("email", user.getEmail());
                    myEdit.putString("first_name", user.getFirst_name());
                    myEdit.putString("last_name", user.getLast_name());
                    myEdit.putString("password", user.getPassword());
                    myEdit.putString("dob", user.getDob());
                    myEdit.putInt("wallet_balance", user.getWallet_balance());
                    myEdit.commit();

                    Toast.makeText(context,"Login Successful",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent
                    Intent intent = new Intent(context, login_success.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                }
                else if(result.intValue() == 201){
                    Toast.makeText(context,"Wrong Email or Password",Toast.LENGTH_LONG).show();
                }
                else if(result.intValue() == 500){
                    Toast.makeText(context,"Internal Error",Toast.LENGTH_LONG).show();
                }

            }

        }
    }
}
