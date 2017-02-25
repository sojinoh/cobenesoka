package com.example.mjami.a91won;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class PhoneTrackingService extends Service {
    public PhoneTrackingService() {
    }

    Context serviceContext = this;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.google.android.c2dm.intent.RECEIVE");
        filter.addAction("com.google.android.c2dm.intent.REGISTRATION");
        filter.addCategory("com.example.myapp");
        this.registerReceiver(new OutgoingCallReceiver(), filter, "com.google.android.c2dm.permission.SEND", null);
        return super.onStartCommand(intent,flags,startId);
    }

    //inner class of outgoing receiver
    public class OutgoingCallReceiver extends BroadcastReceiver {
        final String emergencyNumber = "7634799116";
        @Override
        public void onReceive(Context context, Intent intent) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("receiver", phoneNumber);
            Log.d("receiver", intent.toString() + ", call to: " + phoneNumber);
            Toast.makeText(context, "Outgoing call detected: " + phoneNumber, Toast.LENGTH_LONG).show();
            //TODO: Handle outgoing call event here
            if(phoneNumber.equals(emergencyNumber)){
//                Intent serviceIntent = new Intent(context, LocationService.class);
//                context.startService(serviceIntent);
                String locationContext = Context.LOCATION_SERVICE;
                LocationManager locationManager = (LocationManager) serviceContext.getSystemService(locationContext);

                Location location = null;
                if (ContextCompat.checkSelfPermission(serviceContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (location != null) {
                    Log.d("location", "Lat: " + location.getLatitude());
                    Log.d("location", "Long: " + location.getLongitude());
                }
                else{
                    Log.d("location", "Location is null");
                }
            }
        }
    }
}
