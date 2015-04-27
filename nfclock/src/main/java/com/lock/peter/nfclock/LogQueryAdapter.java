package com.lock.peter.nfclock;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LogQueryAdapter extends ParseQueryAdapter<ParseObject> {

    @InjectView(R.id.text1)
    TextView titleTextView;

    @InjectView(R.id.timestamp)
    TextView timestampView;

    public LogQueryAdapter(Context context,final String doorId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("DoorLog");
                query.orderByDescending("createdAt");
                query.whereEqualTo("DoorName", doorId);
                return query;
            }
        });
    }

    public LogQueryAdapter(Context context,final String doorId,final String username) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("DoorLog");
                query.orderByDescending("createdAt");
                query.whereEqualTo("DoorName", doorId);
                query.whereEqualTo("UserName", username);
                return query;
            }
        });
    }
    public LogQueryAdapter(Context context,final String doorId,final Boolean allowed) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("DoorLog");
                query.orderByDescending("createdAt");
                query.whereEqualTo("DoorName", doorId);
                query.whereEqualTo("AccessGranted", allowed);
                return query;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.log_entry, null);
        }
        super.getItemView(object, v, parent);
        ButterKnife.inject(this,v);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(object.getCreatedAt());
        // Add the title view
        titleTextView.setText(object.getString("UserName"));
        if(object.getBoolean("AccessGranted")){
            v.setBackgroundColor(Color.GREEN);
        }
        timestampView.setText(formattedDate);
        return v;
    }

}