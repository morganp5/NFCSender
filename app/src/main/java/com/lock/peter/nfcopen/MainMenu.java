package com.lock.peter.nfcopen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import de.greenrobot.event.EventBus;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainMenu extends Activity {

    private EventBus bus = EventBus.getDefault();

    @InjectView(R.id.header)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        ButterKnife.inject(this);
        bus.register(this);
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

    public void onEvent(Events.changePasswordEvent changePasswordEvent)  throws ParseException {
        Log.i("MM", "Pin not set");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        tv.setText("Enter New Password Details");
        UpdatePassword fragment = new UpdatePassword();
        fragment.onAttach(this);
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.addToBackStack("Menu");
        transaction.commitAllowingStateLoss();
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

    /** Callback function */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Create an option menu from res/menu/items.xml */
        getMenuInflater().inflate(R.menu.menu_unlock_door, menu);
        /** Get the action view of the menu item whose id is search */
        View v = (View) menu.findItem(R.id.photo).getActionView();
        /** Get the edit text from the action view */
        TextView txtSearch = ( TextView ) v.findViewById(R.id.txt_search);
        if(ParseApplication.currentUser() !=  null )
        {
            txtSearch.setText(ParseApplication.currentUser());
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.logout:
                ParseApplication.logout();
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.changePassword:
                Events.changePasswordEvent CPE = new Events.changePasswordEvent();
                bus.post(CPE);
                break;
        }
        return true;
    }
}