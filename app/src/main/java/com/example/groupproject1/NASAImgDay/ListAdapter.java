package com.example.groupproject1.NASAImgDay;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.groupproject1.R;

class ListAdapter extends BaseAdapter {

    /* For the string array,
    first number is the item ID
    Second number is as follows:
    0 - Date
    1 - Title
    2 - Image Details
    3 - Image location
     */
    private DataBase dbAdapter;
    private LayoutInflater inflater;
    private Context context;
    private Bitmap img;
    String title, date, query;
    ObjectHolder o;

    public ListAdapter(Context context, DataBase dbAdapter, ObjectHolder o) {
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbAdapter = dbAdapter;
        this.o = o;

    }

    public ListAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbAdapter = new DataBase(context);
    }

        @Override
        public int getCount () {

            return dbAdapter.getCount();
        }

        @Override
        public String getItem ( int id){

            return dbAdapter.getTitle(getItemId(id));
        }

    @Override
        public long getItemId (int pos){


        return dbAdapter.getId(pos);
        }

    public String getDate(int id){

        return dbAdapter.getDate(getItemId(id));
    }
    public String getQuery(int id){

        return dbAdapter.getQuery(getItemId(id));
    }
        public void deleteItem ( int pos){

            dbAdapter.deleteMessage(getItemId(pos));

        }


        public void add (String date, String query, String title) {
        dbAdapter.insertMessage(date, query, title);
        }

        public void deleteAll() {

        for (int i = 0; i < dbAdapter.getCount(); i++)
        {
            dbAdapter.deleteMessage(i);

        }
        }
        public Bitmap getImg(String imageLink, int id) {

        Log.i("getImg: ", "Start");
        new nasaQuery(context, o, true, getItem((int)getItemId(id)), getDate((int)getItemId(id))).execute(getQuery((int)getItemId(id)));
            Log.i("getImg: ", "Finished");
        return o.getImg();
        }

        @Override
        public View getView ( int id, View convertView, ViewGroup parent){

        Log.i("Database Size: ", getCount()+"");

            if (getCount() > 0) {
                Log.i("Count: ", getCount()+"");
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.imgday, null);
                    TextView imgText = convertView.findViewById(R.id.imgTV);
                    ImageView imgView = convertView.findViewById(R.id.imgIV);
                        imgText.setText(getDate(id)+": "+getItem(id));
                    imgView.setImageBitmap(getImg(getQuery(id), id));

                }
            }
            return convertView;
        }//end getView

    }//end of ListAdapter


