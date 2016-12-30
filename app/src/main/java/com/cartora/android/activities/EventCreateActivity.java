package com.cartora.android.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cartora.android.R;
import com.cartora.android.fragments.EventCreateFragment;

public class EventCreateActivity extends AppCompatActivity {

	private FloatingActionButton fab;
	private EventCreateFabListener eventCreateFabListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_create);

		EventCreateFragment fragment = new EventCreateFragment();
		eventCreateFabListener = fragment;
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

		fab = (FloatingActionButton) findViewById(R.id.event_create_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				eventCreateFabListener.onFabClick((FloatingActionButton) view);
			}
		});
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	public interface EventCreateFabListener {
		void onFabClick(FloatingActionButton fab);
	}
}
