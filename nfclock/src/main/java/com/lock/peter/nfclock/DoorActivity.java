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
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DoorActivity extends Activity implements AccessCardReader.AccessAttempt {

    public static final String TAG = "DoorActivity";
    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public AccessCardReader mAccessCardReader;

    @InjectView(R.id.doorStatus)
    TextView doorStatus;

    private Door door;
    private boolean addNewUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        String doorId = intent.getStringExtra("id");
        door = new Door(doorId);
        //Set up the card reader
        mAccessCardReader = new AccessCardReader(this,door);
        enableReaderMode();
    }

    @OnClick(R.id.updateUsers)
    void addUser() {
        addNewUser = true;
        showToast("Swipe user phone to add");
    }
    @OnClick(R.id.viewLogs)
    void viewLogs() {
        Intent intent = new Intent(getApplicationContext(), ViewLogActivity.class);
        intent.putExtra("doorName", door.getDoorName());
        startActivity(intent);
    }

    public void onAccessAttempt(String accessCredentials) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        Log.i(TAG, accessCredentials);
        try {
            JSONObject jsonUnlockRequest = new JSONObject(accessCredentials);
            Boolean allowed = door.checkIfAuthorised(jsonUnlockRequest);
            if (addNewUser) {
                String sessionToken = jsonUnlockRequest.getString("SessionToken");
                Log.i(TAG, "Adding new user");
                door.addAllowedUser(sessionToken);
                addNewUser = false;
            }
            if (allowed) {
                final String setting = jsonUnlockRequest.getString("Setting");
                if (setting.equals("Open")) {
                    openCountdown();
                } else {
                    this.runOnUiThread(new Runnable() {
                        public void run() {
                            doorStatus.setText(door.getDoorMessage(setting));
                        }
                    });
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openCountdown() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        doorStatus.setText("Door is unlocked for " + millisUntilFinished / 1000);
                    }
                    public void onFinish() {
                        doorStatus.setText("Door is Locked");
                    }
                }.start();
            }
        });
    }


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
}
