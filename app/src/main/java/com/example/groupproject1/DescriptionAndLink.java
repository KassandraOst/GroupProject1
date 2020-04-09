package com.example.groupproject1;


/**
 * This class gets the selected news and sends it
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptionAndLink extends Activity {

    private static final String TAG = "ListDataActivity";

    TextView dat;
    TextView desc;
    TextView lnk;
    Button saveArticle;


    /**
     *
     * Gets data and sends it for geting saved in database
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_information);

        Intent intent = getIntent();


        String description = intent.getStringExtra("description");
        String link = intent.getStringExtra("link");
        String date = intent.getStringExtra("date");


        saveArticle = findViewById(R.id.saveArticle);
        Context context = getApplicationContext();


        Pattern pattern = Pattern.compile("<p>(.+?)</p>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(description);
        matcher.find();

        desc = findViewById(R.id.description);
        lnk = findViewById(R.id.link);
        dat = findViewById(R.id.dateN);

        desc.setText(description);
        lnk.setText(link);
        dat.setText(date);

        saveArticle.setOnClickListener(e -> {


            Intent next = new Intent(getApplicationContext(), ArticlesList.class);

            next.putExtra("KEY_DESCRIPTION", description);
            next.putExtra("KEY_LINK", link);
            next.putExtra("KEY_DATE", date);

            startActivity(next);


            Toast.makeText(context, R.string.ArticleSaved,
                    Toast.LENGTH_LONG).show();

        });


    }


}








