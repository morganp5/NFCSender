package com.lock.peter.nfclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewLogActivity extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    String doorId;
    @InjectView(R.id.list)
    ListView listView;

    public ParseQueryAdapter getParseQueryAdapter(){
        return mainAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_log);
        ButterKnife.inject(this);

        // Initialize main ParseQueryAdapter
        Intent intent = getIntent();
        doorId = intent.getStringExtra("doorName");
        mainAdapter = new LogQueryAdapter(this, doorId);

        // Initialize ListView and set initial view to mainAdapter
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Create an option menu from res/menu/items.xml */
        getMenuInflater().inflate(R.menu.log_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.allLogs:
                mainAdapter = new LogQueryAdapter(this, doorId);
                break;
            case R.id.accessGranted:
                mainAdapter = new LogQueryAdapter(ViewLogActivity.this, doorId, true);
                break;

            case R.id.accessDenied:
                mainAdapter = new LogQueryAdapter(ViewLogActivity.this, doorId, false);
                break;
            case R.id.search:
                /** Get the action view of the menu item whose id is search */
                View v = (View) item.getActionView();
                /** Get the edit text from the action view */
                EditText txtSearch = (EditText) v.findViewById(R.id.log_search);
                /** Setting an action listener */
                txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        Log.d("TOG", v.getText().toString());
                        mainAdapter = new LogQueryAdapter(ViewLogActivity.this, doorId, v.getText().toString());
                        listView.setAdapter(mainAdapter);
                        mainAdapter.loadObjects();
                        return true;
                    }
                });

                break;

        }
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();
        return true;
    }

}

