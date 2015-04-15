package com.lock.peter.nfcopen;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainMenuFragment extends Fragment {

    private EventBus bus = EventBus.getDefault();
    @InjectView(R.id.unlockDoor)
    Button unlockDoor;

    @InjectView(R.id.normaliseDoor)
    Button normaliseDoor;

    @InjectView(R.id.toggleDoor)
    Button toggleDoor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_menu_fragment, container, false);
        ButterKnife.inject(this,myView);
        return myView;
    }
    @Override
    public void onResume(){
        super.onResume();
        DoorOptions.setDefaults();
        TextView tv = (TextView) getActivity().findViewById(R.id.header);
        tv.setText("Select An Option");
    }

    @OnClick({ R.id.unlockDoor, R.id.normaliseDoor, R.id.toggleDoor })
    public void pickDoor(Button selectedButton) {
        resetButtons();
        if (selectedButton.getId()==R.id.normaliseDoor) {
            selectedButton.setBackgroundResource(R.drawable.normalizesel);
            DoorOptions.setNormalise();
        } else if (selectedButton.getId()==R.id.toggleDoor) {
            selectedButton.setBackgroundResource(R.drawable.togglesel);
            DoorOptions.setToggle();
        }
        else{
            selectedButton.setBackgroundResource(R.drawable.opensel);
        }
    }

    @OnClick({ R.id.changeUserPin })
    public void changePasswordFragment() {
        Events.changePasswordEvent changePasswordEvent = new Events.changePasswordEvent();
        bus.post(changePasswordEvent);
    }

    public void resetButtons(){
        unlockDoor.setBackgroundResource(R.drawable.unlockdoor);
        toggleDoor.setBackgroundResource(R.drawable.toggledoor);
        normaliseDoor.setBackgroundResource(R.drawable.normaizedoor);
    }
}
