package com.traveltime.android.UIHelpers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.traveltime.android.Activities.LocationActivity;
import com.traveltime.android.Models.EventResponse;
import com.traveltime.android.Models.LocationUpdateRequest;
import com.traveltime.android.R;
import com.traveltime.android.Rest.RestServer;
import com.traveltime.android.Utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

	private Context context;
	private ArrayList<EventResponse> eventList;
	private int layoutResId;

	public EventListAdapter(Context context, ArrayList<EventResponse> eventList, @LayoutRes int layoutResId) {
		this.context = context;
		this.eventList = eventList;
		this.layoutResId = layoutResId;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutResId, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final EventResponse event = this.eventList.get(position);
		holder.eventName.setText(event.getEventName());
		Date date = new Date(event.getTimeSecs()*1000L);
		SimpleDateFormat format = new SimpleDateFormat("E, MMM d 'at' h:mm a");
		holder.eventTime.setText(format.format(date));

		holder.card.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LocationActivity.class);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return this.eventList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private CardView card;
		private TextView eventName;
		private TextView eventTime;

		public ViewHolder(View itemView) {
			super(itemView);
			this.card = (CardView) itemView;
			this.eventName = (TextView) itemView.findViewById(R.id.event_name);
			this.eventTime = (TextView) itemView.findViewById(R.id.event_time);
		}
	}

}
