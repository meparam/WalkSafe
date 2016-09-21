package com.example.heidiwu.walksafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    TextView labelLatitude, labelLongitude;
    TextView exLat, exLong;
    Tracker gpsTracker;
    String coord = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocation();    //just displays the coordinates
    }

    public void selectContacts(View view) {
        // Second view when click the button of the main view
        startActivity(new Intent(this, ContactsList.class));
    }

    //returns coordinates in a string
    public String getLocation() {
        double latitude, longitude;
        String showLatitude, showLongitude;
        exLat = (TextView) findViewById(R.id.textView8);
        exLong = (TextView) findViewById(R.id.textView9);
        labelLatitude = (TextView) findViewById(R.id.textView6);
        labelLongitude = (TextView) findViewById(R.id.textView7);

        gpsTracker = new Tracker(MainActivity.this);

        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            //Set text
            exLat.setText(Double.toString(latitude));
            exLong.setText(Double.toString(longitude));

            showLatitude = Double.toString(latitude);
            showLongitude = Double.toString(longitude);

            coord = "http://www.google.com/maps/place/" + showLatitude + "," + showLongitude;
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Location")
                    .setMessage("Your Location isn't on! Turn it on now?")
                    .setPositiveButton("Alright", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return coord;
    }

   //auto sends panic text messages to all contacts
    public void Panic(View view) {
        coord = getLocation();
        //cursor for phone number list
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);

        //send text out
        SmsManager smsManager = SmsManager.getDefault();

        String msg = "I just pressed my PANIC button. I might be in danger! My Location: " + coord;
        //String msg = "I might be in danger. Please check up on me! Location: " + "http://www.google.com/maps/place/40.768663,-73.964784";

        while (cursor.moveToNext()) {
            String phoneNo = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        }
        Toast.makeText(getApplicationContext(), "Panic Alert Sent",
                Toast.LENGTH_SHORT).show();
        cursor.close();
    }


    public void checkMap(View view) {
        String uri = String.format(Locale.ENGLISH, getLocation());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public void Refresh(View view){
        getLocation();
    }
}
