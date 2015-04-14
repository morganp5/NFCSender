package com.lock.peter.nfclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * Created by peter on 25/03/15.
 */

public class ViewLogActivity extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    @InjectView(R.id.list)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_log);
        ButterKnife.inject(this);

        // Initialize main ParseQueryAdapter
        Intent intent = getIntent();
        String doorId = intent.getStringExtra("doorName");
        mainAdapter = new LogQueryAdapter(this,doorId);

        // Initialize ListView and set initial view to mainAdapter
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();
    }

}

