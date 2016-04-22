package com.yifanfwu.locationevents.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifanfwu.locationevents.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventCreateInfoFragment extends Fragment {


	public EventCreateInfoFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_event_create_info, container, false);
	}

}
