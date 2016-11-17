package com.traveltime.android;

import android.app.Application;
import android.content.Context;

public class TravelTimeApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public Context getAppContext() {
		return getApplicationContext();
	}
}
