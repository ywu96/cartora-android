package com.yifanfwu.locationevents.Utils;

import android.content.Context;
import android.util.TypedValue;

public class Utility {

	public static float convertToPixels(Context context, int valueDp) {
		return TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, valueDp, context.getResources().getDisplayMetrics());
	}
}
