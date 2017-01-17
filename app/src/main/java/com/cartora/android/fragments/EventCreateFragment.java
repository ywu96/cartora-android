package com.cartora.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cartora.android.activities.EventCreateActivity;
import com.cartora.android.models.EventWithParticipantsResponse;
import com.cartora.android.utils.Utility;
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
import com.cartora.android.R;
import com.cartora.android.models.EventRequest;
import com.cartora.android.rest.RestServer;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class EventCreateFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, DatePickerDialog.OnDateSetListener,
		TimePickerDialog.OnTimeSetListener, OnMapReadyCallback, EventCreateActivity.EventCreateFabListener {
	private final int PLACE_PICKER_REQUEST = 1;

	private Toolbar toolbar;
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

	private GoogleApiClient googleApiClient;

	public EventCreateFragment() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

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
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_create, container, false);
		initViews(rootView);

		calendar = null;

		mapView.onCreate(savedInstanceState);
		mapView.getMapAsync(this);
		MapsInitializer.initialize(getActivity());
		return rootView;
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

	private void initViews(View rootView) {
		toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.event_create_title);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

		eventName = (EditText) rootView.findViewById(R.id.event_name_field);

		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

		spinnerContainer = (FrameLayout) rootView.findViewById(R.id.spinner_container);
		spinnerContainer.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				// Stop the user from touching things
				return false;
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
	}

	@Override
	public void onFabClick(final FloatingActionButton fab) {
		if (eventName.getText().toString().isEmpty()) {
			Snackbar.make(getActivity().findViewById(R.id.container),
					R.string.no_event_name,
					Snackbar.LENGTH_SHORT)
					.show();
			return;
		}
		if (datePickerText.getText().equals(getString(R.string.event_date_hint))) {
			Snackbar.make(getActivity().findViewById(R.id.container),
					R.string.no_event_date,
					Snackbar.LENGTH_SHORT)
					.show();
			return;
		}
		if (eventLocation == null) {
			Snackbar.make(getActivity().findViewById(R.id.container),
					R.string.no_event_location,
					Snackbar.LENGTH_SHORT)
					.show();
			return;
		}
		fab.setOnClickListener(null);
		spinnerContainer.setAlpha(0f);
		spinnerContainer.setVisibility(View.VISIBLE);
		spinnerContainer.animate().alpha(1f).setDuration(200L).start();

		// User did not change the time
		if (timePickerText.getText().toString().equals("12:00 PM")) {
			calendar.set(Calendar.HOUR_OF_DAY, 12);
			calendar.set(Calendar.MINUTE, 0);
		}

		ArrayList<EventRequest.Participant> participants = new ArrayList<>();
		participants.add(EventRequest.Participant.from(Utility.getUid(getActivity())));

		EventRequest newEvent = new EventRequest(eventName.getText().toString(),
				calendar.getTimeInMillis() / 1000, // Convert to seconds
				eventLocation.getLatLng().latitude,
				eventLocation.getLatLng().longitude,
				participants);

		RestServer.createService(Utility.getAuthToken(getActivity())).createEvent(Utility.getUid(getActivity()), newEvent)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<EventWithParticipantsResponse>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						// Reset the click listener
						fab.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								onFabClick(fab);
							}
						});
						spinnerContainer.setVisibility(View.GONE);
						Snackbar.make(getActivity().findViewById(R.id.container),
								R.string.error_generic_retry,
								Snackbar.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onNext(EventWithParticipantsResponse response) {
						spinnerContainer.setVisibility(View.GONE);
						Snackbar.make(getActivity().findViewById(R.id.container),
								R.string.event_created,
								Snackbar.LENGTH_SHORT)
								.show();

						getActivity().setResult(Activity.RESULT_OK);
						getActivity().finish();
					}
				});
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
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
