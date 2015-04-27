package com.lock.peter.nfclock;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DoorActivity extends Activity implements AccessCardReader.AccessAttempt {

    private static final String TAG = "DoorActivity";
    private static final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private AccessCardReader mAccessCardReader;

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
        mAccessCardReader = new AccessCardReader(this, door);
        enableReaderMode();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId= item.getItemId();
        if (itemId == R.id.logs) {
            viewLogs();
        } else if (itemId == R.id.user) {
            addUser();
        }
        return true;
    }

    private void addUser() {
        addNewUser = true;
        showToast("Swipe user phone to add");
    }

    private void viewLogs() {
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
            } else if (allowed) {
                final String setting = jsonUnlockRequest.getString("Setting");
                accessGranted(setting);
            } else
                openCountdown(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void accessGranted(String setting) {

        if (setting.equals("Open")) {
            openCountdown(true);
        } else if (setting.equals("Toggle")) {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    doorStatus.setText("Door:Toggled Opened");
                    doorStatus.setBackgroundResource(R.drawable.toggled);
                }
            });
        } else {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    doorStatus.setText(R.string.intro_message);
                    doorStatus.setBackgroundResource(R.drawable.closed);
                }
            });
        }
    }


    private void openCountdown(final boolean allowed) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(10000, 1000) {
                    boolean first = true;

                    public void onTick(long millisUntilFinished) {
                        if (allowed) {
                            if (first) {
                                first = false;
                                doorStatus.setBackgroundResource(R.drawable.open);
                            }
                            doorStatus.setText("Unlocked for:" + millisUntilFinished / 1000);
                        } else {
                            doorStatus.setText("Access Denied");
                            if (first) {
                                doorStatus.setBackgroundResource(R.drawable.denied);
                            }
                        }
                    }

                    public void onFinish() {
                        doorStatus.setText("Door:Locked");
                        first = true;
                        doorStatus.setBackgroundResource(R.drawable.closed);
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
