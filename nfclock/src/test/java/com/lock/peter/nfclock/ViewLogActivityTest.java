package com.lock.peter.nfclock;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowListView;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, emulateSdk = 21, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricGradleTestRunner.class)
public class ViewLogActivityTest {


    private Button btnLaunch;
    private EditText etResult;
    private ViewLogActivity activity;
    private ListView listView;

    @Before
    public void setup() throws Exception {
        Intent intent = new Intent(RuntimeEnvironment.application.getApplicationContext(), ViewLogActivity.class);
        intent.putExtra("doorName", "Test");
        activity = Robolectric.buildActivity(ViewLogActivity.class).create().withIntent(intent).visible().get();
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void checkListView() throws Exception {
        listView = (ListView) activity.findViewById(R.id.list);
        ShadowListView shadowListView = shadowOf(listView);
        shadowListView.populateItems();
        assertNotNull(shadowListView);
    }

    @Test
    public void checkAuthorizedLogs() throws Exception {

        MenuItem item = new RoboMenuItem(R.id.accessGranted);
        activity.onOptionsItemSelected(item);
        listView = (ListView) activity.findViewById(R.id.list);
        ShadowListView shadowListView = shadowOf(listView);
        shadowListView.populateItems();
        assertNotNull(shadowListView);
    }

    @Test
    public void checkDeniedLogs() throws Exception {
        MenuItem item = new RoboMenuItem(R.id.accessDenied);
        activity.onOptionsItemSelected(item);
        listView = (ListView) activity.findViewById(R.id.list);
        ShadowListView shadowListView = shadowOf(listView);
        shadowListView.populateItems();
        assertNotNull(shadowListView);
    }

}
