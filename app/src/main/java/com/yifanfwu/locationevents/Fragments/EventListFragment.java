package com.yifanfwu.locationevents.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yifanfwu.locationevents.Models.EventResponse;
import com.yifanfwu.locationevents.R;
import com.yifanfwu.locationevents.Rest.RestServer;
import com.yifanfwu.locationevents.UIHelpers.EventListAdapter;

import java.util.ArrayList;

public class EventListFragment extends Fragment {

	protected FloatingActionButton fab;
	protected TextView noMeshesText;
	protected RecyclerView listRecyclerView;
	protected ProgressBar spinner;

	protected ArrayList<EventResponse> eventList;

	public EventListFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

		this.spinner = (ProgressBar) rootView.findViewById(R.id.spinner);
		this.noMeshesText = (TextView) rootView.findViewById(R.id.no_meshes_text);

		this.eventList = new ArrayList<>();

		this.listRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recyclerview);
		RestServer.getInstance().getEvents(new RestServer.Callback<ArrayList<EventResponse>>() {
			@Override
			public void result(ArrayList<EventResponse> result) {
				spinner.setVisibility(View.GONE);
				if (result.size() != 0) {
					eventList = result;
					noMeshesText.setVisibility(View.GONE);
					listRecyclerView.setAdapter(new EventListAdapter(eventList, R.layout.event_list_item));
					listRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
					listRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
