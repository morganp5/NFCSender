/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lock.peter.nfclock;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic UI for sample discovery.
 */
public class DoorActivity extends Activity implements AccessCardReader.AccountCallback, OnClickListener {

    public static final String TAG = "DoorActivity";
    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public AccessCardReader mAccessCardReader;
    private TextView mAccountField;
    private EditText newUser;
    private Button updateUsers;

    ArrayList<String> users = new ArrayList<>();
    List<ParseUser> list11 = new ArrayList<>();

    /**
     * Called when sample is created. Displays generic UI with welcome text.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_fragment);

        Door door = new Door();
        door.onCreate();
        mAccountField = (TextView) findViewById(R.id.doorStatus);
        newUser = (EditText) findViewById(R.id.newUser);
        updateUsers = (Button) findViewById(R.id.updateUsers);
        updateUsers.setOnClickListener(oclBtbUpdate);
        //Set up the card reader
        mAccessCardReader = new AccessCardReader(this);
        users.add("peter");
        Intent intent = getIntent();
        String doorId = intent.getStringExtra("id");
        Log.i(TAG, doorId);
        ParseQuery query = new ParseQuery("Door");
        query.whereEqualTo("objectId", doorId);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        ParseObject p = list.get(0);
                        if (p.getList("Users") != null) {
                            list11 = p.getList("Users");
                            for (ParseUser obj : list11) {
                                Log.i(TAG, obj.toString());
                            }
                        } else {
                            list11 = null;
                        }
                    }
                }
            }
        });

        Log.i(TAG, "END");
        // Disable Android Beam and register our card reader callback
        enableReaderMode();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, String.valueOf(v.getId()));
        switch (v.getId()) {
            case R.id.updateUsers:
                String user = String.valueOf(newUser.getText());
                newUser.setText("");
                users.add(user);
                Log.i(TAG, users.toString());
        }
    }

    OnClickListener oclBtbUpdate = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String user = String.valueOf(newUser.getText());
            newUser.setText("");
            users.add(user);
            user = user + " has been added to access list";
            showToast(user);
            Log.i(TAG, users.toString());
        }
    };

    protected void showToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }


    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.enableReaderMode(this, mAccessCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }


    @Override
    public void onAccountReceived(final String account) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        Log.i(TAG, account);
        Boolean allowed = false;
        for (java.lang.Object user : users) {
            Log.i(TAG, user.toString());
            if (account.contains(user.toString())) {
                allowed = true;
            }
        }
        for (ParseUser obj : list11) {
            Log.i(TAG, obj.toString());
            if (account.contains(obj.toString())) {
                allowed = true;
            }
        }
        // TODO put all door operations in separate door class
        if (!allowed) {


        } else if (account.contains("Toggle:True")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccountField.setText("Door is Toggled Opened");
                }
            });
        } else if (account.contains("Normalise:True")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccountField.setText("Door is Locked");
                }
            });
        } else {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new CountDownTimer(10000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            mAccountField.setText("Door is unlocked for " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            mAccountField.setText("Door is Locked");
                        }
                    }.start();
                }
            });

        }

    }


}
