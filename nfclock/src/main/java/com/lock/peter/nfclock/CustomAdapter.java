package com.lock.peter.nfclock;

/**
 * Created by peter on 08/02/15.
 */
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

    @Override
    public int getViewTypeCount(){
        return 1;
    }

    public CustomAdapter(Context context) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                //ParseRelation<ParseUser> relation = user.getRelation("friends");
               // ParseQuery friendsQuery = relation.getQuery();

//                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Post");
 //               query.whereMatchesQuery("CreatedBy", friendsQuery);
  //              query.addDescendingOrder("createdAt");

                ParseQuery query = new ParseQuery("Door");

                return query;
            }
        });
    }

 /*   public String getDoor(String doorName, int doorPin){
        String output="";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Door");
        query.whereEqualTo("DoorName", doorName);
        query.whereEqualTo("Pin", doorPin);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                       @Override
                                       public void done(ParseObject Door, ParseException e) {
                                           if (e == null) {
                                               List<ParseObject> users = Door.getList("Users");
                                               /*if(containsUser(users,ParseUser.getCurrentUser())){
                                                   Log.d("Query", "Contains users");
                                               }
                                               Log.d("Query2", "^Should say contains");
                                           } else {
                                               // something went wrong
                                           }
                                               String output="";
                                            //   for (ParseUser user : users) {
                                            //       output+= user.getUsername() + ",";
                                               }
                                               }
                                       }
                                   }

        );
        return output;



    } */
}