package com.yifanfwu.locationevents.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yifanfwu.locationevents.R;
import com.yifanfwu.locationevents.Rest.RestServer;

public class EventListActivity extends AppCompatActivity {

    protected FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        this.fab = (FloatingActionButton) findViewById(R.id.event_list_fab);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestServer.getInstance().getEvents();
            }
        });
    }
}
