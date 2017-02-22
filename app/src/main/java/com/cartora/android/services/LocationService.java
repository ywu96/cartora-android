package com.cartora.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("test123", "Location service created");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
