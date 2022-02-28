package com.ticket.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ticket.R;
import com.ticket.ServerConnection.AsyncTaskClass;
import com.ticket.TrainListSuccessEventListener;
import com.ticket.adapters.TrainRecyclerViewAdapter;
import com.ticket.objects.TrainDetails;

import java.net.MalformedURLException;
import java.util.ArrayList;


public class BuyTicketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "qrcode";
    private TextView selectTrain, currentStation;
    private AutoCompleteTextView destination;
    private Spinner selectTrainSpinner;
    private AsyncTaskClass asyncTaskClass;
    private RecyclerView trainListRV;
    private Button getTain;
    // TODO: Rename and change types of parameters
    private String qrcode;


    public BuyTicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
//     * @param  param Parameter 2.
     * @return A new instance of fragment BuyTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyTicketFragment newInstance(String param1) {
        BuyTicketFragment fragment = new BuyTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrcode = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_ticket, container, false);
        destination= (AutoCompleteTextView) view.findViewById(R.id.destination);
        selectTrain = (TextView) view.findViewById(R.id.select_train);
        getTain = (Button) view.findViewById(R.id.get_train);
        currentStation = (TextView)view.findViewById(R.id.current_station);
        selectTrain.setVisibility(View.INVISIBLE);
        trainListRV = (RecyclerView) view.findViewById(R.id.train_list_rv);

        currentStation.setText("You are at "+qrcode);
        trainListRV.setVisibility(View.INVISIBLE);

        Resources res = getResources();
        final String[] STATIONS = res.getStringArray(R.array.stations);


        destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trainListRV.setVisibility(View.VISIBLE);
                selectTrain.setVisibility(View.VISIBLE);
                String source = qrcode;
                String dest = destination.getText().toString();
                JsonObject obj = new JsonObject();
                obj.addProperty("source",source);
                obj.addProperty("destination",dest);
                try{
                    asyncTaskClass = new AsyncTaskClass(getActivity(), obj.getAsString());
                    asyncTaskClass.setTrainListSucessListner(new TrainListSuccessEventListener() {
                        @Override
                        public void onSuccess(ArrayList<TrainDetails> trainDetailsArrayList) {
                            TrainRecyclerViewAdapter tdAdapter = new TrainRecyclerViewAdapter(getActivity(),trainDetailsArrayList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                            trainListRV.setLayoutManager(linearLayoutManager);
                            trainListRV.setAdapter(tdAdapter);
                        }

                        @Override
                        public void onFailed() {
                            Toast.makeText(getActivity(),"Unable to fetch Train Details",Toast.LENGTH_LONG).show();
                        }
                    });
                    asyncTaskClass.send("gettrainlist");
                }
                catch (MalformedURLException ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getTain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainListRV.setVisibility(View.VISIBLE);
                String source = qrcode;
                String dest = destination.getText().toString();
                JsonObject obj = new JsonObject();
                obj.addProperty("source",source);
                obj.addProperty("destination",dest);
                try{
                    asyncTaskClass = new AsyncTaskClass(getActivity(), obj.toString());
                    asyncTaskClass.setTrainListSucessListner(new TrainListSuccessEventListener() {
                        @Override
                        public void onSuccess(ArrayList<TrainDetails> trainDetailsArrayList) {
                            if(trainDetailsArrayList.size() == 0){
                                selectTrain.setVisibility(View.VISIBLE);
                                selectTrain.setText("No trains Available today");
                                Toast.makeText(getActivity(),"No Trains available Today",Toast.LENGTH_LONG);
                            }
                            else{
                                TrainRecyclerViewAdapter tdAdapter = new TrainRecyclerViewAdapter(getActivity(),trainDetailsArrayList);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                                trainListRV.setLayoutManager(linearLayoutManager);
                                trainListRV.setAdapter(tdAdapter);
                            }

                        }

                        @Override
                        public void onFailed() {
                            Toast.makeText(getActivity(),"Unable to fetch Train Details",Toast.LENGTH_LONG).show();
                        }
                    });
                    asyncTaskClass.send("gettrainlist");
                }
                catch (MalformedURLException ex){
                    ex.printStackTrace();
                }
            }
        });



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_item,
                STATIONS);
        destination.setThreshold(1);
        destination.setAdapter(adapter);


//        textView = (TextView) view.findViewById(R.id.qr);
//        textView.setText(qrcode);



        return view;
    }

}

