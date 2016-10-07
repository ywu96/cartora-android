package com.traveltime.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.traveltime.android.R;
import com.traveltime.android.fragments.EventListFragment;
import com.traveltime.android.utils.Strings;

public class EventListActivity extends AppCompatActivity {

	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		setTitle(R.string.event_list_title);

		userId = getSharedPreferences(Strings.SHARED_PREF_NAME, MODE_PRIVATE).getString(Strings.UID_KEY, null);

		getSupportFragmentManager().beginTransaction().replace(R.id.container, new EventListFragment()).commit();
	}

	@Override
	public void onBackPressed() {
		// Do nothing.
	}
}
