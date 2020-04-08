package com.example.groupproject1.NASAImgDay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.groupproject1.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class nasaQuery extends AsyncTask<String, Integer, String> {
    private String date1 = "", explain = "", title = "", imageLink = "", imageQuery = "";
    private Bitmap nasaPic;
    private String[] queryArray;
    private Context context;
    private LayoutInflater inflater;
    DataBase dbAdapter;
    private String query;
    ProgressBar progressBar;
    Boolean getImage = false;
    ObjectHolder o;


    public nasaQuery(Context context, ObjectHolder o) {
        dbAdapter = new DataBase(context);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.activity_nasa_img_day, null);
        progressBar = convertView.findViewById(R.id.progressBar);
        this.o = o;

    }
    public nasaQuery(Context context, ObjectHolder o, Boolean getImage, String title, String date1) {
        dbAdapter = new DataBase(context);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View convertView = inflater.inflate(R.layout.activity_nasa_img_day, null);
        //progressBar = convertView.findViewById(R.id.progressBar);
        this.getImage = getImage;
        this.title = title;
        this.date1 = date1;
        this.o = o;
    }

    @Override
    protected String doInBackground(String... strings) {
        String ret = null;
        //progressBar.setVisibility(View.VISIBLE);
        //progressBar.setProgress(0);

        query = strings[0];


        if(getImage){
            getImage(query, title);
        } else {
            run(query);
        }
        //What is returned here will be passed as a parameter to onPostExecute:
        return ret;
    }//end of doInBackground

    private void getImage(String query, String title) {
        getImageFromURL(query, title+".jpg");
            View view = inflater.inflate(R.layout.fragment_image, null, false);
            ImageView img = view.findViewById(R.id.imgView);
            img.setImageBitmap(getImageFromURL(query, title + ".jpg"));

    }
    private void run(String query) {

        InputStream jsonStream = getInputStream(query);
        JSONObject jsonObject = getJsonObjectFromStream(jsonStream);

        queryArray = jsonObject.toString().split("\"");

        for (int i = 0; i < queryArray.length; i++) {

            if (queryArray[i].equals("date")) {
                date1 = queryArray[i + 2];

            } else if (queryArray[i].equals("explanation")) {
                explain = queryArray[i + 2];
            } else if (queryArray[i].equals("title")) {
                title = queryArray[i + 2];
            } else if (queryArray[i].equals("url")) {
                imageLink = queryArray[i + 2];
            }

        }
        String imageTitle = "";
        imageQuery = "";
        String[] imageArray = imageLink.split("/");
        for (int i = 0; i < imageArray.length; i++) {
            imageQuery += imageArray[i];

            if (imageArray[i].equals("image\\")) {
                imageTitle = imageArray[i + 2];
            }

        }
        imageQuery = imageQuery.replace("\\", "/");

        queryArray = jsonObject.toString().split("\"");
        nasaPic = getImageFromURL(imageQuery, title + ".jpg");
    }
    @Override                   //Type 3
    protected void onPostExecute(String sentFromDoInBackground) {
        super.onPostExecute(sentFromDoInBackground);
        int count = 0;

        //update GUI Stuff:
        if (date1 != null && title != null && !getImage) {

            for(int i = 0; i < dbAdapter.getCount(); i++){
                if(dbAdapter.getDate(dbAdapter.getId(i)).equals(date1)) {
                count++;
                }
            }
            if (count ==0) {
                o.setDate(date1);
                o.setQuery(query);
                o.setTitle(title);
                Log.i("onPostExecute: ", "It's over");
            }else
            Log.i("onPostExecute: ", "Query already exists");

            o.setImg(nasaPic);
        }
        //progressBar.setProgress(100);
        //progressBar.setVisibility(View.INVISIBLE );
    }

    @Override                       //Type 2
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //Update GUI stuff only:
        //progressBar.setVisibility(View.VISIBLE);
        //progressBar.setProgress(values[0]);

    }

    public InputStream getInputStream(String URL) {
        String ret = null;
        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("response code:", "" + urlConnection.getResponseCode());
            return urlConnection.getInputStream();
        } catch (IOException ioe) {
            ret = "IO Exception. Is the Wifi connected?";
        }
        return null;
    }

    public JSONObject getJsonObjectFromStream(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        String ret = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return new JSONObject(stringBuffer.toString());
        } catch (IOException ioe) {
            ret = "IO Exception. Is the Wifi connected?";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Bitmap getImageFromURL(String imageLink, String iconName) {
        try {
            if (!fileExistance(iconName)) {
                java.net.URL url = new URL(imageLink);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Bitmap image = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = context.openFileOutput(iconName, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    o.setImg(image);
                    return image;
                }
            } else {
                FileInputStream fis = null;
                fis = context.openFileInput(iconName);
                return BitmapFactory.decodeStream(fis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean fileExistance(String fname) {

        File file = context.getFileStreamPath(fname);
        return file.exists();
    }



}