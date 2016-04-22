package com.yifanfwu.locationevents.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.yifanfwu.locationevents.R;

public class EventCreateFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

	protected LinearLayout locationLayout;
	protected TextView placePickerText;
	protected FrameLayout spinnerContainer;

	protected Place eventLocation;

	protected GoogleApiClient googleApiClient;
	protected final int PLACE_PICKER_REQUEST = 1;

	public EventCreateFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (this.googleApiClient != null) {
			this.googleApiClient = new GoogleApiClient
					.Builder(getActivity())
					.addApi(Places.GEO_DATA_API)
					.addApi(Places.PLACE_DETECTION_API)
					.enableAutoManage(getActivity(), this)
					.build();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);

		this.spinnerContainer = (FrameLayout) rootView.findViewById(R.id.spinner_container);
		this.spinnerContainer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// stop user from pressing things
			}
		});
		this.spinnerContainer.setVisibility(View.GONE);

		this.placePickerText = (TextView) rootView.findViewById(R.id.event_location_text);
		this.locationLayout = (LinearLayout) rootView.findViewById(R.id.event_location_layout);
		this.locationLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				spinnerContainer.setAlpha(0f);
				spinnerContainer.setVisibility(View.VISIBLE);
				spinnerContainer.animate().alpha(1f).setDuration(200L).start();
				PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
				try {
					startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
				} catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
					e.printStackTrace();
				}
			}
		});
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			spinnerContainer.setVisibility(View.GONE);
			if (resultCode == Activity.RESULT_OK) {
				Place place = PlacePicker.getPlace(getActivity(), data);
				this.eventLocation = place;
				this.placePickerText.setText(place.getName().toString());
			}
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
}
