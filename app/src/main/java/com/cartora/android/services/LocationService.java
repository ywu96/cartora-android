package com.cartora.android.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cartora.android.R;
import com.cartora.android.activities.EventListActivity;
import com.cartora.android.models.EventResponse;
import com.cartora.android.models.LocationLatLng;
import com.cartora.android.rest.RestServer;
import com.cartora.android.utils.Utility;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class LocationService extends Service {

	private static final long LOCATION_MIN_TIME = 3000L;
	private static final float LOCATION_MIN_DIST = 0.0f;
	private static final int NOTIFICATION_ID = R.string.location_service_tracking;

	private BackgroundLocationListener backgroundLocationListener;
	private LocationManager locationManager;
	private NotificationManager notificationManager;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("test123", "Location service created");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			stopSelf();
		}
		showNotification();
		backgroundLocationListener = new BackgroundLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MIN_DIST, backgroundLocationListener);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(backgroundLocationListener);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the expanded notification
		CharSequence text = getText(R.string.location_service_tracking);

		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, EventListActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		Notification notification = new Notification.Builder(this)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setTicker(text)
				.setWhen(System.currentTimeMillis())
				.setContentTitle("Event Name")
				.setContentText(text)
				.setContentIntent(contentIntent)
				.build();

		// Send the notification.
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	private class BackgroundLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			Context context = LocationService.this.getApplicationContext();

			RestServer.createService(Utility.getAuthToken(context))
					.updateLocationBackground(Utility.getUid(context),
							LocationLatLng.from(location.getLatitude(), location.getLongitude()))
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<LocationLatLng>() {
						@Override
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
						}

						@Override
						public void onNext(LocationLatLng locationLatLng) {
							Toast.makeText(getApplicationContext(), "location update", Toast.LENGTH_SHORT).show();
						}
					});
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}
	}
}
