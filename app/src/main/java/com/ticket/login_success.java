package com.ticket;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.ticket.fragments.CameraFragment;
import com.ticket.fragments.TicketFragment;
import com.ticket.fragments.WalletFragment;

public class login_success extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success_activity);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new TicketFragment()).commit();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedPreferences = this.getSharedPreferences("User_Details", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    //The Bottom navigation bar
    private NavigationBarView.OnItemSelectedListener bottomNavListener = new NavigationBarView.OnItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        Fragment fragment= null;
        switch (item.getItemId()){
            case R.id.scan:
                fragment = new CameraFragment();
                break;
            case R.id.recharge:
                fragment = new WalletFragment();
                break;
            case R.id.ticket:
                fragment = new TicketFragment();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
        return false;
    }
};




}
