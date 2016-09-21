package com.example.heidiwu.walksafe;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.test.ApplicationTestCase;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by heidiwu on 8/10/16.
 */
public class ContactsList extends Activity {

    private ListView lview;
    int[] contact_pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.contacts_list);

        lview = (ListView) findViewById(R.id.listView1);

        getContacts();
    }

    protected void getContacts(){
        ArrayList<String> contacts = new ArrayList<String>();

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);

        while (cursor.moveToNext()) {

            String contactName = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phNumber = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contacts.add(contactName + " : " + phNumber);

        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.text, contacts);

        lview.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, contacts);
        lview.setAdapter(adapter);
        // we can select multiple item
        lview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void Cancel(View view){
        // Second view when click the button of the main view
        startActivity(new Intent(this, MainActivity.class));
    }

    public void Continue(View view){
        final SparseBooleanArray checked = lview.getCheckedItemPositions();
        int allCount = 0;
        for (int pos = lview.getCount() - 1; pos >= 0; pos--) {
            if (checked.get(pos) == true){
                allCount++;
            }
        }
        if (allCount == 0){
            Toast.makeText(getApplicationContext(), "Please select at least 1 contact",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            contact_pos = new int[allCount];
            //can combine allCount parse and array parse into one
            int j = 0;
            for (int pos = lview.getCount() - 1; pos >= 0; pos--) {
                if (checked.get(pos) == true) {
                    contact_pos[j] = pos;
                    j++;
                }
            }
            Intent intent = new Intent(this, TimerTest.class);
            intent.putExtra("contactPos", contact_pos);
            startActivity(intent);
        }
    }
}

