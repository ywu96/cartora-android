package com.yifanfwu.locationevents.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;

public class Utility {

	public static float convertToPixels(Context context, int valueDp) {
		return TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, valueDp, context.getResources().getDisplayMetrics());
	}

	public static String getUid(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Strings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		return preferences.getString(Strings.UID_KEY, null);
	}
}
