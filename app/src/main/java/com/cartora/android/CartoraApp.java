package com.cartora.android;

import android.app.Application;
import android.content.Context;

public class CartoraApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public Context getAppContext() {
		return getApplicationContext();
	}
}
