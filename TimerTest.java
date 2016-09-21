package com.example.heidiwu.walksafe;

import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class TimerTest extends Activity {
    private int NOTIFICATION_ID = 1001;
    private Notification.Builder mBuilder;
    TextView text1;
    private static final String FORMAT = "%02d:%02d";
    Tracker gpsTracker;
    String coord = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_test);
        final int[] contact_pos = getIntent().getIntArrayExtra("contactPos");
        text1 = (TextView) findViewById(R.id.textView1);
        long delayTime = 1000;

        final Button ClickGo = (Button) findViewById(R.id.startTimer);
        final Button ClickSafe = (Button) findViewById(R.id.pingSafe);

        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();

        mBuilder = new Notification.Builder(this);


        Intent resultIntent = new Intent(this, TimerTest.class);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntentToOpenApp = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentTitle("WALKSAFE")
                .setContentIntent(pendingIntentToOpenApp)
                .setContentText("Your safety timer ran out! ALERTS SENT!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[] { 0, 500, 500, 500, 500, 500, 500 });


       Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        //COUNTER WORKS WHEN GIVEN CORRECT PARAMETERS
        final CountDownTimer timer = new CountDownTimer(45000, delayTime) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                text1.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                text1.setTextSize(50);
                ClickSafe.setTextSize(50);
                text1.setText("Alerting...");
                ClickSafe.setText("False Alarm?");

                coord = getLocation();

                String msg = "My safety timer ran out. Can you check up on me? Location: " + coord;
                Cursor cursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        null, null, null);

                //send text out
                SmsManager smsManager = SmsManager.getDefault();

                while (cursor.moveToNext()) {
                    for (int i = 0; i < contact_pos.length; i++) {
                        if (cursor.getPosition() == contact_pos[i]) {
                            String phoneNo = cursor
                                    .getString(cursor
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                        }
                    }
                }
                cursor.close();
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            }
        };

        //Working
        ClickGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1.setTextSize(80);
                timer.start();
                ClickGo.setText("EXTEND TIMER");
            }
        });

        ClickSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                if (text1.getText() == "Alerting..."){
                    Cursor cursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            null, null, null);

                    //send text out
                    SmsManager smsManager = SmsManager.getDefault();

                    String msg = "False alarm! I'm fine! Though if you really want to make sure, feel free to call/text me.";

                    while (cursor.moveToNext()) {
                        for (int i = 0; i < contact_pos.length; i++) {
                            if (cursor.getPosition() == contact_pos[i]) {
                                String phoneNo = cursor
                                        .getString(cursor
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                            }
                        }
                    }
                    cursor.close();
                }
                ClickGo.setText("GO");
                ClickSafe.setText("SAFE");
                text1.setText("00:45");
                text1.setTextSize(80);
            }
        });
    }

    //WORKING
    //returns coordinates in a string
    public String getLocation(){
        String showLatitude,showLongitude;
        double latitude, longitude;

        gpsTracker = new Tracker(TimerTest.this);
        if(gpsTracker.canGetLocation()){

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            showLatitude = Double.toString(latitude);
            showLongitude = Double.toString(longitude);

            //coord = "Latitude: " + showLatitude + " Longitude: " + showLongitude;
            coord = "http://www.google.com/maps/place/" + showLatitude + "," + showLongitude;
        }
        return coord;
    }
}