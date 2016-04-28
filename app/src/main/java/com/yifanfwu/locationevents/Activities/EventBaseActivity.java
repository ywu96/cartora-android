package com.yifanfwu.locationevents.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.yifanfwu.locationevents.Fragments.EventCreateFragment;
import com.yifanfwu.locationevents.Fragments.EventListFragment;
import com.yifanfwu.locationevents.Models.EventResponse;
import com.yifanfwu.locationevents.R;
import com.yifanfwu.locationevents.Rest.RestServer;
import com.yifanfwu.locationevents.Utils.Utility;

public class EventBaseActivity extends AppCompatActivity {

	protected Firebase firebaseRef;
	protected String userId;

	protected FloatingActionButton fab;
	protected EventListFragment eventListFragment;
	protected EventCreateFragment eventCreateFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_event_base);

		setTitle(R.string.event_list_title);

		this.firebaseRef = new Firebase("https://vivid-inferno-3846.firebaseio.com");
		this.userId = this.firebaseRef.getAuth().getUid();

		this.eventListFragment = new EventListFragment();
		this.eventCreateFragment = new EventCreateFragment();

		getSupportFragmentManager().beginTransaction().add(R.id.base_fragment_container, eventListFragment).commit();

		this.fab = (FloatingActionButton) findViewById(R.id.event_base_fab);
		this.fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getSupportFragmentManager().findFragmentById(R.id.base_fragment_container) instanceof EventListFragment) {
					fab.animate().translationY(Utility.convertToPixels(getBaseContext(), 100)).setDuration(300L).setInterpolator(new AccelerateInterpolator()).start();
					setTitle(R.string.event_create_title);
					getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment_container, eventCreateFragment).commit();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().findFragmentById(R.id.base_fragment_container) instanceof EventCreateFragment) {
			this.fab.animate().translationY(0f).setDuration(300L).setInterpolator(new DecelerateInterpolator()).start();
			setTitle(R.string.event_list_title);
			getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment_container, this.eventListFragment).commit();
		}
	}
}
