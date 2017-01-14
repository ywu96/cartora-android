package com.cartora.android.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.cartora.android.R;
import com.cartora.android.activities.EventListActivity;
import com.cartora.android.models.EventResponse;
import com.cartora.android.rest.RestServer;
import com.cartora.android.uihelpers.EventListAdapter;
import com.cartora.android.utils.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class EventListFragment extends Fragment {

	private TextView noEventsText;
	private RecyclerView listRecyclerView;
	private EventListAdapter listAdapter;
	private ProgressBar spinner;

	private ArrayList<EventResponse> eventList;

	public EventListFragment() {
	}

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

		listRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recyclerview);
		listRecyclerView.setAdapter(listAdapter);
		listRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		listRecyclerView.setItemAnimator(new DefaultItemAnimator());
		loadList();

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case EventListActivity.NEW_EVENT_REQUEST:
				if (resultCode == Activity.RESULT_OK) {
					loadList();
				}
		}
	}

	private void loadList() {
		RestServer.createService(Utility.getAuthToken(getActivity())).getEvents(Utility.getUid(getActivity()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<ArrayList<EventResponse>>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						Toast.makeText(getActivity(), R.string.error_generic_retry, Toast.LENGTH_SHORT).show();
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
								listAdapter.notifyDataSetChanged();
							} else {
								listRecyclerView.setVisibility(View.GONE);
								noEventsText.setVisibility(View.VISIBLE);
							}
						}
					}
				});
	}

	public class EventComparator implements Comparator<EventResponse> {
		@Override
		public int compare(EventResponse lhs, EventResponse rhs) {
			long diff = lhs.startTime - rhs.startTime;
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
