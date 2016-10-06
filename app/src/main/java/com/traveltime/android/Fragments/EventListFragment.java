package com.traveltime.android.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.traveltime.android.models.EventResponse;
import com.traveltime.android.R;
import com.traveltime.android.rest.RestServer;
import com.traveltime.android.uihelpers.EventListAdapter;
import com.traveltime.android.utils.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class EventListFragment extends Fragment {

	private TextView noEventsText;
	private RecyclerView listRecyclerView;
	private EventListAdapter listAdapter;
	private ItemTouchHelper itemTouchHelper;

	private ProgressBar spinner;

	private ArrayList<EventResponse> eventList;

	public EventListFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventList = new ArrayList<>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

		spinner = (ProgressBar) rootView.findViewById(R.id.spinner);
		noEventsText = (TextView) rootView.findViewById(R.id.no_events_text);

		listAdapter = new EventListAdapter(getActivity(), eventList, R.layout.event_list_item);

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
				final int listIndex = viewHolder.getLayoutPosition();
				EventResponse event = eventList.get(listIndex);
				RestServer.getInstance().deleteEvent(event.getId(), new RestServer.Callback<EventResponse>() {
					@Override
					public void result(EventResponse result) {
						if (!isDetached()) {
							eventList.remove(listIndex);
							listAdapter.notifyItemRemoved(listIndex);
							Toast.makeText(getActivity(), "Event deleted", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		};
		itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

		listRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recyclerview);
		listRecyclerView.setAdapter(listAdapter);
		listRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		listRecyclerView.setItemAnimator(new DefaultItemAnimator());
		itemTouchHelper.attachToRecyclerView(listRecyclerView);

		RestServer.getInstance().getEvents(Utility.getUid(getActivity()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<ArrayList<EventResponse>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(ArrayList<EventResponse> events) {
						if (!isDetached()) {
							spinner.setVisibility(View.GONE);
							if (events != null && events.size() != 0) {
								eventList.clear();
								eventList.addAll(events);
								Collections.sort(eventList, new EventComparator());
								noEventsText.setVisibility(View.GONE);
								listRecyclerView.setVisibility(View.VISIBLE);
							} else {
								listRecyclerView.setVisibility(View.GONE);
								noEventsText.setVisibility(View.VISIBLE);
							}
						}
					}
				});
		return rootView;
	}

	public class EventComparator implements Comparator<EventResponse> {
		@Override
		public int compare(EventResponse lhs, EventResponse rhs) {
			long diff = lhs.getTimeSecs() - rhs.getTimeSecs();
			if (diff < 0) {
				return -1;
			} else if (diff > 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
