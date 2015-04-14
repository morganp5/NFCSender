package com.lock.peter.nfcopen;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainMenuFragment extends Fragment {

    private EventBus bus = EventBus.getDefault();

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

    @OnClick({ R.id.unlockDoor, R.id.normaliseDoor, R.id.toggleDoor })
    public void pickDoor(Button selectedButton) {
        String fragmentText = getString(R.string.unlockDoorText);
        Events.buttonPressed buttonEvent = new Events.buttonPressed();
        if (selectedButton.getId()==R.id.normaliseDoor) {
            fragmentText = getString(R.string.normaliseDoorText);
        } else if (selectedButton.getId()==R.id.toggleDoor) {
            fragmentText = getString(R.string.toggleDoorText);
        }
        buttonEvent.setText(fragmentText);
        Log.d("HHHHH",fragmentText);
        bus.post(buttonEvent);
    }

    @OnClick({ R.id.changeUserPin })
    public void changePasswordFragment() {
        Events.changePasswordEvent changePasswordEvent = new Events.changePasswordEvent();
        bus.post(changePasswordEvent);
    }

    @OnClick({R.id.logout})
    public void logout() {
                //TODO Remove reliance on ParseUser
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
    }
}
