package com.lock.peter.nfclock;

import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseConfig;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 29/01/15.
 */


public class Door {

    private boolean requiresPin = false;
    private final String TAG = "DOOR";
    private String doorName = "";
    private ArrayList<ParseUser> users = new ArrayList<ParseUser>();
    private final ArrayList<String> groupUsers = new ArrayList<>();

    //Constructor pulls door object from parse and sets up values
    //TODO Change Contructor to add new door to parse add seperate method for pulling existing doors
    public Door(String ID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Door");
        query.whereEqualTo("objectId", ID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                       @Override
                                       public void done(ParseObject door, ParseException e) {
                                           if (e == null) {
                                               requiresPin = door.getBoolean("requiresPin");
                                               setDoorName(door.getString("DoorName"));
                                               Log.i(TAG, door.getString("DoorName"));
                                               getAllowedUsers();
                                           } else {
                                               Log.e(TAG,"Error Getting Door");
                                           }
                                       }
                                   }

        );
    }

    public void onCreate() {
        Log.i("Door", "Adding to parse");
    }

    public void getAllowedUsers(){
        ParseQuery<ParseRole> query2 = ParseRole.getQuery();
        Log.i(TAG,getDoorName());
        query2.whereEqualTo("name", getDoorName());
        query2.findInBackground(new FindCallback<ParseRole>() {
            @Override
            public void done(List<ParseRole> objects, ParseException e) {
                if (e == null) {
                    for (ParseRole role : objects) {
                        Log.i(TAG , role.getObjectId());
                        ParseRelation<ParseUser> usersRelation = role.getRelation("users");
                        ParseQuery<ParseUser> usersQuery = usersRelation.getQuery();
                        usersQuery.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                for (ParseUser user : objects) {
                                    groupUsers.add(user.getUsername());
                                    Log.i(TAG , user.getUsername());
                                }
                            }
                        });
                    }
                } else {
                    Log.i(TAG , "Query Failed");
                }
            }
        });
    }
     public boolean checkIfAuthorised(String message){
         //TODO Have NFC message sent as json to parse either
         boolean allowed = false;
         for (String user : groupUsers) {
             Log.i(TAG, user);
             if (message.contains(user)) {
                 allowed = true;
             }
         }
         Log.i(TAG, String.valueOf(allowed));
         return allowed;
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

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

}
