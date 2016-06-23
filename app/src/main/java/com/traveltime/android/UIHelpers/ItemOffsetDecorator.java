package com.traveltime.android.UIHelpers;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.traveltime.android.Utils.Utility;

public class ItemOffsetDecorator extends RecyclerView.ItemDecoration {

	private Context context;

	public ItemOffsetDecorator(Context context) {
		this.context = context;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (parent.getChildLayoutPosition(view) == 0)
			outRect.top = (int) Utility.convertToPixels(this.context, 8);
	}
}
