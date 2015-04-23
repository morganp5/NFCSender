package com.lock.peter.nfcopen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;


public class MainMenu extends Activity {

    @InjectView(R.id.header)
    TextView tv;

    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        ButterKnife.inject(this);
        bus.register(this);

        MainMenuFragment fragment = new MainMenuFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.onAttach(this);
        transaction.add(R.id.main_fragment, fragment, "Menu");
        transaction.commit();
    }


    //Creates the custom ActionBar for the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_unlock_door, menu);
        View v = (View) menu.findItem(R.id.userid).getActionView();
        TextView txtSearch = (TextView) v.findViewById(R.id.txt_search);
        txtSearch.setText(User.getCurrentUser());
        return super.onCreateOptionsMenu(menu);
    }

    //Display enter pin dialogue when pin requested by door
    public void onEvent(Events.PinRequest pin) {
        Log.i("MM", "Pin not set");
        showPinDialog();
    }

    //Swap the main fragment when a user selects change pin
    public void onEvent(Events.ChangePasswordEvent changePasswordEvent) throws ParseException {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        tv.setText("Enter New Password Details");
        UpdatePassword fragment = new UpdatePassword();
        fragment.onAttach(this);
        transaction.replace(R.id.main_fragment, fragment, "updatePassword");
        transaction.addToBackStack("Menu");
        transaction.commitAllowingStateLoss();
    }

    //Displays Pin Dialogue requesting user to enter appropriate door pin
    public void showPinDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptsView = layoutInflater.inflate(R.layout.prompts, null);
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
                                Toast.makeText(getApplicationContext(), "Rescan Phone", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.logout:
                User.logout();
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.changePassword:
                Events.ChangePasswordEvent CPE = new Events.ChangePasswordEvent();
                bus.post(CPE);
                break;

            default:
                break;
        }
        return true;
    }

}