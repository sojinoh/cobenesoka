package com.example.mjami.a91won;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mjami on 2/25/2017.
 */

public class OutgoingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.d("receiver", phoneNumber);
        Log.d(OutgoingCallReceiver.class.getSimpleName(), intent.toString() + ", call to: " + phoneNumber);
        Toast.makeText(context, "Outgoing call detected: " + phoneNumber, Toast.LENGTH_LONG).show();
        //TODO: Handle outgoing call event here
    }
}