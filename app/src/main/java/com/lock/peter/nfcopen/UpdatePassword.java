package com.lock.peter.nfcopen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by peter on 13/04/15.
 */
public class UpdatePassword extends Fragment {
    private EventBus bus = EventBus.getDefault();

    @InjectView(R.id.current_password)
    EditText currentPasswordEt;
    @InjectView(R.id.new_password)
    EditText newPasswordEt;
    @InjectView(R.id.confirm__new_password)
    EditText confirmNewPasswordEt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.update_password_fragment, container, false);
        ButterKnife.inject(this, myView);
        return myView;
    }


    @OnClick(R.id.update_password)
    void changePassword()  {
        String currentPassword = currentPasswordEt.getText().toString();
        String newPassword = newPasswordEt.getText().toString();
        String confirmNewPassword = confirmNewPasswordEt.getText().toString();
        if(confirmNewPassword.equals(newPassword)){
            try {
                ParseApplication.updatePassword(currentPassword,newPassword);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getActivity(), "Current Passwords Do Not Match", Toast.LENGTH_SHORT);
        }
    }

}
