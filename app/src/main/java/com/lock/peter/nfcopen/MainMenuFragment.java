package com.lock.peter.nfcopen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainMenuFragment extends Fragment {

    @InjectView(R.id.unlockDoor)
    Button unlockDoor;
    @InjectView(R.id.normalizeDoor)
    Button normalizeDoor;
    @InjectView(R.id.toggleDoor)
    Button toggleDoor;
    private EventBus bus = EventBus.getDefault();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_menu_fragment, container, false);
        ButterKnife.inject(this, myView);
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DoorOptions.setDefaults();
        TextView tv = (TextView) getActivity().findViewById(R.id.header);
        tv.setText("Select An Option");
    }

    @OnClick({R.id.unlockDoor, R.id.normalizeDoor, R.id.toggleDoor})
    public void pickDoor(Button selectedButton) {
        resetButtons();
        if (selectedButton.getId() == R.id.normalizeDoor) {
            selectedButton.setBackgroundResource(R.drawable.normalizesel);
            DoorOptions.setNormalize();
        } else if (selectedButton.getId() == R.id.toggleDoor) {
            selectedButton.setBackgroundResource(R.drawable.togglesel);
            DoorOptions.setToggle();
        } else {
            selectedButton.setBackgroundResource(R.drawable.opensel);
        }
    }

    @OnClick({R.id.changeUserPin})
    public void changePasswordFragment() {
        Events.changePasswordEvent changePasswordEvent = new Events.changePasswordEvent();
        bus.post(changePasswordEvent);
    }

    public void resetButtons() {
        unlockDoor.setBackgroundResource(R.drawable.unlockdoor);
        toggleDoor.setBackgroundResource(R.drawable.toggledoor);
        normalizeDoor.setBackgroundResource(R.drawable.normaizedoor);
        DoorOptions.setDefaults();
    }
}
