package com.cartora.android.managers;

import com.cartora.android.models.LocationLatLng;
import com.cartora.android.models.LocationUpdateResponse;
import com.cartora.android.models.UserLocation;
import com.cartora.android.utils.LatLngInterpolator;
import com.cartora.android.utils.Utility;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MarkerManager {

	private static final long ANIMATE_DURATION = 3000L;

	private GoogleMap map;
	private HashMap<Integer, LocationLatLng> userLocationMap;
	private HashMap<Integer, Marker> userMarkerMap;

	public MarkerManager(GoogleMap map, LocationUpdateResponse locationUpdateResponse) {
		this.map = map;
		userLocationMap = new HashMap<>();
		userMarkerMap = new HashMap<>();
		updateLocations(locationUpdateResponse);

		for (Integer userId : userLocationMap.keySet()) {
			LocationLatLng latLng = userLocationMap.get(userId);
			MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude));
			userMarkerMap.put(userId, this.map.addMarker(markerOptions));
		}
	}

	public void updateMarkers(LocationUpdateResponse locationUpdateResponse) {
		updateLocations(locationUpdateResponse);

		for (Integer userId : userLocationMap.keySet()) {
			LocationLatLng latLng = userLocationMap.get(userId);
			Utility.animateMarker(userMarkerMap.get(userId),
					new LatLng(latLng.latitude, latLng.longitude),
					ANIMATE_DURATION,
					new LatLngInterpolator.Linear());
		}
	}

	private void updateLocations(LocationUpdateResponse locationUpdateResponse) {
		ArrayList<UserLocation> locations = locationUpdateResponse.locations;
		for (UserLocation userLocation : locations) {
			userLocationMap.put(userLocation.userId, userLocation.location);
		}
	}
}
