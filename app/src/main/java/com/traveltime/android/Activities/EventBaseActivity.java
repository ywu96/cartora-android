package com.traveltime.android.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.traveltime.android.Fragments.EventCreateFragment;
import com.traveltime.android.Fragments.EventListFragment;
import com.traveltime.android.R;
import com.traveltime.android.Utils.Strings;
import com.traveltime.android.Utils.Utility;

public class EventBaseActivity extends AppCompatActivity {

	private String userId;

	public static boolean isTransitioning = false;
	private FloatingActionButton fab;
	private EventListFragment eventListFragment;
	private EventCreateFragment eventCreateFragment;

	private static final long TRANSITION_DURATION = 500L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_base);

		setTitle(R.string.event_list_title);

		this.userId = getSharedPreferences(Strings.SHARED_PREF_NAME, MODE_PRIVATE).getString(Strings.UID_KEY, null);

		this.eventListFragment = new EventListFragment();
		this.eventCreateFragment = new EventCreateFragment();

		getSupportFragmentManager().beginTransaction().add(R.id.base_fragment_container, eventListFragment).commit();

		this.fab = (FloatingActionButton) findViewById(R.id.event_base_fab);
		this.fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getSupportFragmentManager().findFragmentById(R.id.base_fragment_container) instanceof EventListFragment) {
					fab.animate().translationY(Utility.convertToPixels(getBaseContext(), 100)).setDuration(TRANSITION_DURATION).setInterpolator(new AccelerateInterpolator()).start();
					setTitle(R.string.event_create_title);
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
					fragmentTransaction.replace(R.id.base_fragment_container, eventCreateFragment).commit();
					startTransitionTimer();
				}
			}
		});
		this.fab.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(getBaseContext(), LocationActivity.class);
				startActivity(intent);
				return true;
			}
		});
	}

	public static void startTransitionTimer() {
		isTransitioning = true;

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isTransitioning = false;
			}
		}, TRANSITION_DURATION);
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().findFragmentById(R.id.base_fragment_container) instanceof EventCreateFragment && !isTransitioning) {
			this.fab.animate().translationY(0f).setDuration(TRANSITION_DURATION).setInterpolator(new DecelerateInterpolator()).start();
			setTitle(R.string.event_list_title);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			fragmentTransaction.replace(R.id.base_fragment_container, this.eventListFragment).commit();
			startTransitionTimer();
		}
	}
}
