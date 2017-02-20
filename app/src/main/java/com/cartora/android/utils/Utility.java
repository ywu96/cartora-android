package com.cartora.android.utils;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Property;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Utility {

	public static float convertToPixels(Context context, int valueDp) {
		return TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, valueDp, context.getResources().getDisplayMetrics());
	}

	public static int getUid(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Strings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(Strings.SHARED_PREF_ID_KEY, -1);
	}

	public static String getAuthToken(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Strings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		return preferences.getString(Strings.SHARED_PREF_AUTH_TOKEN_KEY, null);
	}

	public static void animateMarker(Marker marker, LatLng finalPosition, long duration, final LatLngInterpolator latLngInterpolator) {
		TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
			@Override
			public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
				return latLngInterpolator.interpolate(fraction, startValue, endValue);
			}
		};
		Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
		ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
		animator.setDuration(duration);
		animator.start();
	}
}
