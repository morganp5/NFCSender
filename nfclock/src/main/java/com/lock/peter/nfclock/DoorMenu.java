package com.lock.peter.nfclock;

/**
 * Created by peter on 05/02/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.parse.ParseObject;

import java.util.List;
//TODO turn into fragment and use main activity to coordinate
public class DoorMenu extends Activity implements OnItemSelectedListener {


    private final String TAG = "DOORMENU";

    private Spinner selectDoor;
    private Boolean selectDoorInit = false;

    private Button btnSubmit;
    private EditText doorPinTV;

    private String selectedDoor = "";

    //Parse Adapter For Pulling List Of Doors
    private CustomAdapter mainAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_menu);
        pullDoors();
        addListenerOnButton();
        selectDoor.setOnItemSelectedListener(this);

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
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        selectDoor = (Spinner) findViewById(R.id.selectDoor);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                //mainAdapter.getDoor();
                ParseObject door = (ParseObject) selectDoor.getSelectedItem();
                List<ParseObject> users = door.getList("Users");
                String output = "";
                Log.e("DOORMENUTVPIN", doorPinTV.getText().toString());
                Log.e("DOORMENUSPIN", door.get("Pin").toString());
                door.fetchIfNeededInBackground();
                if(doorPinTV.getText().toString().equals(door.get("Pin").toString())){
                    Log.e(TAG, door.get("Pin").toString());
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
            //TODO MAKE Elements XML and inject that instead
            RelativeLayout mRlayout = (RelativeLayout) findViewById(R.id.door_menu);
            RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mRparams.setMargins(10, 30, 10, 30);
            mRparams.addRule(RelativeLayout.BELOW, R.id.selectDoor);
            TextView tv = new TextView(this);
            tv.setText("Enter Door Code");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setLayoutParams(mRparams);
            mRlayout.addView(tv);
            doorPinTV = new EditText(this);
            mRparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mRparams.addRule(RelativeLayout.BELOW, R.id.selectDoor);
            mRparams.addRule(RelativeLayout.ALIGN_PARENT_END);

            doorPinTV.setLayoutParams(mRparams);
            //myEditText.setMinWidth(100);
            doorPinTV.setWidth(400);
            mRlayout.addView(doorPinTV);
        } else selectDoorInit = !selectDoorInit;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}