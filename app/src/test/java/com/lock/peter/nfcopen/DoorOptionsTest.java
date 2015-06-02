package com.lock.peter.nfcopen;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

/**
 * Created by peter on 20/04/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21 ,manifest = "./src/main/AndroidManifest.xml")

public class DoorOptionsTest {
    DoorOptions doorOptions = new DoorOptions();

    @Test
    public void checkPinSettings(){
        doorOptions.setPin(1000);
        int pin = doorOptions.getPin();
        assertEquals(pin, 1000);
    }

    @Test
    public void testDoorSettings(){
        String currentSetting = doorOptions.getCurrentSetting();
        assertEquals(currentSetting,"Open");
        doorOptions.setToggle();
        currentSetting = doorOptions.getCurrentSetting();
        assertEquals(currentSetting, "Toggle");
        doorOptions.setNormalize();
        currentSetting = doorOptions.getCurrentSetting();
        assertEquals(currentSetting,"Normalize");
        doorOptions.setDefaults();
        currentSetting = doorOptions.getCurrentSetting();
        assertEquals(currentSetting, "Open");
    }


    @Test
    public void testNdefMessage() throws JSONException{
        DoorOptions.setPin(1234);
        String unlockRequest= doorOptions.prepareNdefPayload("Peter", "11111111");
        JSONObject jsonUnlockRequest = new JSONObject(unlockRequest);
        String userName = jsonUnlockRequest.getString("Name");
        assertEquals(userName,"Peter");
        String sesionToken = jsonUnlockRequest.getString("SessionToken");
        assertEquals(sesionToken,"11111111");
        String Setting = jsonUnlockRequest.getString("Setting");
        assertEquals(Setting,"Open");
        int pin = jsonUnlockRequest.getInt("Pin");
        assertEquals(pin,1234);

    }
    
}
