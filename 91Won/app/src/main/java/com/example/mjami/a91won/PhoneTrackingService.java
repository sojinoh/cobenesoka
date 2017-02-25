package com.example.mjami.a91won;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class PhoneTrackingService extends Service {

    OutgoingCallReceiver receiver;
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
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        receiver = new OutgoingCallReceiver();
        this.registerReceiver(receiver, filter);
        return START_STICKY;
    }

    public void cleanupService(){
        this.unregisterReceiver(receiver); //unregisters BroadcastReceiver
    }

    public void onDestroy(){
        cleanupService();
        super.onDestroy();
    }
    //inner class of outgoing receiver
    public class OutgoingCallReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        private GoogleApiClient mGoogleApiClient;

        final String emergencyNumber = "7634799116";
        @Override
        public void onReceive(Context context, Intent intent) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("receiver", phoneNumber);
            Log.d("receiver", intent.toString() + ", call to: " + phoneNumber);
            Toast.makeText(context, "Transmitting Location to Emergency Services " , Toast.LENGTH_LONG).show();

            if(phoneNumber.equals(emergencyNumber)){
                createAndConnectGoogleLocationClient();
            }
        }

        private void createAndConnectGoogleLocationClient(){
            mGoogleApiClient = new GoogleApiClient.Builder(serviceContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();

            if(mGoogleApiClient != null){
                Log.d("connection", "client successfully instantiated");
                mGoogleApiClient.connect();
            }
        }

        @Override
        public void onConnected(Bundle bundle) {

            Log.d("connection", "client connected");
            Location location = null;

            if (ContextCompat.checkSelfPermission(serviceContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }

            if (location != null) {
                Log.d("location", "Lat: " + location.getLatitude());
                Log.d("location", "Long: " + location.getLongitude());

                LocationAsyncTask task = new LocationAsyncTask();
                String latStr = new Double(location.getLatitude()).toString();
                String longStr = new Double(location.getLongitude()).toString();
                TelephonyManager tMgr = (TelephonyManager)serviceContext.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                Log.d("phone", mPhoneNumber);

                task.execute(new String[] {latStr, longStr, mPhoneNumber});
            }
            else{
                Log.d("location", "Location is null");
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d("suspended", "connection suspended");
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d("failure", "Could not connect");
        }
    }
}
