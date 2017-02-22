package com.cartora.android.fcm;

import android.content.Intent;
import android.util.Log;

import com.cartora.android.services.LocationService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class CartoraFirebaseMessagingService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Map<String, String> data = remoteMessage.getData();

		// TODO: just for testing
		Log.d("test123", "Data received!");
		Intent intent = new Intent(CartoraFirebaseMessagingService.this, LocationService.class);
		startService(intent);
	}
}
