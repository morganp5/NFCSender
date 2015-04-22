package com.lock.peter.nfclock;

import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by peter on 21/04/15.
 */

@Config(constants = BuildConfig.class, emulateSdk = 21, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricGradleTestRunner.class)
public class AddDoorActivityTest {


    private Button btnLaunch;
    private EditText etResult;
    private AddDoorActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(AddDoorActivity.class).create().visible().get();
        etResult = (EditText) activity.findViewById(R.id.enter_door_name);
        btnLaunch = (Button) activity.findViewById(R.id.create_door);
    }

    @Test
    public void alertDialogueShown() throws Exception {
        String doorName = "Test Door";
        etResult.setText(doorName);
        btnLaunch.performClick();
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo("Door " + doorName + " has been added to Parse"));
    }

}
