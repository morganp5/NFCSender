package com.lock.peter.nfcopen;

/**
 * Created by peter on 25/01/15.
 */

import org.json.JSONException;
import org.json.JSONObject;

public class DoorOptions {

    private static final Boolean OPTION_FALSE = false;
    private static final Boolean OPTION_TRUE = true;
    private static final String TAG = "DoorOptions";
    private static Boolean requiresPin = OPTION_FALSE;
    private static String setting = "Open";
    private static int pin = 0;

    public static Boolean isPinRequired() {
        return requiresPin;
    }

    public static int getPin() {
        return pin;
    }

    public static void setPin(int pin) {
        DoorOptions.pin = pin;
        requiresPin = OPTION_TRUE;
    }

    public static void setDefaults() {
        setting = "Open";
    }

    public static void setToggle() {
        setting = "Toggle";
    }

    public static void setNormalize() {
        setting = "Normalize";
    }

    public static String getCurrentSetting() {
        return setting;
    }

    public static String prepareNdefPayload(String user, String sessionToken) {
        JSONObject unlockRequest = new JSONObject();
        try {
            unlockRequest.put("Name", user);
            unlockRequest.put("SessionToken", sessionToken);
            unlockRequest.put("Setting", setting);
            if (isPinRequired()) {
                unlockRequest.put("Pin", getPin());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unlockRequest.toString();
    }
}
