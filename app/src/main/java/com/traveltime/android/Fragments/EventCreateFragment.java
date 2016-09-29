package com.traveltime.android.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.traveltime.android.activities.EventBaseActivity;
import com.traveltime.android.models.EventRequest;
import com.traveltime.android.models.EventResponse;
import com.traveltime.android.models.EventUserRequest;
import com.traveltime.android.R;
import com.traveltime.android.rest.RestServer;
import com.traveltime.android.utils.Strings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EventCreateFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback {

	private TextView placePickerText;
	private TextView datePickerText;
	private TextView timePickerText;
	private FrameLayout spinnerContainer;
	private Calendar calendar;
	private boolean isCreatingEvent;

	private LinearLayout mapLayout;
	private GoogleMap googleMap;
	private MapView mapView;

	private EditText eventName;
	private Place eventLocation;
	private ArrayList<EventUserRequest> userList;

	private GoogleApiClient googleApiClient;
	private final int PLACE_PICKER_REQUEST = 1;

	public EventCreateFragment() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient
					.Builder(getActivity())
					.addOnConnectionFailedListener(this)
					.addConnectionCallbacks(this)
					.addApi(LocationServices.API)
					.addApi(Places.GEO_DATA_API)
					.addApi(Places.PLACE_DETECTION_API)
					.enableAutoManage(getActivity(), this)
					.build();
		}

		userList = new ArrayList<>();

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);

		isCreatingEvent = false;
		eventName = (EditText) rootView.findViewById(R.id.event_name_field);
		calendar = null;

		spinnerContainer = (FrameLayout) rootView.findViewById(R.id.spinner_container);
		spinnerContainer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// stop user from pressing things
			}
		});
		spinnerContainer.setVisibility(View.GONE);

		datePickerText = (TextView) rootView.findViewById(R.id.event_date_text);
		datePickerText.setOnClickListener(new View.OnClickListener() {
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
				datePickerDialog.vibrate(true);
				datePickerDialog.setMinDate(now);
				datePickerDialog.show(getActivity().getFragmentManager(), "datepickerdialog");
			}
		});

		timePickerText = (TextView) rootView.findViewById(R.id.event_time_text);
		timePickerText.setOnClickListener(new View.OnClickListener() {
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
				timePickerDialog.vibrate(true);
				timePickerDialog.show(getActivity().getFragmentManager(), "timepickerdialog");
			}
		});

		placePickerText = (TextView) rootView.findViewById(R.id.event_location_text);
		placePickerText.setOnClickListener(new View.OnClickListener() {
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

		mapLayout = (LinearLayout) rootView.findViewById(R.id.map_layout);
		mapView = (MapView) rootView.findViewById(R.id.map_view);
		mapView.onCreate(savedInstanceState);
		mapView.getMapAsync(this);
		MapsInitializer.initialize(getActivity());

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		googleApiClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		googleApiClient.disconnect();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			spinnerContainer.setVisibility(View.GONE);
			if (resultCode == Activity.RESULT_OK) {
				Place place = PlacePicker.getPlace(getActivity(), data);
				eventLocation = place;
				placePickerText.setText(place.getName().toString());
				mapLayout.setVisibility(View.VISIBLE);
				googleMap.clear();
				googleMap.addMarker(new MarkerOptions()
						.position(place.getLatLng()));
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13f));
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
		if (!EventBaseActivity.isTransitioning) {
			switch (item.getItemId()) {
				case R.id.action_accept:
					if (eventName.getText().toString().isEmpty()) {
						Toast.makeText(getActivity(), "Enter an event name", Toast.LENGTH_SHORT).show();
						return false;
					}
					if (datePickerText.getText().equals(getString(R.string.event_date_hint))) {
						Toast.makeText(getActivity(), "Pick an event date", Toast.LENGTH_SHORT).show();
						return false;
					}
					if (eventLocation == null) {
						Toast.makeText(getActivity(), "Set an event location", Toast.LENGTH_SHORT).show();
						return false;
					}
					if (isCreatingEvent) {
						return false;
					}
					isCreatingEvent = true;
					spinnerContainer.setAlpha(0f);
					spinnerContainer.setVisibility(View.VISIBLE);
					spinnerContainer.animate().alpha(1f).setDuration(200L).start();

					SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(Strings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
					EventUserRequest selfUser = new EventUserRequest(preferences.getString(Strings.UID_KEY, null));
					userList.add(selfUser);

					// user did not change time
					if (timePickerText.getText().toString().equals("12:00 PM")) {
						calendar.set(Calendar.HOUR_OF_DAY, 12);
						calendar.set(Calendar.MINUTE, 0);
					}

					EventRequest newEvent = new EventRequest(eventName.getText().toString(),
							userList,
							eventLocation.getLatLng().latitude,
							eventLocation.getLatLng().longitude,
							calendar.getTimeInMillis()/1000);

					RestServer.getInstance().createEvent(newEvent, new RestServer.Callback<EventResponse>() {
						@Override
						public void result(EventResponse result) {
							isCreatingEvent = false;
							spinnerContainer.setVisibility(View.GONE);
							Toast.makeText(getActivity(), "Event created!", Toast.LENGTH_SHORT).show();
							eventName.setText("");
							eventLocation = null;
							getActivity().onBackPressed();
						}
					});
					break;
				case R.id.action_cancel:
					eventName.setText("");
					eventLocation = null;
					getActivity().onBackPressed();
					break;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		calendar.set(year, monthOfYear, dayOfMonth);

		SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
		datePickerText.setText(format.format(calendar.getTime()));
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);

		SimpleDateFormat format = new SimpleDateFormat("h:mm a");
		timePickerText.setText(format.format(calendar.getTime()));
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {}

	@Override
	public void onConnectionSuspended(int i) {}
}
