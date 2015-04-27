package com.lock.peter.nfclock;

/**
 * Created by peter on 05/02/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnEditorAction;
import butterknife.OnItemSelected;

public class DoorMenu extends Activity {

    private final static String TAG = "DOORMENU";

    @InjectView(R.id.selectDoor)
    Spinner selectDoor;

    @InjectView(R.id.doorCodeET)
    EditText doorPinET;

    @InjectView(R.id.doorCodeText)
    TextView doorCodeText;

    private Boolean selectDoorInit = false;


    //Parse Adapter For Pulling List Of Doors
    private CustomAdapter mainAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_menu);
        ButterKnife.inject(this);
        pullDoors();
    }

    // add items into spinner dynamically
    private void pullDoors() {
        mainAdapter = new CustomAdapter(this);
        //Display DoorNames in list
        mainAdapter.setTextKey("DoorName");
        mainAdapter.loadObjects();
        Log.d(TAG, "Doors Loaded");
        selectDoor.setAdapter(mainAdapter);
        selectDoor.setPrompt("Select Door");
    }

    @OnItemSelected(R.id.selectDoor)
    public void doorSelected() {
        if (selectDoorInit) {
            doorPinET.setVisibility(View.VISIBLE);
            doorCodeText.setVisibility(View.VISIBLE);

        } else selectDoorInit = !selectDoorInit;
    }

    public void addNewDoor() {
        Intent intent = new Intent(getApplicationContext(), AddDoorActivity.class);
        startActivity(intent);
    }

    public void getDoor() {
        ParseObject door = (ParseObject) selectDoor.getSelectedItem();
        String doorPin = door.get("Pin").toString();
        String userPin = doorPinET.getText().toString();
        Log.e("USER_ENTERED_PIN", userPin);
        Log.e(TAG, doorPin);
        door.fetchIfNeededInBackground();
        if (userPin.equals(doorPin)) {
            Intent intent = new Intent(getApplicationContext(), DoorActivity.class);
            intent.putExtra("id", door.getObjectId());
            startActivity(intent);
        }
    }

    @OnEditorAction(R.id.doorCodeET)
    boolean enterDoorPin(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            getDoor();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_door_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        if (itemId == R.id.newdoor) {
            addNewDoor();
        }
        return true;

    }

}