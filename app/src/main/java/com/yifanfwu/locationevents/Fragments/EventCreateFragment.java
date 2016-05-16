package com.yifanfwu.locationevents.Fragments;


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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yifanfwu.locationevents.Activities.EventBaseActivity;
import com.yifanfwu.locationevents.Models.EventRequest;
import com.yifanfwu.locationevents.Models.EventResponse;
import com.yifanfwu.locationevents.Models.EventUserRequest;
import com.yifanfwu.locationevents.R;
import com.yifanfwu.locationevents.Rest.RestServer;
import com.yifanfwu.locationevents.Utils.Strings;

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

		if (this.googleApiClient == null) {
			this.googleApiClient = new GoogleApiClient
					.Builder(getActivity())
					.addOnConnectionFailedListener(this)
					.addConnectionCallbacks(this)
					.addApi(LocationServices.API)
					.addApi(Places.GEO_DATA_API)
					.addApi(Places.PLACE_DETECTION_API)
					.enableAutoManage(getActivity(), this)
					.build();
		}

		this.userList = new ArrayList<>();

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);

		this.eventName = (EditText) rootView.findViewById(R.id.event_name_field);
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
				datePickerDialog.vibrate(true);
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
				timePickerDialog.vibrate(true);
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

		this.mapLayout = (LinearLayout) rootView.findViewById(R.id.map_layout);
		this.mapView = (MapView) rootView.findViewById(R.id.map_view);
		this.mapView.onCreate(savedInstanceState);
		this.mapView.getMapAsync(this);
		MapsInitializer.initialize(getActivity());

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		this.googleApiClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		this.googleApiClient.disconnect();
	}

	@Override
	public void onResume() {
		super.onResume();
		this.mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		this.mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		this.mapView.onLowMemory();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			spinnerContainer.setVisibility(View.GONE);
			if (resultCode == Activity.RESULT_OK) {
				Place place = PlacePicker.getPlace(getActivity(), data);
				this.eventLocation = place;
				this.placePickerText.setText(place.getName().toString());
				this.mapLayout.setVisibility(View.VISIBLE);
				this.googleMap.clear();
				this.googleMap.addMarker(new MarkerOptions()
						.position(place.getLatLng())
						.icon(BitmapDescriptorFactory.defaultMarker()));
				this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13f));
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
					if (this.eventName.getText().toString().isEmpty()) {
						Toast.makeText(getActivity(), "Enter an event name", Toast.LENGTH_SHORT).show();
						return false;
					}
					if (this.datePickerText.getText().equals(getString(R.string.event_date_hint))) {
						Toast.makeText(getActivity(), "Pick an event date", Toast.LENGTH_SHORT).show();
						return false;
					}
					if (this.eventLocation == null) {
						Toast.makeText(getActivity(), "Set an event location", Toast.LENGTH_SHORT).show();
						return false;
					}
					SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(Strings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
					EventUserRequest selfUser = new EventUserRequest(preferences.getString(Strings.UID_KEY, null));
					this.userList.add(selfUser);

					// user did not change time
					if (this.timePickerText.getText().toString().equals("12:00 PM")) {
						this.calendar.set(Calendar.HOUR_OF_DAY, 12);
						this.calendar.set(Calendar.MINUTE, 0);
					}

					EventRequest newEvent = new EventRequest(this.eventName.getText().toString(),
							this.userList,
							this.eventLocation.getLatLng().latitude,
							this.eventLocation.getLatLng().longitude,
							this.calendar.getTimeInMillis()/1000);

					RestServer.getInstance().createEvent(newEvent, new RestServer.Callback<EventResponse>() {
						@Override
						public void result(EventResponse result) {
							Toast.makeText(getActivity(), "Event created!", Toast.LENGTH_SHORT).show();
							eventName.setText("");
							eventLocation = null;
							getActivity().onBackPressed();
						}
					});
					break;
				case R.id.action_cancel:
					getActivity().onBackPressed();
					break;
			}
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

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {

	}
}
