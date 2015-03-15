package com.lock.peter.nfclock;

/**
 * Created by peter on 05/02/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class DoorMenu extends Activity {

    private final String TAG = "DOORMENU";

    @InjectView(R.id.selectDoor)
    Spinner selectDoor;

    private Boolean selectDoorInit = false;
    @InjectView(R.id.selectDoorBtn)
    Button selectDoorButton;

    @InjectView(R.id.doorCodeET)
    EditText doorPinET;

    @InjectView(R.id.doorCodeText)
    TextView doorCodeText;

    //Parse Adapter For Pulling List Of Doors
    private CustomAdapter mainAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_menu);
        ButterKnife.inject(this);
        pullDoors();
        addListenerOnButton();
    }

    // add items into spinner dynamically
    public void pullDoors() {
        mainAdapter = new CustomAdapter(this);
        //Display DoorNames in list
        mainAdapter.setTextKey("DoorName");
        mainAdapter.loadObjects();
        Log.d(TAG, "Doors Loaded");
        selectDoor.setAdapter(mainAdapter);
        selectDoor.setPrompt("Select Door");
    }

    // Get the selected dropdown list value
    public void addListenerOnButton() {
        selectDoorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoor();
            }
        });

        doorPinET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    getDoor();
                }
                return false;
            }
        });

    }


    @OnItemSelected(R.id.selectDoor)
    public void doorSelected() {
        if (selectDoorInit) {
            doorPinET.setVisibility(View.VISIBLE);
            doorCodeText.setVisibility(View.VISIBLE);
            selectDoorButton.setVisibility(View.VISIBLE);

        } else selectDoorInit = !selectDoorInit;
    }

    @OnClick(R.id.addDoorButton)
    public void addNewDoor() {
        Intent intent = new Intent(getApplicationContext(), AddDoorActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.selectDoorBtn)
    private void getDoor() {
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


}