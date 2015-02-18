package com.lock.peter.nfclock;

/**
 * Created by peter on 05/02/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

public class DoorMenu extends Activity implements OnItemSelectedListener {


    private final String TAG = "DOORMENU";

    private Spinner selectDoor;
    private Boolean selectDoorInit = false;
    private Button selectDoorButton;
    private EditText doorCodeET;
    //Parse Adapter For Pulling List Of Doors
    private CustomAdapter mainAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_menu);
        pullDoors();
        addListenerOnButton();
    }

    // add items into spinner dynamically
    public void pullDoors() {
        selectDoor = (Spinner) findViewById(R.id.selectDoor);
        mainAdapter = new CustomAdapter(this);
        //Display DoorNames in list
        mainAdapter.setTextKey("DoorName");
        mainAdapter.loadObjects();
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
                ParseObject door = (ParseObject) selectDoor.getSelectedItem();
                String doorPin = door.get("Pin").toString();
                String userPin = doorCodeET.getText().toString();
                Log.e("USER_ENTERED_PIN", userPin);
                Log.e(TAG , doorPin);
                door.fetchIfNeededInBackground();
                if (userPin.equals(doorPin)){
                    Intent intent = new Intent(getApplicationContext(), DoorActivity.class);
                    intent.putExtra("id", door.getObjectId());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (selectDoorInit) {
            doorCodeET = (EditText) findViewById(R.id.doorCodeET);
            TextView doorCodeText = (TextView) findViewById(R.id.doorCodeText);
            doorCodeET.setVisibility(View.VISIBLE);
            doorCodeText.setVisibility(View.VISIBLE);
            selectDoorButton.setVisibility(View.VISIBLE);

        } else selectDoorInit = !selectDoorInit;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}