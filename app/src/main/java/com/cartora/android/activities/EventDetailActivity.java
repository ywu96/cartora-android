package com.cartora.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cartora.android.R;
import com.cartora.android.adapters.EventListAdapter;
import com.cartora.android.models.EventWithParticipantsResponse;
import com.cartora.android.rest.RestServer;
import com.cartora.android.utils.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

	private TextView eventName;
	private TextView eventDate;
	private TextView eventLocation;

	private EventWithParticipantsResponse event;

	private GoogleMap map;
	private boolean isMapSetup = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		int eventId = getIntent().getIntExtra(EventListAdapter.EVENT_LIST_ID_KEY, -1);

		eventName = (TextView) findViewById(R.id.event_name);
		eventDate = (TextView) findViewById(R.id.event_date);
		eventLocation = (TextView) findViewById(R.id.event_location);

		populateFields(eventId);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.event_map);
		mapFragment.getMapAsync(this);
	}

	private void populateFields(int eventId) {
		RestServer.createService(Utility.getAuthToken(this))
				.getEvent(Utility.getUid(this), eventId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<EventWithParticipantsResponse>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
					}

					@Override
					public void onNext(EventWithParticipantsResponse event) {
						EventDetailActivity.this.event = event;
						eventName.setText(event.name);
						eventName.setSelected(true);

						Date date = new Date(event.startTime * 1000L);
						SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a");
						eventDate.setText(format.format(date));

						if (map != null && !isMapSetup) {
							setupMap(map);
							isMapSetup = true;
						}
					}
				});
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

		if (event != null) {
			setupMap(map);
			isMapSetup = true;
		}
	}

	private void setupMap(GoogleMap googleMap) {
		LatLng eventLatLng = new LatLng(event.location.latitude, event.location.longitude);
		MarkerOptions markerOptions = new MarkerOptions().position(eventLatLng);
		googleMap.addMarker(markerOptions);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLatLng, 14f));
	}
}
