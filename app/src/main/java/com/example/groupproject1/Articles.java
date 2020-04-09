package com.example.groupproject1;

/**
 * This class displays list of articles with buttons
 * toast messages, tool bar, navigation drawer and Snackbar
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Articles extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String Activity_Name = "BBC News reader";
    private final String Url = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";

    protected ListView list;
    protected ProgressBar progressBar;

    NewsAdapter newsAdapter;
    TextView titles;
    ArrayList<ArticlesInformation> articlesInformationArrayList = new ArrayList<>();
    Button save;


    /**
     * onCreate will the start the application
     * and get all list of articles and other features
     *
     * @param savedInstanceState
     */
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_listview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);

//Drawer layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Snackbar snackbar = Snackbar.make(navigationView, R.string.snk, Snackbar.LENGTH_LONG);
        snackbar.show();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);


        //progress bar minimum value to 50 and max value to 100
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(100);
        progressBar.setProgress(50);
        newsAdapter = new NewsAdapter(this);
        new NewsList().execute();


        list = findViewById(R.id.news);
        list.setAdapter(newsAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * when the user selects an article it takes the user to
             * that specific news and provides additional details with save feature
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(Articles.this, DescriptionAndLink.class);
                in.putExtra("title", articlesInformationArrayList.get(position + 1).getTitle());
                in.putExtra("description", articlesInformationArrayList.get(position + 1).getDescription());
                in.putExtra("link", articlesInformationArrayList.get(position + 2).getLink());
                in.putExtra("date", articlesInformationArrayList.get(position + 3).getDate());
                startActivityForResult(in, 222);


            }

        });

/*
* list of articles which are saved can be accessed here
* */
        save = findViewById(R.id.savedBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Articles.this, Saved.class);
                startActivity(intent);
            }


        });


    }
/*
* This gets the menu
*
* @param menu
* */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        progressBar.setProgress(75);
        return true;
    }

    /*
    *
    * The menu which shows on top of the screen reacts to the user
    * and gives information
    *
    * @param item
    * */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.one:
                Toast.makeText(this, R.string.menuBBC, Toast.LENGTH_SHORT).show();
                Intent BBC = new Intent(this, Articles.class);
                startActivity(BBC);
                break;

            case R.id.two:
                Toast.makeText(this,R.string.menuG , Toast.LENGTH_SHORT).show();
                break;

            case R.id.three:
                Toast.makeText(this, R.string.menuN, Toast.LENGTH_SHORT).show();
                break;

            case R.id.four:
                Toast.makeText(this, R.string.menuE, Toast.LENGTH_SHORT).show();
                break;


            case R.id.five:
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Articles.this);
                alertBuilder.setTitle(R.string.alertTitle);
                alertBuilder.setMessage(R.string.AlertMessage);
                alertBuilder.setPositiveButton(R.string.pb, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int num) {
                        alertBuilder.setTitle("");
                    }
                });
                alertBuilder.show();
                break;
        }
        return true;
    }


    public boolean onNavigationItemSelected(MenuItem item) {

        String message = null;

        switch (item.getItemId()) {
            case R.id.bbcN:
                Intent BBC = new Intent(this, Articles.class);
                startActivity(BBC);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

/*
* This class helps the listView to be shown of all the articles
* */
    private class NewsAdapter extends ArrayAdapter<ArticlesInformation> {
        public NewsAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return articlesInformationArrayList.size();
        }

        public ArticlesInformation getItem(int position) {
            return articlesInformationArrayList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Articles.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.activity_articles_details, null);

            titles = (TextView) result.findViewById(R.id.newaheadline);
            titles.setText(getItem(position).getTitle()); // get the string at position
            return result;
        }

    }

    /**
     * AsyncTask is used here to get the detailed list of articles
     *
     *
     */

    private class NewsList extends AsyncTask<String, Integer, String> {


        private String news;
        private String link;
        private ArrayList<String> description = new ArrayList<>();
        private ArrayList<String> title = new ArrayList<>();
        private ArrayList<String> links = new ArrayList<>();
        private ArrayList<String> date = new ArrayList<>();


        /**
         * AsyncTask is used for proper and easy use of UI thread
         * helps in sync the details.
         *
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream iStream = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(iStream, "UTF-8");


                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {

                        case XmlPullParser.START_TAG:
                            String name = xpp.getName();
                            if (name.equals("title")) {
                                xpp.next();
                                news = xpp.getText();
                                if (!news.contains("BBC News - US")) {
                                    title.add(news);
                                }
                                publishProgress(30);
                                Log.d("News is:", news);

                            }
                            if (name.equals("description")) {
                                xpp.next();
                                String newsdescription = xpp.getText();


                                description.add(newsdescription);

                                publishProgress(30);

                            } else if (name.equals("link")) {
                                xpp.next();
                                link = xpp.getText();
                                if (!link.toString().equals("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml") &&
                                        !link.toString().equals("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml"))
                                    links.add(link);
                                publishProgress((30));

                            }

                            Log.i("read XML tag:", name);
                            break;

                        case XmlPullParser.TEXT:
                            break;
                    }

                    xpp.next();
                }
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }

            return "";
        }

        /**
         * This method should be implemented when AsyncTask is used to set the data in.
         *
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            articlesInformationArrayList.clear();
            for (int i = 0; i < title.size(); i++) {
                ArticlesInformation n = new ArticlesInformation();
                n.setTitle(title.get(i));
                n.setDescription(description.get(i));
                n.setLink(links.get(i));
                progressBar.setProgress(100);


                articlesInformationArrayList.add(i, n);
                newsAdapter.notifyDataSetChanged();
            }
        }
    }
}
