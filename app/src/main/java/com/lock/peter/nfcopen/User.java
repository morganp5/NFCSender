package com.lock.peter.nfcopen;

import com.parse.ParseException;
import com.parse.ParseUser;


public class User {

    private static ParseUser currentUser = ParseUser.getCurrentUser();

    public static String getCurrentUser() {
        String username = "";
        if (currentUser.getUsername() != null) {
            currentUser = com.parse.ParseUser.getCurrentUser();
            username = currentUser.getUsername().toString();
        }
        return username;
    }

    public static String getSessionToken(){
        String st = "";
        if (currentUser.getUsername() != null) {
            currentUser = com.parse.ParseUser.getCurrentUser();
            st = currentUser.getSessionToken();
        }
        return st;
    }

    public static boolean updatePassword(String currentPassword, String newPassword) throws ParseException {
        ParseUser user = ParseUser.logIn(currentUser.getUsername(), currentPassword);
        if (user != null) {
            currentUser.setPassword(newPassword);
            currentUser.saveInBackground();
            return true;
        } else {
            return false;
        }
    }

    public static void logout() {
        currentUser.logOut();
    }
}
