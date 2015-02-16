package nfcsender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lock.peter.nfcsender.R;
import com.parse.ParseUser;


public class Welcome extends Activity {

    // Declare Variable
    Button logout;
    Button unlockDoor;
    Button normaliseDoor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        String struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        // Locate Button in welcome.xml
        logout = (Button) findViewById(R.id.logout);

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
            }
        });
        final Context t = this;


        //Todo replace all buttons with command pattern interface


        // Locate Button in welcome.xml
        unlockDoor = (Button) findViewById(R.id.unlockDoor);

        // Logout Button Click Listener
        unlockDoor.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                DoorOptions.setNormalise(t, false);
                DoorOptions.SetToggle(t, false);
                // If user is anonymous, send the user to LoginSignupActivity.class
                Intent intent = new Intent(Welcome.this,
                        UnlockDoor.class);
                startActivity(intent);
            }
        });

        Button toggleDoor = (Button) findViewById(R.id.toggleDoor);

        // Logout Button Click Listener
        toggleDoor.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                DoorOptions.setNormalise(t, false);
                DoorOptions.SetToggle(t, true);
                // If user is anonymous, send the user to LoginSignupActivity.class
                Intent intent = new Intent(Welcome.this,
                        UnlockDoor.class);
                startActivity(intent);
            }
        });

        normaliseDoor = (Button) findViewById(R.id.normaliseDoor);

        // Logout Button Click Listener
        normaliseDoor.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                DoorOptions.SetToggle(t, false);
                DoorOptions.setNormalise(t, true);
                // If user is anonymous, send the user to LoginSignupActivity.class
                // Intent for the activity to open when user selects the notification
                Intent intent = new Intent(Welcome.this,
                        UnlockDoor.class);
                startActivity(intent);
            }
        });


    }
}