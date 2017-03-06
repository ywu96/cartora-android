package com.cartora.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cartora.android.utils.Strings;
import com.google.firebase.iid.FirebaseInstanceId;

public class CartoraApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// TODO: just for testing
		String token = FirebaseInstanceId.getInstance().getToken();
		if (!Strings.isNullOrEmpty(token)) {
			SharedPreferences sharedPreferences = getSharedPreferences(Strings.SHARED_PREF_NAME, MODE_PRIVATE);
			sharedPreferences.edit()
					.putString(Strings.SHARED_PREF_FCM_ID_KEY, token)
					.apply();
		}
		Log.d("test123", "FCM Token: " + token);
	}

	public Context getAppContext() {
		return getApplicationContext();
	}
}
