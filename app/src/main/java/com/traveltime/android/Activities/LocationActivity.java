package com.traveltime.android.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.traveltime.android.R;
import com.traveltime.android.UIHelpers.LatLngInterpolator;
import com.traveltime.android.Utils.Utility;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap map;

	private Marker selfMarker;
	private LocationManager locationManager;
	private LocationListener locationListener;

	private static final long LOCATION_MIN_TIME = 3000L;
	private static final float LOCATION_MIN_DIST = 10f;

	private static final int LOCATION_UPDATES = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.locationListener = new CustomLocationListener();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_UPDATES);
			return;
		}
		this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MIN_DIST, this.locationListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		this.locationManager.removeUpdates(this.locationListener);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.map = googleMap;

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		this.setInitialMap();
	}

	public class CustomLocationListener implements LocationListener {

		private static final long ANIMATE_DURATION = 3000L;

		@Override
		public void onLocationChanged(Location location) {
			LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());

			if (selfMarker != null) {
				Utility.animateMarker(selfMarker, newLocation, ANIMATE_DURATION, new LatLngInterpolator.Linear());
			}

			map.animateCamera(CameraUpdateFactory.newLatLng(newLocation));
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
			this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MIN_DIST, this.locationListener);
		}
	}

	private void setInitialMap() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		Location lastLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastLocation != null) {
			LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
			MarkerOptions markerOptions = new MarkerOptions().position(lastLatLng);
			selfMarker = map.addMarker(markerOptions);
			this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 16f));
		} else {
			LatLng world = new LatLng(0, 0);
			this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(world, 0.1f));
		}
	}
}
