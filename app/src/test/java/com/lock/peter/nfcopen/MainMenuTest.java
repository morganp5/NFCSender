package com.lock.peter.nfcopen;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert.*;
import org.robolectric.*;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import junit.framework.Assert;

import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowIntent;



@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21 ,manifest = "./src/main/AndroidManifest.xml")

public class MainMenuTest {

    private Button unlockDoorBtn;
    private Button toggleDoorBtn;
    private Button normalizeDoorBtn;
    private Button changeUserPinBtn;
    private MainMenu activity;


    @Before
    public void setup() throws Exception{
        activity = Robolectric.buildActivity(MainMenu.class).create().visible().get();
        unlockDoorBtn = (Button) activity.findViewById(R.id.unlockDoor);
        toggleDoorBtn = (Button) activity.findViewById(R.id.toggleDoor);
        normalizeDoorBtn = (Button) activity.findViewById(R.id.normalizeDoor);
        changeUserPinBtn = (Button) activity.findViewById(R.id.changeUserPin);

    }
   @Test
    public void buttonsNotNull() throws Exception{
        assertNotNull(unlockDoorBtn);
        assertNotNull(toggleDoorBtn);
        assertNotNull(normalizeDoorBtn);
        assertNotNull(changeUserPinBtn);
    }

    @Test
    public void checkUpdatePasswordSwap() throws Exception
    {
        MainMenuFragment mainMenuFragment = (MainMenuFragment) activity.getFragmentManager().findFragmentByTag("Menu");
        assertNotNull("Menu fragment not found", mainMenuFragment);
        changeUserPinBtn.performClick();
        UpdatePassword updateFragment = (UpdatePassword) activity.getFragmentManager().findFragmentByTag("updatePassword");
        String y =  activity.getFragmentManager().toString();
        assertNotNull("Password fragment not found" , updateFragment);
    }
    @Test
    public void checkLogout()
    {
        MenuItem x = new RoboMenuItem(R.id.logout);
        activity.onOptionsItemSelected(x);
        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.peekNextStartedActivityForResult().intent;
        ShadowIntent intent = shadowOf(startedIntent);
        assertThat(intent.getComponent()).isEqualTo(new ComponentName(activity, MainActivity.class));
    }

    @Test
    public void checkPasswordFromMenu() throws Exception
    {
        MenuItem x = new RoboMenuItem(R.id.changePassword);
        activity.onOptionsItemSelected(x);
        checkUpdatePasswordSwap();
    }

    @Test
    public void alertDialogueShown(){
        Events.PinRequest pinRequest = new Events.PinRequest();
        activity.onEvent(pinRequest);
        AlertDialog alert =
                ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog sAlert = shadowOf(alert);
        TextView alertDialogueTV = (TextView)sAlert.getView().findViewById(R.id.textView1);
        assertEquals(alertDialogueTV.getText().toString() ,(activity.getString(R.string.EnterPinPrompt)));
    }





}
