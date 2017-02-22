package com.cartora.android.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class CartoraFirebaseInstanceIDService extends FirebaseInstanceIdService {

	@Override
	public void onTokenRefresh() {
		// Get updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d("test123", "FCM Token: " + refreshedToken);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// Instance ID token to your app server.
//		sendRegistrationToServer(refreshedToken);
	}
}
