package com.example.mjami.a91won;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
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

    boolean permissionsMet = false;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(this, PhoneTrackingService.class);

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
                    if(serviceIsRunning(PhoneTrackingService.class)) {
                        Log.d("service", "stopping service");
                        stopService(serviceIntent);
                    }
                }
            }
        });
    }

    private boolean serviceIsRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
        else { //all permissions already acquired
            Log.d("permissions", "all permissions met");
            permissionsMet = true;
            beginPhoneTracking();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Log.d("permissions", "callback");
        boolean allPermissionsMet = true;
        for (int i = 0; i < permissions.length; i++){
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                Log.d("permissions", "Permission missing: " + permissions[i]);
                allPermissionsMet = false;
            }
        }

        permissionsMet = allPermissionsMet;
        beginPhoneTracking();
    }

    private void beginPhoneTracking(){
        if (permissionsMet) {
            Log.d("service", "starting service");
//            Intent serviceIntent = new Intent(this, PhoneTrackingService.class);
            startService(serviceIntent);
        }
    }
}
