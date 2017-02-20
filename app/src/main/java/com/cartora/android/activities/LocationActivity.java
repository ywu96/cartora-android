package com.cartora.android.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.cartora.android.R;
import com.cartora.android.managers.MarkerManager;
import com.cartora.android.models.EventResponse;
import com.cartora.android.models.LocationLatLng;
import com.cartora.android.models.LocationUpdateResponse;
import com.cartora.android.models.UserLocation;
import com.cartora.android.rest.RestServer;
import com.cartora.android.utils.LatLngInterpolator;
import com.cartora.android.utils.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap map;

	private MarkerManager markerManager;
	private LocationManager locationManager;
	private LocationListener locationListener;

	private static final long LOCATION_MIN_TIME = 3000L;
	private static final float LOCATION_MIN_DIST = 0.0f;

	private static final int LOCATION_UPDATES = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new CustomLocationListener();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_UPDATES);
			return;
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MIN_DIST, locationListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		locationManager.removeUpdates(locationListener);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		setInitialMap();
	}

	public class CustomLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			final Context context = LocationActivity.this;

			RestServer.createService(Utility.getAuthToken(context))
					.updateLocationBackground(Utility.getUid(context), LocationLatLng.from(location.getLatitude(), location.getLongitude()))
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<EventResponse>() {
						@Override
						public void onCompleted() {}

						@Override
						public void onError(Throwable e) {}

						@Override
						public void onNext(EventResponse eventResponse) {
							Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show();
						}
					});
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onProviderDisabled(String provider) {}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == LOCATION_UPDATES) {
			for (int i : grantResults) {
				if (i != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(LocationActivity.this, "Your location will not be used", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MIN_DIST, locationListener);
			setInitialMap();
		}
	}

	private void setInitialMap() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastLocation != null) {
			LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 16f));
		} else {
			LatLng world = new LatLng(0, 0);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(world, 0.1f));
		}

		// just for testing
		UserLocation location1 = UserLocation.from(1, LocationLatLng.from(43.8590, -79.2846));
		UserLocation location2 = UserLocation.from(2, LocationLatLng.from(43.8595, -79.2850));
		UserLocation location3 = UserLocation.from(3, LocationLatLng.from(43.8600, -79.2853));
		UserLocation location4 = UserLocation.from(4, LocationLatLng.from(43.8580, -79.2863));

		ArrayList<UserLocation> locations = new ArrayList<>();
		locations.add(location1);
		locations.add(location2);
		locations.add(location3);
		locations.add(location4);

		LocationUpdateResponse lur = new LocationUpdateResponse(locations);
		markerManager = new MarkerManager(map, lur);
	}

	boolean test = false;

	// Just for tests!!!!!!
	@Override
	public void onBackPressed() {
		if (test) {
			super.onBackPressed();
			return;
		}

		UserLocation location1 = UserLocation.from(1, LocationLatLng.from(43.8585, -79.2840));
		UserLocation location2 = UserLocation.from(2, LocationLatLng.from(43.8600, -79.2855));
		UserLocation location3 = UserLocation.from(3, LocationLatLng.from(43.8590, -79.2880));
		UserLocation location4 = UserLocation.from(4, LocationLatLng.from(43.8600, -79.2846));

		ArrayList<UserLocation> locations2 = new ArrayList<>();
		locations2.add(location1);
		locations2.add(location2);
		locations2.add(location3);
		locations2.add(location4);

		LocationUpdateResponse lur2 = new LocationUpdateResponse(locations2);
		markerManager.updateMarkers(lur2);
		test = true;
	}
}
