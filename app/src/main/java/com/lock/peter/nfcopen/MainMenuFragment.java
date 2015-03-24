package com.lock.peter.nfcopen;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
    public void pickDoor(Button door) {
        String fragmentText = getString(R.string.unlockDoorText);
        Events.buttonPressed button = new Events.buttonPressed();
        if (door.getId()==R.id.normaliseDoor) {
            fragmentText = getString(R.string.normaliseDoorText);
        } else if (door.getId()==R.id.toggleDoor) {
            fragmentText = getString(R.string.toggleDoorText);
        }
        button.setText( fragmentText );
        bus.post(button);
    }

    @OnClick({R.id.logout})
    public void logout() {
                //TODO Remove reliance on ParseUser
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
    }
}
