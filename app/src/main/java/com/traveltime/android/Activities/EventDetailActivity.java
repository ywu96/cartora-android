package com.traveltime.android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.traveltime.android.Models.EventResponse;
import com.traveltime.android.R;
import com.traveltime.android.UIHelpers.EventListAdapter;

import org.parceler.Parcels;

public class EventDetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		EventResponse event = Parcels.unwrap(getIntent().getParcelableExtra(EventListAdapter.EVENT_LIST_PARCEL_KEY));
		Log.d("test123", event.getEventName());
	}
}
