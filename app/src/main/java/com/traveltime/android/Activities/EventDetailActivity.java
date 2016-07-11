package com.traveltime.android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.traveltime.android.Models.EventResponse;
import com.traveltime.android.R;
import com.traveltime.android.UIHelpers.EventListAdapter;

import org.parceler.Parcels;

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

	private TextView eventName;

	private EventResponse event;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		this.event = Parcels.unwrap(getIntent().getParcelableExtra(EventListAdapter.EVENT_LIST_PARCEL_KEY));

		this.eventName = (TextView) findViewById(R.id.event_name);
		this.eventName.setText(this.event.getEventName());

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.event_map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.map = googleMap;
		LatLng eventLatLng = new LatLng(this.event.getEventLocation().getLatitude(), this.event.getEventLocation().getLongitude());
		MarkerOptions markerOptions = new MarkerOptions().position(eventLatLng);
		this.map.addMarker(markerOptions);
		this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLatLng, 15f));
	}
}
