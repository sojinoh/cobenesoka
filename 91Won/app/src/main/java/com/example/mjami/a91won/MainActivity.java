package com.example.mjami.a91won;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int MY_PROCESS_OUTGOING_CALLS_PERMISSION = 0;
    final int MY_INTERNET_PERMISSION = 1;
    final int MY_FINE_LOCATION_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton onOrOff = (ToggleButton) findViewById(R.id.toggleButton);
        onOrOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Log.d("checked","checked");
                    checkAndSetPermissions();
                }
                else
                {
                    Log.d("checked", "unchecked");
                }
            }
        });
    }

    private void checkAndSetPermissions() {
        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<Integer> permissions_nums = new ArrayList<>();
        //TODO: Should probably change "this" to the background service
        Log.d("permissions", "ready");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("permissions", "needed");
            permissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
            permissions_nums.add(MY_PROCESS_OUTGOING_CALLS_PERMISSION);
            Log.d("permissions", "added");
        }

        //Internet Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
            permissions_nums.add(MY_INTERNET_PERMISSION);
        }

        //Fine Location Services
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions_nums.add(MY_FINE_LOCATION_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
            permissions_nums.add(MY_FINE_LOCATION_PERMISSION);
        }


        if(permissions.size() != 0){
            String[] permissionsArray = new String[permissions.size()];
            for(int i = 0; i < permissions.size(); i++){
                permissionsArray[i] = permissions.get(i);
            }
            Log.d("permissions", "about to request");
            ActivityCompat.requestPermissions(this, permissionsArray, 0);
        }
        else {
            Intent serviceIntent = new Intent(this, PhoneTrackingService.class);
            startService(serviceIntent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Log.d("permissions", "callback");
        for(String perm : permissions){
            Log.d("permissions", perm);
        }

        Intent serviceIntent = new Intent(this, PhoneTrackingService.class);
        startService(serviceIntent);
    }
}
