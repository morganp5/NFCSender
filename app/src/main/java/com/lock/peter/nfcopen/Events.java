package com.lock.peter.nfcopen;

/**
 * Created by Peter on 26/02/2015.
 */
public class Events {

    //Trigger: Door requires a pin
    //Post-condition: Pin Dialogue Presented in main activity
    public static class PinRequest {
    }

    //Trigger:User Wants To Change Password
    //Post-condition: Update Password Activity Started
    public static class ChangePasswordEvent {
    }

}
