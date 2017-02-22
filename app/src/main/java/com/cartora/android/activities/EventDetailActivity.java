package com.cartora.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.cartora.android.R;
import com.cartora.android.models.EventResponse;
import com.cartora.android.adapters.EventListAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

	private TextView eventName;
	private TextView eventDate;
	private TextView eventLocation;

	private EventResponse event;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		event = Parcels.unwrap(getIntent().getParcelableExtra(EventListAdapter.EVENT_LIST_PARCEL_KEY));

		eventName = (TextView) findViewById(R.id.event_name);
		eventDate = (TextView) findViewById(R.id.event_date);
		eventLocation = (TextView) findViewById(R.id.event_location);

		eventName.setText(event.name);
		eventName.setSelected(true);

		Date date = new Date(event.startTime * 1000L);
		SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a");
		eventDate.setText(format.format(date));

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.event_map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

		// TODO: uncomment this once Millin adds locations to events
//		LatLng eventLatLng = new LatLng(event.location.latitude, event.location.longitude);
//		MarkerOptions markerOptions = new MarkerOptions().position(eventLatLng);
//		map.addMarker(markerOptions);
//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLatLng, 15f));
	}
}
