package com.example.n7t.myapplication;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Asus NB on 15/11/2017.
 */

public class RegisterFirebase extends FirebaseInstanceIdService {
    String refreshedToken;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
    public String getToken(){
        if(TextUtils.isEmpty(refreshedToken)){
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
        }
        return refreshedToken;
    }
}
