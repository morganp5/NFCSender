package com.lock.peter.nfcopen;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ParseApplication extends Application {


    private final static String APPLICATIONID = "OO2FrQNqEfSEOSQUjl2sEvcJS8CtbKcSFsAaROd3";
    private final static String CLIENTKEY = "PIb9iRwlcENfnGAEXrKMrVi6I00Kq2uv6O1o3cXT";


    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, APPLICATIONID, CLIENTKEY);
        Log.i("parse init", "parse initialized ");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}