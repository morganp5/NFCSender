package com.lock.peter.nfclock;

import android.app.Application;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ParseApplication extends Application {

    private final String APPLICATION_ID = "OO2FrQNqEfSEOSQUjl2sEvcJS8CtbKcSFsAaROd3";
    private final String CLIENT_KEY = "PIb9iRwlcENfnGAEXrKMrVi6I00Kq2uv6O1o3cXT";
    private List<ParseObject> doors = new ArrayList<>();
    private static ArrayList<String> doorNames = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        Log.i("parse init", "parse initialized ");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}