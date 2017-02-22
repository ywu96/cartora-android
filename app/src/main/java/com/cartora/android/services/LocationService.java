package com.cartora.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class LocationService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();

		// TODO: just for testing
		Log.d("test123", "Location service created");
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
