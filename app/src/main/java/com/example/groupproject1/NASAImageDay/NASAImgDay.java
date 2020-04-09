package com.example.groupproject1.NASAImageDay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.groupproject1.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class NASAImgDay extends AppCompatActivity {

    Toolbar tb;
    ImportAPI a;
    DataBase dbAdapter;
    ListView listv;
    ObjectHolder o = new ObjectHolder();
    private FragmentManager fragManager;
    private FragmentTransaction fragTrans;
    ImageFragment aFragment;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_img_day);
        dbAdapter = new DataBase(this);
        listv = findViewById(R.id.imgList);
        fragManager = getSupportFragmentManager();

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.Open, R.string.Close) {


            public void onDrawerOpened(View draw) {
                super.onDrawerOpened(draw);
                Context c = getApplicationContext();
                Toast.makeText(c, "You have opened the drawer", Toast.LENGTH_SHORT).show();

            }

            public void onDrawerClosed(View draw) {
                super.onDrawerOpened(draw);
                Context c = getApplicationContext();

            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nv = findViewById(R.id.navView);
        nv.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        listv.setOnItemLongClickListener((list, view, pos, id) -> {
            ListAdapter la = new ListAdapter(getApplicationContext(), dbAdapter, o);
            ListView listv = findViewById(R.id.imgList);
            new AlertDialog.Builder(NASAImgDay.this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this?\n" + la.getItem(pos)+"\nWarning! This can't be undone!")

                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                        la.deleteItem(pos);
                        listv.setAdapter(la);
                        la.notifyDataSetChanged();

                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;

        });// end of long click

        listv.setOnItemClickListener((list, view, pos, id) -> {
            ListAdapter la = new ListAdapter(getApplicationContext(), dbAdapter, o);
            new AlertDialog.Builder(NASAImgDay.this)
                    .setTitle("View Image")
                    .setMessage("Would you like to load the full image and description of: " + la.getItem(pos)+ "?")

                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        aFragment = new ImageFragment();
                        fragManager.popBackStackImmediate();

                        Bundle b = new Bundle();
                        b.putLong("ID", dbAdapter.getId(pos));
                        b.putString("query", dbAdapter.getQuery((int) dbAdapter.getId(pos)));
                        aFragment.setArguments(b);
                            try {
                                Intent goToBlankActivity = new Intent(this, EmptyActivity.class);
                                goToBlankActivity.putExtra("bundle",b);
                                startActivity(goToBlankActivity);
                            } catch (Exception e) {
                                e.printStackTrace();
                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .show();
    });// end of click
// end of toolbar
updateList(null);
    }//end of OnCreate
    public void updateList(String date) {

        ListAdapter la = new ListAdapter(getApplicationContext(), dbAdapter, o);
        if(date != null) {
            a = new ImportAPI(date);
            new nasaQuery(this, o).execute(a.getApi());
        }

        Cursor cursor = dbAdapter.getAllEntries();


            if (cursor.getCount() != 0 ) {
                while (cursor.moveToNext()) {
                    la = new ListAdapter(getApplicationContext(), dbAdapter, o);

                    listv.setAdapter(la);
                    la.notifyDataSetChanged();
                }
            }



        Log.i("updateList: ", "It's over");
    }

    public void dateSearch() {
        aFragment = new ImageFragment();
        fragManager.popBackStackImmediate();

        Bundle b = new Bundle();
        aFragment.setArguments(b);
        try {
            Intent goToBlankActivity = new Intent(this, EmptyActivity2.class);
            goToBlankActivity.putExtra("bundle",b);
            startActivityForResult(goToBlankActivity, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 600) {
            String date = data.getStringExtra("date");
            Log.i("OnActivityResult: ", date);
            updateList(date);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.search:
                dateSearch();
                break;
            case R.id.refresh:
                updateList(null);
                break;

            default:
                return super.onOptionsItemSelected(item);

        }//end of item switch

        return true;
    }//end of onOptionsItemSelected

    public boolean onNavigationItemSelected (MenuItem item) {
        int id = item.getItemId();

        switch(id) {

            case R.id.navGuardian:
                Toast.makeText(this, "You have pressed Guardian", Toast.LENGTH_SHORT).show();
                // Intent goToGuardian = new Intent(this, ChatRoomActivity.class);
                // startActivity(goToGuardian);
                break;
            case R.id.navnasaEarth:
                Toast.makeText(this, "You have pressed Earth", Toast.LENGTH_SHORT).show();
                // Intent goToEarth = new Intent(this, WeatherForecast.class);
                // startActivity(goToEarth);
                break;

            case R.id.navbbcNews:
                Toast.makeText(this, "You have pressed BBC", Toast.LENGTH_SHORT).show();
                // Intent goTobbc = new Intent(this, WeatherForecast.class);
                // startActivity(goTobbc);
                break;
        }//end of switch
        // DrawerLayout drawer = findViewById(R.id.drawerLayout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }//end of onNavigationItemSelected


    public class ImportAPI {
        String api, link = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d", date ="&date=";
        int year, day, month;
        String date1, title, imageLink, imageQuery, explain;

        public ImportAPI(int month, int day, int year) {

            setDate(month, day, year);

        }

        public ImportAPI (String date1) {
            setApi(link+date+date1);

        }
        public void setDate(int month, int day, int year) {

            this.year = year;
            this.month = month;
            this.day = day;
            setApi(link+date+year+"-"+month+"-"+day);
        }

        public void setApi(String api) {
            this.api = api;
        }

        public String getApi() {
            return api;
        }

    }//end of ImportAPI Class
}//end of Class


