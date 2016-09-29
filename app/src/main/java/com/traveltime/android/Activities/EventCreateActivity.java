package com.traveltime.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.traveltime.android.R;
import com.traveltime.android.fragments.EventCreateFragment;

public class EventCreateActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_create);

		setTitle(getString(R.string.event_create_title));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportFragmentManager().beginTransaction().replace(R.id.container, new EventCreateFragment()).commit();
	}

}
