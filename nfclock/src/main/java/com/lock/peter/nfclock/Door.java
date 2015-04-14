package com.lock.peter.nfclock;

import android.util.Log;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.FindCallback;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 29/01/15.
 */


public class Door {

    private boolean requiresPin = false;
    private final String TAG = "DOOR";
    private String doorName = "";
    private final ArrayList<String> groupUsers = new ArrayList<>();
    private ParseRole doorRole;
    private String doorMessage;

    //Constructor pulls door object from parse and sets up values
    //TODO Change Contructor to add new door to parse add seperate method for pulling existing doors
    public Door(String ID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Door");
        query.whereEqualTo("objectId", ID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                       @Override
                                       public void done(ParseObject door, ParseException e) {
                                           if (e == null) {
                                               setRequiresPin(door.getBoolean("requiresPin"));
                                               setDoorName(door.getString("DoorName"));
                                               Log.i(TAG, door.getString("DoorName"));
                                               getAllowedUsers();
                                           } else {
                                               Log.e(TAG, "Error Getting Door");
                                           }
                                       }
                                   }

        );
    }

    public void addAllowedUser(String sessionToken){
        ParseUser user = new ParseUser();
        user.becomeInBackground(sessionToken,new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    Log.i(TAG, "The sesion token user is " + user.getUsername());
                    doorRole.getUsers().add(user);
                    doorRole.saveInBackground();
                    groupUsers.add(user.getUsername());
                } else if (user == null) {
                    Log.i(TAG,"Login Failed User Is Null");
                } else {
                    Log.i(TAG,"Login Failed ");
                }
            }
        });
    }

    private void getAllowedUsers() {
        ParseQuery<ParseRole> query2 = ParseRole.getQuery();
        Log.i(TAG, getDoorName());
        query2.whereEqualTo("name", getDoorName());
        query2.findInBackground(new FindCallback<ParseRole>() {
            @Override
            public void done(List<ParseRole> objects, ParseException e) {
                if (e == null) {
                    for (ParseRole role : objects) {
                        doorRole = role ;
                        ParseRelation<ParseUser> usersRelation = role.getRelation("users");
                        ParseQuery<ParseUser> usersQuery = usersRelation.getQuery();
                        usersQuery.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                for (ParseUser user : objects) {
                                    groupUsers.add(user.getUsername());
                                    Log.i(TAG, user.getUsername());
                                }
                            }
                        });
                    }
                } else {
                    Log.i(TAG, "Query Failed");
                }
            }
        });
    }

    public boolean checkIfAuthorised(JSONObject accessCredentials) throws JSONException{
        boolean accessGranted = false;
        String user = accessCredentials.getString("Name");
        if(groupUsers.contains(user)){
            accessGranted = true;
        }
        logAccessAttempt(user,accessGranted);
        Log.i(TAG,"User Authorized" + String.valueOf(accessGranted));
        return accessGranted;
    }

    public void logAccessAttempt(String userName,boolean accessGranted){
        ParseObject DoorLog = new ParseObject("DoorLog");
        DoorLog.put("DoorName", getDoorName());
        DoorLog.put("UserName", userName);
        DoorLog.put("AccessGranted", accessGranted);
        DoorLog.saveInBackground();
    }

    private boolean doorOpen = false;

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public String getDoorMessage(String setting) {
        if(setting.equals("Toggle"))
        {
            setDoorOpen(true);
            setDoorMessage("Door is Toggled Opened");
        }
        else {
            setDoorOpen(false);
            setDoorMessage("Door is Locked");
        }
        return doorMessage;
    }

    private void setDoorMessage(String doorMessage) {
        this.doorMessage = doorMessage;
    }

    public boolean isRequiresPin() {
        return requiresPin;
    }

    public void setRequiresPin(boolean requiresPin) {
        this.requiresPin = requiresPin;
    }
}



