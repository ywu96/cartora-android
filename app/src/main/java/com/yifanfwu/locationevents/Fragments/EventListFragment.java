package com.yifanfwu.locationevents.Fragments;


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
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yifanfwu.locationevents.Models.EventResponse;
import com.yifanfwu.locationevents.R;
import com.yifanfwu.locationevents.Rest.RestServer;
import com.yifanfwu.locationevents.UIHelpers.EventListAdapter;
import com.yifanfwu.locationevents.Utils.Utility;

import java.util.ArrayList;

public class EventListFragment extends Fragment {

	private FloatingActionButton fab;
	private TextView noMeshesText;
	private RecyclerView listRecyclerView;
	private ItemTouchHelper itemTouchHelper;

	private ProgressBar spinner;

	private ArrayList<EventResponse> eventList;

	public EventListFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
				EventResponse event = eventList.get(viewHolder.getAdapterPosition());
				RestServer.getInstance().deleteEvent(event.getId(), new RestServer.Callback<EventResponse>() {
					@Override
					public void result(EventResponse result) {
						Toast.makeText(getActivity(), "Event deleted", Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
		this.itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

		this.spinner = (ProgressBar) rootView.findViewById(R.id.spinner);
		this.noMeshesText = (TextView) rootView.findViewById(R.id.no_meshes_text);

		this.eventList = new ArrayList<>();

		this.listRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recyclerview);
		this.listRecyclerView.setAdapter(new EventListAdapter(this.eventList, R.layout.event_list_item));
		this.listRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		this.listRecyclerView.setItemAnimator(new DefaultItemAnimator());
		this.itemTouchHelper.attachToRecyclerView(this.listRecyclerView);

		RestServer.getInstance().getEvents(Utility.getUid(getActivity().getApplicationContext()), new RestServer.Callback<ArrayList<EventResponse>>() {
			@Override
			public void result(ArrayList<EventResponse> result) {
				spinner.setVisibility(View.GONE);
				if (result != null && result.size() != 0) {
					eventList.clear();
					eventList.addAll(result);
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

}
