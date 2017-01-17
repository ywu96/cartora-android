package com.cartora.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cartora.android.R;
import com.cartora.android.fragments.EventListFragment;

public class EventListActivity extends AppCompatActivity {

	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		setTitle(R.string.event_list_title);

		fab = (FloatingActionButton) findViewById(R.id.event_list_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EventListActivity.this, EventCreateActivity.class);
				startActivity(intent);
			}
		});
		fab.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(EventListActivity.this, LocationActivity.class);
				startActivity(intent);
				return true;
			}
		});

		getSupportFragmentManager().beginTransaction().replace(R.id.container, new EventListFragment()).commit();
	}

	@Override
	public void onBackPressed() {
		// Do nothing.
	}
}
