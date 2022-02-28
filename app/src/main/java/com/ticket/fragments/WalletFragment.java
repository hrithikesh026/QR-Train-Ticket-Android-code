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
import android.widget.TextView;

import com.ticket.R;



public class WalletFragment extends Fragment {

    private TextView balanceView;
    private Button recharge;
    public WalletFragment() {
        // Required empty public constructor
    }

    public static WalletFragment newInstance() {
        WalletFragment fragment = new WalletFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        balanceView = (TextView) view.findViewById(R.id.wallet_balance_textview);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User_Details", Context.MODE_PRIVATE);
        balanceView.setText(""+sharedPreferences.getInt("wallet_balance",0));
        recharge = (Button) view.findViewById(R.id.rechargebtn);

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment2 ;
                fragment2 = RechargeFragment.newInstance();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment2);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        return view;
    }
}