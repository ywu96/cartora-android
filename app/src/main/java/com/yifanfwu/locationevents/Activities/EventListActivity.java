package com.yifanfwu.locationevents.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.client.Firebase;
import com.yifanfwu.locationevents.Models.EventRequest;
import com.yifanfwu.locationevents.Models.EventResponse;
import com.yifanfwu.locationevents.Models.EventUserRequest;
import com.yifanfwu.locationevents.R;
import com.yifanfwu.locationevents.Rest.RestServer;
import com.yifanfwu.locationevents.UIHelpers.EventListAdapter;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    protected Firebase firebaseRef;
    protected String userId;

    protected FloatingActionButton fab;
    protected RecyclerView listRecyclerView;
    protected ArrayList<EventResponse> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_event_list);

        this.firebaseRef = new Firebase("https://vivid-inferno-3846.firebaseio.com");
        this.userId = this.firebaseRef.getAuth().getUid();

        this.eventList = new ArrayList<>();

        this.listRecyclerView = (RecyclerView) findViewById(R.id.event_list_recyclerview);
        RestServer.getInstance().getEvents(new RestServer.Callback<ArrayList<EventResponse>>() {
            @Override
            public void result(ArrayList<EventResponse> result) {
                eventList = result;
                listRecyclerView.setAdapter(new EventListAdapter(eventList, R.layout.event_list_item));
                listRecyclerView.setLayoutManager(new LinearLayoutManager(getParent()));
                listRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });


        this.fab = (FloatingActionButton) findViewById(R.id.event_list_fab);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<EventUserRequest> userList = new ArrayList<>();
                userList.add(new EventUserRequest(firebaseRef.getAuth().getUid()));

                EventRequest eventRequest = new EventRequest("Walk on the Beach", userList, 35.0, 128.4, 1234567890);
                RestServer.getInstance().createEvent(eventRequest, new RestServer.Callback<EventResponse>() {
                    @Override
                    public void result(EventResponse result) {
                        eventList.add(result);
                        listRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        });
    }


}
