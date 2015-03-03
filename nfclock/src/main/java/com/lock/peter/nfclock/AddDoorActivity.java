package com.lock.peter.nfclock;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseRole;

public class AddDoorActivity extends Activity {


    @InjectView(R.id.enter_door_name)
    EditText doorNameET;

    @InjectView(R.id.enter_door_pin)
    EditText doorPinEt;

    @InjectView(R.id.requires_door_pin)
    CheckBox requiresPinRB;

    @InjectView(R.id.create_door)
    Button createDoor;

    private boolean requiresPin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_door);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.create_door)
    public void addDoor() {
        ParseObject Door = new ParseObject("Door");
        String doorName = String.valueOf(doorNameET.getText());
        Door.put("DoorName", doorName);
        ParseRole Role = new ParseRole(doorName);
        Role.saveInBackground();
        if (requiresPin) {
            int pin = Integer.parseInt(String.valueOf(doorPinEt.getText()));
            Door.put("Pin", pin);
        }
        Door.put("requiresPin", requiresPin);
        Door.saveInBackground();
        doorAddedMessage(doorName);
        resetForm();
    }

    @OnClick(R.id.requires_door_pin)
    public void addPin() {
        requiresPin = true;
        doorPinEt.setVisibility(View.VISIBLE);
        doorPinEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    addDoor();
                }
                return false;
            }
        });
    }

    private void resetForm() {
        doorNameET.setText("");
        doorPinEt.setText("");
        doorPinEt.setVisibility(View.INVISIBLE);
        requiresPinRB.setChecked(false);
        requiresPin = false;
    }

    private void doorAddedMessage(String doorName) {
        CharSequence text = "Door " + doorName + " has been added to Parse";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

}
