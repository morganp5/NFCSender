package com.lock.peter.nfcopen;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import org.robolectric.*;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import junit.framework.Assert;

import org.robolectric.shadows.ShadowActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21 ,manifest = "./src/main/AndroidManifest.xml")

public class MainMenuTest {

    private Button unlockDoorBtn;
    private Button toggleDoorBtn;
    private Button normaliseDoorBtn;
    private Button changeUserPinBtn;
    private MainMenu activity;
    private ParseApplication PA;


    @Before
    public void setup() throws Exception{
        activity = Robolectric.buildActivity(MainMenu.class).create().visible().get();
        unlockDoorBtn = (Button) activity.findViewById(R.id.unlockDoor);
        toggleDoorBtn = (Button) activity.findViewById(R.id.toggleDoor);
        normaliseDoorBtn = (Button) activity.findViewById(R.id.normaliseDoor);
        changeUserPinBtn = (Button) activity.findViewById(R.id.changeUserPin);

    }
    @Test
    public void buttonsNotNull() throws Exception{
        assertNotNull(unlockDoorBtn);
        assertNotNull(toggleDoorBtn);
        assertNotNull(normaliseDoorBtn);
        assertNotNull(changeUserPinBtn);
    }

    @Test
    public void testSomething() throws Exception {
        // test
    }  private ShadowActivity shadowActivity;

    @Test
    public void buttonClickShouldStartNewActivity() throws Exception
    {
        Button button = (Button) activity.findViewById(R.id.changeUserPin);
        button.performClick();
      //  Resources res = getResources();

       // Drawable x =
       // assertThat(button.getBackground()).isEqualTo(R.drawable.unlockdoor);
//        Intent intent = shadowActivity.peekNextStartedActivityForResult().intent;
  //      assertThat(intent.getComponent()).isEqualTo(new ComponentName(activity, UpdatePassword.class));
    }
}
