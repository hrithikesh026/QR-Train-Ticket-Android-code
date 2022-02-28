package com.ticket.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ticket.R;
import com.ticket.objects.Tickets;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int status_code=0;
    private TextView header;
    private RecyclerView ticketsRV;
    private ArrayList<Tickets> ticketsArrayList;
    public TicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        ticketsRV = (RecyclerView) view.findViewById(R.id.ticket_recycler);
        header = (TextView)view.findViewById(R.id.header_textview);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User_Details", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");


        String url = "https://qr-train-ticket.herokuapp.com/getticket?email="+ email;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            ticketsArrayList = new ArrayList<Tickets>();
                            header.setText("Your Ticket is Shown below");
                            // Display the first 500 characters of the response string.
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                Tickets ticket = Tickets.getObject(jsonArray.getJSONObject(i).toString());
                                ticketsArrayList.add(ticket);
                            }

                            TicketsAdapter courseAdapter = new TicketsAdapter(getActivity(), ticketsArrayList);

                            // below line is for setting a layout manager for our recycler view.
                            // here we are creating vertical list so we will provide orientation as vertical
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                            // in below two lines we are setting layoutmanager and adapter to our recycler view.
                            ticketsRV.setLayoutManager(linearLayoutManager);
                            ticketsRV.setAdapter(courseAdapter);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                header.setText("You do not have any tickets");
                ticketsRV.setVisibility(View.INVISIBLE);
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);


        return view;
    }

}