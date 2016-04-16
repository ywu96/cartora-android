package com.yifanfwu.locationevents.UIHelpers;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yifanfwu.locationevents.Models.EventResponse;
import com.yifanfwu.locationevents.R;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    protected ArrayList<EventResponse> eventList;
    protected int layoutResId;

    public EventListAdapter(ArrayList<EventResponse> eventList, @LayoutRes int layoutResId) {
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
        EventResponse event = this.eventList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventTime.setText(event.getTimeMillis().toString());
    }

    @Override
    public int getItemCount() {
        return this.eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView eventName;
        protected TextView eventTime;

        public ViewHolder(View itemView) {
            super(itemView);
            this.eventName = (TextView) itemView.findViewById(R.id.event_name);
            this.eventTime = (TextView) itemView.findViewById(R.id.event_time);
        }
    }

}
