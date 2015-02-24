package com.lock.peter.nfcopen;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lock.peter.nfcopen.R;


public class MainMenu extends Activity implements OnFragmentInteractionListener  {

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        String username = ParseApplication.currentUser();
        TextView userTV = (TextView) findViewById(R.id.txtuser);
        userTV.setText("You are logged in as " + username);

        MainMenuFragment fragment = new MainMenuFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.onAttach(this);
        transaction.add(R.id.sample_content_fragment, fragment, "Menu");
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(String uri) {
        final String TAG = "MENU";
        Log.d(TAG, String.valueOf(uri));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DoorOptions.setDefaults();
        Fragment fragment = UnlockDoorFragment.newInstance(uri);
        fragment.onAttach(this);
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.addToBackStack("Menu");
        transaction.commit();
    }

}