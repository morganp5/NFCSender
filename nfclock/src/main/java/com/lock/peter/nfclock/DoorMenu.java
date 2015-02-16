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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
//TODO turn into fragment and use main activity to coordinate
public class DoorMenu extends Activity implements OnItemSelectedListener {

    private Spinner spinner2;
    private Button btnSubmit;
    private Boolean spinnerInit = false;
    private EditText doorPinTV;
    //private ParseQueryAdapter<ParseObject> mainAdapter;
    private String selectedDoor = "";
    private CustomAdapter mainAdapter;
    private final String TAG = "DOORMENU";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_menu);
        addItemsOnSpinner2();
        addListenerOnButton();
        spinner2.setOnItemSelectedListener(this);

    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        mainAdapter = new CustomAdapter(this);
        mainAdapter.setTextKey("DoorName");
        mainAdapter.loadObjects();
        spinner2.setPrompt("Select Door");
        //spinner2.setSelection(1);
        spinner2.setAdapter(mainAdapter);
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                //mainAdapter.getDoor();
                ParseObject door = (ParseObject) spinner2.getSelectedItem();
                List<ParseObject> users = door.getList("Users");
                String output = "";
                Log.e("DOORMENUTVPIN", doorPinTV.getText().toString());
                Log.e("DOORMENUSPIN", door.get("Pin").toString());
                door.fetchIfNeededInBackground();
                if(doorPinTV.getText().toString().equals(door.get("Pin").toString())){
                    Log.e(TAG, door.get("Pin").toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("id", door.getObjectId());
                    startActivity(intent);
                }
                /*ParseObject.fetchAllIfNeededInBackground(users, new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e != null) {
                            Log.e("DOORMENU", e.getMessage(), e);
                            for (ParseObject obj : objects) {
                                Log.e("DOORMENU", obj.toString());
                                ParseUser user = (ParseUser) obj;
                                String output = user.getUsername() + ",";
                                Log.e("DOORMENU", output);
                            }
                            return;
                        }
                        // friends are each filled with data
                    }
                });
                for (ParseObject obj : users) {
                    Log.e("DOORMENU", obj.toString());
                    ParseUser user = (ParseUser) obj;
                    output = user.getUsername() + ",";
                    Log.e("DOORMENU", output);
                }
                Toast.makeText(DoorMenu.this,
                        "OnClickListener : " +
                                "\nSpinner 2 : " + spinner2.getSelectedItem().toString() +
                                "\ndoorPinText" + doorPinTV.getText() +
                                "\n" + output,
                        Toast.LENGTH_SHORT).show();
*/
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerInit) {
            //TODO MAKE Elements XML and inject that instead
            RelativeLayout mRlayout = (RelativeLayout) findViewById(R.id.door_menu);
            RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mRparams.setMargins(10, 30, 10, 30);
            mRparams.addRule(RelativeLayout.BELOW, R.id.spinner2);
            TextView tv = new TextView(this);
            tv.setText("Enter Door Code");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setLayoutParams(mRparams);
            mRlayout.addView(tv);
            doorPinTV = new EditText(this);
            mRparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mRparams.addRule(RelativeLayout.BELOW, R.id.spinner2);
            mRparams.addRule(RelativeLayout.ALIGN_PARENT_END);

            doorPinTV.setLayoutParams(mRparams);
            //myEditText.setMinWidth(100);
            doorPinTV.setWidth(400);
            mRlayout.addView(doorPinTV);
        } else spinnerInit = !spinnerInit;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}