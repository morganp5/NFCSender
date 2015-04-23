package com.lock.peter.nfcopen;

/**
 * Created by peter on 25/01/15.
 */

import org.json.JSONException;
import org.json.JSONObject;

public class DoorOptions {

    //Has The Door Pin Been Set
    private static int pin = 0;
    private static Boolean requiresPin = false;
    private static String setting = "Open";

    public static int getPin() {
        return pin;
    }

    public static void setPin(int pin) {
        DoorOptions.pin = pin;
        requiresPin = true;
    }
    public static Boolean isPinSet() {
        return requiresPin;
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

    //Prepares the JSON Message that will be transmitted to the NFCOpen application
    public static String prepareNdefPayload(String user, String sessionToken) {
        JSONObject unlockRequest = new JSONObject();
        try {
            unlockRequest.put("Name", user);
            unlockRequest.put("SessionToken", sessionToken);
            unlockRequest.put("Setting", setting);
            if (isPinSet()) {
                unlockRequest.put("Pin", getPin());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unlockRequest.toString();
    }
}
