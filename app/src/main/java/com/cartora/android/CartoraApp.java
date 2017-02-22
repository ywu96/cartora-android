package com.cartora.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class CartoraApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// TODO: just for testing
		String token = FirebaseInstanceId.getInstance().getToken();
		Log.d("test123", "FCM Token: " + token);
	}

	public Context getAppContext() {
		return getApplicationContext();
	}
}
