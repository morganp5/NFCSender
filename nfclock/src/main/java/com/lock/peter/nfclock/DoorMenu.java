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
import butterknife.OnClick;

public class DoorMenu extends Activity implements OnItemSelectedListener {

    //TODO Fully implement ButterKnife
    private final String TAG = "DOORMENU";

    private Spinner selectDoor;
    private Boolean selectDoorInit = false;
    private Button selectDoorButton;
    private EditText doorPinET;
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
        selectDoor = (Spinner) findViewById(R.id.selectDoor);
        doorPinET = (EditText) findViewById(R.id.doorCodeET);
        mainAdapter = new CustomAdapter(this);
        //Display DoorNames in list
        mainAdapter.setTextKey("DoorName");
        mainAdapter.loadObjects();
        Log.d(TAG , "Doors Loaded");
        selectDoor.setAdapter(mainAdapter);
        selectDoor.setPrompt("Select Door");
        selectDoor.setOnItemSelectedListener(this);
    }

    // Get the selected dropdown list value
    public void addListenerOnButton() {
        selectDoorButton = (Button) findViewById(R.id.btnSubmit);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (selectDoorInit) {
            TextView doorCodeText = (TextView) findViewById(R.id.doorCodeText);
            doorPinET.setVisibility(View.VISIBLE);
            doorCodeText.setVisibility(View.VISIBLE);
            selectDoorButton.setVisibility(View.VISIBLE);

        } else selectDoorInit = !selectDoorInit;
    }

    @OnClick(R.id.addDoorButton)
    public void addNewDoor(){
        Log.d(TAG,"HEY");
        Intent intent = new Intent(getApplicationContext(), AddDoorActivity.class);
        startActivity(intent);
    }

    private void getDoor(){
        ParseObject door = (ParseObject) selectDoor.getSelectedItem();
        String doorPin = door.get("Pin").toString();
        String userPin = doorPinET.getText().toString();
        Log.e("USER_ENTERED_PIN", userPin);
        Log.e(TAG , doorPin);
        door.fetchIfNeededInBackground();
        if (userPin.equals(doorPin)){
            Intent intent = new Intent(getApplicationContext(), DoorActivity.class);
            intent.putExtra("id", door.getObjectId());
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}