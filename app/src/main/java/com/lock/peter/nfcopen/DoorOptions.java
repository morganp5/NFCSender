package com.lock.peter.nfcopen;

/**
 * Created by peter on 25/01/15.
 */

import android.content.Context;
import android.util.Log;

public class DoorOptions {

    private static final Boolean OPTION_FALSE = false;
    private static final Boolean OPTION_TRUE = true;
    private static final String TAG = "DoorOptions";
    private static final Object sAccountLock = new Object();
    private static Boolean toggle = OPTION_FALSE;
    private static Boolean normalise = OPTION_FALSE;
    private static Boolean pinSet = OPTION_FALSE;
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
        synchronized (sAccountLock) {
            Log.i(TAG, "Setting Defaults");
            normalise = OPTION_FALSE;
            toggle = OPTION_FALSE;
        }
    }

    //insert toggle option
    public static void setToggle() {
        synchronized (sAccountLock) {
            Log.i(TAG, "Setting toggle");
            normalise = OPTION_FALSE;
            toggle = OPTION_TRUE;
        }
    }

    public static Boolean getToggle() {
        synchronized (sAccountLock) {
            Log.i(TAG, "Returning toggle: " + toggle);
            return toggle;
        }
    }

    public static void setNormalise() {
        synchronized (sAccountLock) {
            Log.i(TAG, "Setting Normalise");
            toggle = OPTION_FALSE;
            normalise = OPTION_TRUE;
        }
    }

    public static Boolean getNormalise(Context c) {
        synchronized (sAccountLock) {
            return normalise;
        }
    }


    public static String prepareNdefPayload(Context c) {
        if (toggle) {
            return " Toggle:True ";
        } else if (normalise) {
            return " Normalise:True ";
        }
        return "";
    }
}
