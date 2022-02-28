package com.ticket.adapters;

import com.ticket.R;
import com.ticket.ServerConnection.AsyncTaskClass;
import com.ticket.objects.*;
import com.ticket.objects.TrainDetails.TicketPrice;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGEncoder;

public class TrainRecyclerViewAdapter extends RecyclerView.Adapter<TrainRecyclerViewAdapter.Viewholder>{
    private Context context;
    private ArrayList<TrainDetails> trainArrayList;

    // Constructor
    public TrainRecyclerViewAdapter(Context context, ArrayList<TrainDetails> trainArrayList) {
        this.context = context;
        this.trainArrayList = trainArrayList;
    }
    @NonNull
    @Override
    public TrainRecyclerViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainlist_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainRecyclerViewAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        TrainDetails model = trainArrayList.get(position);
        holder.trainNo.setText(model.getNum());
        holder.trainName.setText(model.getName());
        holder.departure.setText(model.getSt());
        holder.arrival.setText(model.getDt());
        holder.journey.setText(model.getTt());
        holder.from.setText(model.getS());
        holder.to.setText(model.getD());
        List<TicketPrice> l = model.getPriceList();
//        for(int i=0;i<l.size();i++){
//            System.out.println(""+l.get(i));
//        }
        ArrayAdapter<TicketPrice> arrayAdapter = new ArrayAdapter<TicketPrice>(context, android.R.layout.simple_spinner_dropdown_item,l);
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        holder.classList.setAdapter(arrayAdapter);

        holder.selectTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("cLICKED" + holder.classList.getSelectedItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Please Confirm");
                builder.setCancelable(true);
                builder.setMessage(" ");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("User_Details", Context.MODE_PRIVATE);
                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("email",sharedPreferences.getString("email",""));
                            jsonObject.put("from",model.getS());
                            jsonObject.put("to",model.getD());
                            jsonObject.put("train_name",model.getName());
                            jsonObject.put("train_no",model.getNum());
                            jsonObject.put("tclass", holder.classList.getSelectedItem().toString().split(" - ₹")[0]);
                            jsonObject.put("ticket_price",Integer.parseInt( holder.classList.getSelectedItem().toString().split(" - ₹")[1]));
                            System.out.println(jsonObject.toString());
                            AsyncTaskClass asyncTaskClass = new AsyncTaskClass(context,jsonObject.toString());
                            asyncTaskClass.send("buyticket");
//
                        }
                        catch(JSONException ex){
                            ex.printStackTrace();
                        }
                        catch (MalformedURLException ex){
                            ex.printStackTrace();
                        }

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return trainArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView trainName, trainNo, arrival, departure, journey, from,to;
        private Spinner classList;
        private Button selectTrain;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            trainName= itemView.findViewById(R.id.train_name);
            trainNo = itemView.findViewById(R.id.train_no);
            arrival = itemView.findViewById(R.id.arrival);
            departure = itemView.findViewById(R.id.departure);
            journey = itemView.findViewById(R.id.journey);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            classList = itemView.findViewById(R.id.class_list);
            selectTrain = itemView.findViewById(R.id.select_train_button);
        }
    }
}
