package com.cartora.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cartora.android.R;
import com.cartora.android.fragments.EventCreateFragment;

public class EventCreateActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_create);

		getSupportFragmentManager().beginTransaction().replace(R.id.container, new EventCreateFragment()).commit();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
}
