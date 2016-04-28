package com.yifanfwu.locationevents.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yifanfwu.locationevents.Models.EventRequest;
import com.yifanfwu.locationevents.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventCreateFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

	protected TextView placePickerText;
	protected TextView datePickerText;
	protected TextView timePickerText;
	protected FrameLayout spinnerContainer;
	protected Calendar calendar;

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

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);

		this.calendar = null;

		this.spinnerContainer = (FrameLayout) rootView.findViewById(R.id.spinner_container);
		this.spinnerContainer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// stop user from pressing things
			}
		});
		this.spinnerContainer.setVisibility(View.GONE);

		this.datePickerText = (TextView) rootView.findViewById(R.id.event_date_text);
		this.datePickerText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (calendar == null) {
					calendar = Calendar.getInstance();
				}
				Calendar now = Calendar.getInstance();
				DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
						EventCreateFragment.this,
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)
				);
				datePickerDialog.setThemeDark(true);
				datePickerDialog.vibrate(false);
				datePickerDialog.setMinDate(now);
				datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
			}
		});

		this.timePickerText = (TextView) rootView.findViewById(R.id.event_time_text);
		this.timePickerText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (calendar == null) {
					calendar = Calendar.getInstance();
				}
				TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
						EventCreateFragment.this,
						calendar.get(Calendar.HOUR_OF_DAY),
						calendar.get(Calendar.MINUTE),
						false
				);
				timePickerDialog.setThemeDark(true);
				timePickerDialog.vibrate(false);
				timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
			}
		});

		this.placePickerText = (TextView) rootView.findViewById(R.id.event_location_text);
		this.placePickerText.setOnClickListener(new View.OnClickListener() {
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_event_create, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_accept:
				Toast.makeText(getActivity(), "Event created!", Toast.LENGTH_SHORT).show();
				getActivity().onBackPressed();
				break;
			case R.id.action_cancel:
				getActivity().onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		this.calendar.set(year, monthOfYear, dayOfMonth);

		SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy");
		this.datePickerText.setText(format.format(calendar.getTime()));
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
		this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		this.calendar.set(Calendar.MINUTE, minute);

		SimpleDateFormat format = new SimpleDateFormat("h:mm a");
		this.timePickerText.setText(format.format(calendar.getTime()));
	}

	public EventRequest generateEvent() {
		return null;
	}
}
