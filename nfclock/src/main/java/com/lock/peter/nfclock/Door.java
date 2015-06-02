package com.lock.peter.nfclock;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Door {

    private boolean pinRequired = false;
    private int doorPin;
    private static final String TAG = "DOOR";
    private String doorName = "";
    private final List<String> groupUsers = new ArrayList<>();
    private ParseRole doorRole;

    //Constructor pulls door object from parse and sets up values
    public Door(String ID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Door");
        query.whereEqualTo("DoorName", ID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                       @Override
                                       public void done(ParseObject door, ParseException e) {
                                           if (e == null) {
                                               setPinRequired(door.getBoolean("requiresPin"));
                                               setDoorName(door.getString("DoorName"));
                                               Log.i(TAG, door.getString("DoorName"));
                                               getAllowedUsers();
                                               if (pinRequired) {
                                                   doorPin = door.getInt("Pin");
                                               }
                                           } else {
                                               Log.e(TAG, "Error Getting Door");
                                           }
                                       }
                                   }

        );
    }

    protected void addAllowedUser(String sessionToken) {
        ParseUser user = new ParseUser();
        user.becomeInBackground(sessionToken, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    Log.i(TAG, "The sesion token user is " + doorRole.getName() + user.getUsername());
                    doorRole.getUsers().add(user);
                    try {
                        doorRole.save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    Log.i(TAG, "Saved");
                    groupUsers.add(user.getUsername());
                } else if (user == null) {
                    Log.i(TAG, "Login Failed User Is Null");
                } else {
                    Log.i(TAG, "Login Failed ");
                }
            }
        });
    }

    private void getAllowedUsers() {
        ParseQuery<ParseRole> query = ParseRole.getQuery();
        Log.i(TAG, getDoorName());

        query.whereEqualTo("name", getDoorName());
        query.findInBackground(new FindCallback<ParseRole>() {
            @Override
            public void done(List<ParseRole> objects, ParseException e) {
                if (e == null) {
                    ParseACL acl = new ParseACL();
                    acl.setPublicWriteAccess(true);
                    acl.setPublicReadAccess(true);
                    for (ParseRole role : objects) {
                        doorRole = role;
                        doorRole.setACL(acl);
                        Log.i(TAG,doorRole.getName());
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

    public boolean checkIfAuthorised(JSONObject accessCredentials) throws JSONException {
        boolean accessGranted = false;
        String user = accessCredentials.getString("Name");
        if (groupUsers.contains(user)) {
            accessGranted = true;
        }
        if (pinRequired) {
            int transmittedPin = accessCredentials.getInt("Pin");
            if(transmittedPin != doorPin){
                accessGranted = false;
            }
        }
        logAccessAttempt(user, accessGranted);
        Log.i(TAG, "User Authorized " + String.valueOf(accessGranted));
        return accessGranted;
    }

    public void logAccessAttempt(String userName, boolean accessGranted) {
        ParseObject DoorLog = new ParseObject("DoorLog");
        DoorLog.put("DoorName", getDoorName());
        DoorLog.put("UserName", userName);
        DoorLog.put("AccessGranted", accessGranted);
        DoorLog.saveInBackground();
    }


    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }
    public boolean isPinRequired() {
        return pinRequired;
    }

    public void setPinRequired(boolean pinRequired) {
        this.pinRequired = pinRequired;
    }
}



