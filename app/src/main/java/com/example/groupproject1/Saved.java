package com.example.groupproject1;
/*
* This class is used to show all saved articles and delete
* */
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Saved extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    ArrayList<String> listItem;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView slv = (ListView) findViewById(R.id.SavedListView);

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor cursor = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listData.add(cursor.getString(1));
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, listData);
            slv.setAdapter(adapter);
        }


        slv.setOnItemLongClickListener((parent, view, position, id) -> {

            final int which_item = position;
            new AlertDialog.Builder(Saved.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle(R.string.sure)
                    .setMessage(R.string.del)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        String val = listData.get(position);


                        listData.remove(val);
                        slv.setAdapter(adapter);
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            return true;
        });
    }
}