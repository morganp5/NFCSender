package com.lock.peter.nfcopen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainMenu extends Activity {

    private EventBus bus = EventBus.getDefault();

    @InjectView(R.id.txtuser)
    TextView userTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        ButterKnife.inject(this);
        bus.register(this);
        String username = ParseApplication.currentUser();
        userTV.setText("You are logged in as " + username);
        MainMenuFragment fragment = new MainMenuFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.onAttach(this);
        transaction.add(R.id.sample_content_fragment, fragment, "Menu");
        transaction.commit();
    }

    public void onEvent(Events.PinRequest pin) {
        Log.i("MM", "Pin not set");
        showPinDialog();
    }
    public void onEvent(Events.buttonPressed button) {
        Log.i("MM", "Event Button Press");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = UnlockDoorFragment.newInstance(button.getText());
        fragment.onAttach(this);
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.addToBackStack("Menu");
        transaction.commit();
    }


    public void showPinDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setRawInputType(Configuration.KEYBOARD_12KEY);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DoorOptions.setPin(Integer.parseInt(userInput.getText().toString()));
                                Toast.makeText(getApplicationContext(),"Rescan Phone", Toast.LENGTH_SHORT);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}