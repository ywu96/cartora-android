package com.traveltime.android.Fragments;


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

import com.traveltime.android.Models.EventResponse;
import com.traveltime.android.R;
import com.traveltime.android.Rest.RestServer;
import com.traveltime.android.UIHelpers.EventListAdapter;
import com.traveltime.android.UIHelpers.ItemOffsetDecorator;
import com.traveltime.android.Utils.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventListFragment extends Fragment {

	private TextView noMeshesText;
	private RecyclerView listRecyclerView;
	private EventListAdapter listAdapter;
	private ItemTouchHelper itemTouchHelper;

	private ProgressBar spinner;

	private ArrayList<EventResponse> eventList;

	public EventListFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.eventList = new ArrayList<>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

		this.spinner = (ProgressBar) rootView.findViewById(R.id.spinner);
		this.noMeshesText = (TextView) rootView.findViewById(R.id.no_meshes_text);

		this.listAdapter = new EventListAdapter(this.eventList, R.layout.event_list_item);

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
						eventList.remove(listIndex);
						listAdapter.notifyItemRemoved(listIndex);
						Toast.makeText(getActivity(), "Event deleted", Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
		this.itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

		this.listRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recyclerview);
		this.listRecyclerView.setAdapter(this.listAdapter);
		this.listRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		this.listRecyclerView.setItemAnimator(new DefaultItemAnimator());
		this.listRecyclerView.addItemDecoration(new ItemOffsetDecorator(getActivity()));
		this.itemTouchHelper.attachToRecyclerView(this.listRecyclerView);

		RestServer.getInstance().getEvents(Utility.getUid(getActivity().getApplicationContext()), new RestServer.Callback<ArrayList<EventResponse>>() {
			@Override
			public void result(ArrayList<EventResponse> result) {
				spinner.setVisibility(View.GONE);
				if (result != null && result.size() != 0) {
					eventList.clear();
					eventList.addAll(result);
					Collections.sort(eventList, new EventComparator());
					noMeshesText.setVisibility(View.GONE);
					listRecyclerView.setVisibility(View.VISIBLE);
				} else {
					listRecyclerView.setVisibility(View.GONE);
					noMeshesText.setVisibility(View.VISIBLE);
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
