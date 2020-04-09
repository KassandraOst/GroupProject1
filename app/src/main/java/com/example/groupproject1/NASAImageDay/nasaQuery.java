package com.example.groupproject1.NASAImageDay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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
    int getImage;
    ObjectHolder o;
    ImageView imgView;
    TextView titleText, detailText;


    public nasaQuery(Context context, ObjectHolder o) {
        dbAdapter = new DataBase(context);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.activity_nasa_img_day, null);
        progressBar = convertView.findViewById(R.id.progressBar);
        this.o = o;
        this.getImage =0;

    }
    public nasaQuery(Context context, ObjectHolder o, ImageView imgView, TextView titleText, TextView detailText, int getImage, String title, String date1, String explain) {

        dbAdapter = new DataBase(context);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.o = o;
        this.imgView = imgView;
        this.getImage = getImage;
        this.title = title;
        this.date1 = date1;
        this.titleText = titleText;
        this.detailText = detailText;
        this.explain = explain;

    }

    @Override
    protected String doInBackground(String... strings) {
        String ret = null;
        //progressBar.setVisibility(View.VISIBLE);
        //progressBar.setProgress(0);

        query = strings[0];


        if(getImage != 0){
            getImage(query, title);
        }  else
        run(query);
        //What is returned here will be passed as a parameter to onPostExecute:
        return ret;
    }//end of doInBackground

    private void getImage(String query, String title) {
        nasaPic = getImageFromURL(query, title + ".jpg");

    }
    private void run(String query) {

        InputStream jsonStream = getInputStream(query);
        JSONObject jsonObject = getJsonObjectFromStream(jsonStream);
        if (jsonObject != null) {

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
            dbAdapter.insertMessage(date1, imageQuery, title, explain);
        }
        else {

        }
    }
    @Override                   //Type 3
    protected void onPostExecute(String sentFromDoInBackground) {
        super.onPostExecute(sentFromDoInBackground);
        int count = 0;

        //update GUI Stuff:
        if (date1 != null && title != null) {

            if(nasaPic == null) {

                nasaPic = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.error);
            }
            for(int i = 0; i < dbAdapter.getCount(); i++){
                if(dbAdapter.getDate(dbAdapter.getId(i)).equals(date1)) {
                count++;
                }
            }
            if (count ==0) {
                o.setDate(date1);
                o.setQuery(imageQuery);
                o.setTitle(title);

            }else if (imgView != null && getImage == 1 && nasaPic != null) {
                imgView.setImageBitmap(nasaPic);
                imgView.setContentDescription(title);

            } else if(getImage == 2){
                imgView.setImageBitmap(nasaPic);
                imgView.setContentDescription(title);
                titleText.setText(title);
                detailText.setText(explain);


            } else {

            }


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
        } catch (Exception e) {
            Log.e("JSONObject Error: ", e+ " Unable to Complete");
            date1 = null;
            title = null;
            explain = null;
            nasaPic = null;
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
                    return image;
                }
            } else {
                FileInputStream fis = null;
                fis = context.openFileInput(iconName);
                return BitmapFactory.decodeStream(fis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("JsonObject error: ", e.toString());

        }

        return null;
    }

    private boolean fileExistance(String fname) {

        File file = context.getFileStreamPath(fname);
        return file.exists();
    }



}