package com.traveltime.android.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.traveltime.android.fragments.EventListFragment;
import com.traveltime.android.R;
import com.traveltime.android.utils.Strings;

public class EventListActivity extends AppCompatActivity {

	private String userId;

	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		setTitle(R.string.event_list_title);

		userId = getSharedPreferences(Strings.SHARED_PREF_NAME, MODE_PRIVATE).getString(Strings.UID_KEY, null);

		getSupportFragmentManager().beginTransaction().replace(R.id.container, new EventListFragment()).commit();

		fab = (FloatingActionButton) findViewById(R.id.event_list_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), EventCreateActivity.class);
				startActivity(intent);
			}
		});
		fab.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(getBaseContext(), LocationActivity.class);
				startActivity(intent);
				return true;
			}
		});
	}

	@Override
	public void onBackPressed() {
		// Do nothing.
	}
}
