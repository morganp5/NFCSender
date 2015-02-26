package com.lock.peter.nfcopen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lock.peter.nfcopen.R;
import com.parse.ParseUser;

import de.greenrobot.event.EventBus;

public class MainMenuFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    private static final int[] BUTTON_IDS = {
            R.id.unlockDoor,
            R.id.toggleDoor,
            R.id.normaliseDoor,
            R.id.logout,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_menu_fragment, container, false);
        for (int id : BUTTON_IDS) {
            Button button = (Button) myView.findViewById(id);
            button.setOnClickListener(this);
        }
        return myView;
    }

    @Override
    public void onClick(View v) {
        final String TAG = "MENU";
        switch (v.getId()) {
            case R.id.unlockDoor:
                DoorOptions.setDefaults();
                Log.d(TAG, "Unlock Door Selected");
                mListener.onFragmentInteraction(getString(R.string.unlockDoorText));
                break;
            case R.id.normaliseDoor:
                DoorOptions.setNormalise();
                Log.d(TAG, "Normalise Door Selected");
                mListener.onFragmentInteraction(getString(R.string.normaliseDoorText));
                break;
            case R.id.toggleDoor:
                DoorOptions.setToggle();
                Log.d(TAG, "Toggle Door Selected");
                mListener.onFragmentInteraction(getString(R.string.toggleDoorText));
                break;
            case R.id.logout:
                //TODO Remove reliance on ParseUser
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
