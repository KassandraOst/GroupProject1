package com.example.groupproject1;

/**
 * The user is navigated here after selecting one news. Where data is saved and send to database
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ArticlesList extends Activity {

    private static final String TAG = "myApp";

    DatabaseHelper mDatabaseHelper;
    TextView ds;
    ListView lv;
    Button savedAr;

    /**
     * Gets the news description,link,title and saves it
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_listview);


        Intent in = getIntent();


        String desc = in.getStringExtra("KEY_DESCRIPTION");


        mDatabaseHelper = new DatabaseHelper(this);

//Check if description value is 0 or no
        String newEntry = desc;
        if (desc.length() != 0) {
            AddData(newEntry);
        } else {
            toastMessage("No New entry. Please select");
        }


        setContentView(R.layout.saved_listview);
        lv = (ListView) findViewById(R.id.myList);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();

    }

    private void populateListView() {
    //    Log.d(TAG, "populateListView: Display data in the ListView. ");

   // call the database and getData method.
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, listData);
        lv.setAdapter(adapter);
    }
/*
* The new values are inserted here and check
*
* @param newEntery*/

    public void AddData(String newEntery) {

        boolean insertData = mDatabaseHelper.addData(newEntery);

        if (insertData) {
            //toastMessage("");
        } else {
            toastMessage("Something went wrong");
        }

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
