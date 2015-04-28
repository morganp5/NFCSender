package com.lock.peter.nfcopen;

/**
 * Created by peter on 25/01/15.
 */

import org.json.JSONException;
import org.json.JSONObject;

public class DoorOptions {

    //Current Pin To Be Set
    private static int pin = 0;
    //Has The Door Pin Been Set
    private static Boolean pinSet = false;
    private static String currentSetting = "Open";


    public static int getPin(){
        return pin;
    }
    public static boolean isPinSet(){
        return pinSet;
    }
    public static void setPin(int pin) {
        DoorOptions.pin = pin;
        pinSet = true;
    }

    public static void setDefaults() {
        currentSetting = "Open";
    }

    public static void setToggle() {
        currentSetting = "Toggle";
    }

    public static void setNormalize() {
        currentSetting = "Normalize";
    }


    //Prepares the JSON Message that will be transmitted to the NFCOpen application
    public static String prepareNdefPayload(String user, String sessionToken) {
        JSONObject unlockRequest = new JSONObject();
        try {
            unlockRequest.put("Name", user);
            unlockRequest.put("SessionToken", sessionToken);
            unlockRequest.put("Setting", currentSetting);
            if (pinSet) {
                unlockRequest.put("Pin", getPin());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unlockRequest.toString();
    }
}
