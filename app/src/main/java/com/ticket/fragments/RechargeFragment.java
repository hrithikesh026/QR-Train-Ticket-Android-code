package com.ticket.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ticket.R;
import com.ticket.RechargeSuccessListener;
import com.ticket.ServerConnection.AsyncTaskClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class RechargeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText amount,password;
    private Button recharge;
    private int status_code;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RechargeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RechargeFragment newInstance() {
        RechargeFragment fragment = new RechargeFragment();
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
        View view = inflater.inflate(R.layout.fragment_recharge, container, false);
        amount = view.findViewById(R.id.amount_edit_text);
        password = view.findViewById(R.id.password_edit_text);
        recharge = view.findViewById(R.id.recharge_button);

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amt, pass;
                amt = amount.getText().toString();
                pass = password.getText().toString();
                if(amt.isEmpty()){
                    amount.setError("Enter Amount");
                }
                if(pass.isEmpty()){
                    password.setError("Enter password");
                }
                if(!amt.isEmpty() && !pass.isEmpty()){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User_Details", Context.MODE_PRIVATE);
                    String email = sharedPreferences.getString("email","");

                    try{
                        JSONObject obj = new JSONObject();
                        obj.put("email",email);
                        obj.put("amount",Integer.parseInt(amt));

                        AsyncTaskClass asyncTaskClass = new AsyncTaskClass(getActivity(),obj.toString());
                        asyncTaskClass.setRechargeSuccessListener(new RechargeSuccessListener() {
                            @Override
                            public void onSuccess(String jsonString) {
                                try{
                                    JSONObject obj = new JSONObject(jsonString);
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User_Details", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("wallet_balance",obj.getInt("wallet_balance"));
                                    editor.commit();

                                    Fragment fragment2 ;
                                    fragment2 = WalletFragment.newInstance();
                                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container,fragment2);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailed() {
                                Toast.makeText(getActivity(),"Unable to recharge wallet",Toast.LENGTH_SHORT);
                            }
                        });
                        asyncTaskClass.send("recharge");

                    }
                    catch(JSONException ex){
                        ex.printStackTrace();
                    }
                    catch (MalformedURLException ex){
                        ex.printStackTrace();
                    }




                }
            }
        });

        return view;
    }
}