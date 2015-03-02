package com.lock.peter.nfcopen;

/**
 * Created by peter on 25/01/15.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;

public class DoorOptions {

    private static final Boolean OPTION_FALSE = false;
    private static final Boolean OPTION_TRUE = true;
    private static final String TAG = "DoorOptions";
    private static Boolean pinSet = OPTION_FALSE;
    private static String setting = "Open" ;
    private static int pin = 0;

    public static Boolean isPinSet() {
        return pinSet;
    }

    public static int getPin() {
        return pin;
    }

    public static void setPin(int pin) {
        DoorOptions.pin = pin;
        pinSet = OPTION_TRUE;
    }

    public static void setDefaults() {
            setting = "Open";
    }

    public static void setToggle() {
            setting = "Toggle";
    }

    public static void setNormalise() {
            setting = "Normalise";

    }

    public static String prepareNdefPayload(String user, String sessionToken) {
        JSONObject object = new JSONObject();
        try {
            object.put("Name", user);
            object.put("SessionToken", sessionToken);
            object.put("Setting", setting);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
