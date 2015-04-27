package com.lock.peter.nfclock;

/**
 * Created by peter on 08/02/15.
 */

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

    @Override
    public int getViewTypeCount(){
        return 1;
    }

    public CustomAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Door");
                return query;
            }
        });
    }

}