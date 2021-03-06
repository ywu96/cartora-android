package com.cartora.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cartora.android.R;
import com.cartora.android.activities.EventDetailActivity;
import com.cartora.android.models.EventResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

	public static final String EVENT_LIST_NAME_KEY = "event_list_name";
	public static final String EVENT_LIST_ID_KEY = "event_list_id";

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
		View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final EventResponse event = eventList.get(position);
		holder.eventName.setText(event.name);
		Date date = new Date(event.startTime * 1000L);
		SimpleDateFormat format = new SimpleDateFormat("E, MMM d 'at' h:mm a");
		holder.eventTime.setText(format.format(date));

		holder.container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt(EVENT_LIST_ID_KEY, Integer.valueOf(event.id));
				Intent intent = new Intent(context, EventDetailActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return eventList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private FrameLayout container;
		private TextView eventName;
		private TextView eventTime;

		public ViewHolder(View itemView) {
			super(itemView);
			container = (FrameLayout) itemView.findViewById(R.id.event_list_card);
			eventName = (TextView) itemView.findViewById(R.id.event_name);
			eventTime = (TextView) itemView.findViewById(R.id.event_time);
		}
	}

}
