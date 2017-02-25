package com.example.mjami.a91won;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

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
        //TODO: Should probably change "this" to the background service
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PROCESS_OUTGOING_CALLS)){
                //get permissions request
                Log.d("permissions", "requestion outgoing_calls permissions");
            }
            else{
                //no action needed?
                Log.d("permissions", "permissions already held");
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PROCESS_OUTGOING_CALLS)){
                //get permissions request
                Log.d("permissions", "requestion outgoing_calls permissions");
            }
            else{
                //no action needed?
                Log.d("permissions", "permissions already held");
            }
        }
    }
}
