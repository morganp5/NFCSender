package com.lock.peter.nfclock;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseConfig;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 29/01/15.
 */


public class Door {

    public Door() {
    }

    public void onCreate() {

        Log.i("Door", "Adding to parse");
        ParseObject Door = new ParseObject("Door");
        ArrayList<ParseUser> users = new ArrayList<ParseUser>();
        ParseUser user;
        try {
            user = ParseUser.logIn("peter", "1");
            Log.i("DOOR", "Logged in as" + ParseUser.getCurrentUser().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
            user = new ParseUser();
            Log.i("DOOR", "Logged in as failed" + ParseUser.getCurrentUser().getUsername());
        }
        users.add(user);
        try {
            user = ParseUser.logIn("Peter", "123456");
            Log.i("DOOR", "Logged in as" + ParseUser.getCurrentUser().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
            user = new ParseUser();
            Log.i("DOOR", "Logged in as failed" + ParseUser.getCurrentUser().getUsername());
        }
        users.add(user);
        Door.put("Pin", 2341);
        Door.addAll("Users", users);
        Door.put("closed", true);
        //Door.saveInBackground();
        Log.i("Door", "added");

        //Checking user pin for door will eventually need Login name aswell for door just a test
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Door");
        query.whereEqualTo("Pin", 234);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                       @Override
                                       public void done(ParseObject Door, ParseException e) {
                                           if (e == null) {
                                               ArrayList<ParseUser> users = (ArrayList<ParseUser>) Door.get("Users");
                                               if(containsUser(users,ParseUser.getCurrentUser())){
                                                   Log.d("Query","Contains users");
                                               }
                                               Log.d("Query2", "^Should say contains");
                                           } else {
                                               // something went wrong
                                           }
                                       }
                                   }

        );
    }
    private boolean containsUser(List<ParseUser> list, ParseUser user) {
        for (ParseUser parseUser : list) {
            if (parseUser.hasSameId(user)) return true;
        }
        return false;

    }

    private boolean doorOpen = false;

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public void toggleDoor() {
        setDoorOpen(true);
    }

    public void normaliseDoor() {
        setDoorOpen(false);
    }

}
