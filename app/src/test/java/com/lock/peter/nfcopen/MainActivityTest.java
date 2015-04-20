package com.lock.peter.nfcopen;

import android.widget.Button;
import android.widget.EditText;

import com.parse.ui.ParseLoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertNotNull;

/**
 * Created by peter on 20/04/15.
 */
public class MainActivityTest {
    private ParseLoginActivity activity;
    private EditText changeUserPinBtn;


    @Before
    public void setup() throws Exception{
        activity = Robolectric.buildActivity(ParseLoginActivity.class).create().visible().get();
        changeUserPinBtn = (EditText) activity.findViewById(R.id.login_username_input);
    }

    @Test
    public void buttonsNotNull() throws Exception{
        assertNotNull(changeUserPinBtn);
    }

}
