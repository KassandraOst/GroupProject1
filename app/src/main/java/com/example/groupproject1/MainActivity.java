package com.example.groupproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button newsButton;
    Button imgDayButton;
    Button earthButton;
    Button BBCButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiates all the buttons to the appropriate views
        newsButton = findViewById(R.id.mButton1);
        imgDayButton = findViewById(R.id.mButton2);
        earthButton = findViewById(R.id.mButton3);
        BBCButton = findViewById(R.id.mButton4);

        // When you click mButton1 it will send you to the first activity,Guardian news article search.
        newsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
 //TODO: REPLACE (CLASS) WITH THE APPROPRIATE CLASS AND REMOVE COMMENT MARKS
                /*Intent goToGuardianActivity = new Intent(v.getContext(), (CLASS).class);
                startActivity(goToGuardianActivity);*/
            }
        });//End newsButton onClickListener

        //When you click mButton2 it will send you to the second activity, NASA Image of the Day
        imgDayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: REPLACE (CLASS) WITH THE APPROPRIATE CLASS AND REMOVE COMMENT MARKS
                /*Intent goToImgDay = new Intent(v.getContext(), (CLASS).class);
                startActivity(goToImgDay);*/
            }
        });//End imgDayButton onClickListener

        //When you click mButton3 it will send you to the third activity, Earth Imagery Database
        earthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: REPLACE (CLASS) WITH THE APPROPRIATE CLASS AND REMOVE COMMENT MARKS
                /*Intent goToEarthImg = new Intent(v.getContext(), (CLASS).class);
                startActivity(goToEarthImg);*/
            }
        });//End earthButton onClickListener

        //When you click mButton4 it will send you to the fourth activity, BBC News Reader
        BBCButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: REPLACE (CLASS) WITH THE APPROPRIATE CLASS AND REMOVE COMMENT MARKS
                /*Intent goToBBCNews = new Intent(v.getContext(), (CLASS).class);
                startActivity(goToBBCNews);*/
            }
        });//End BBCButton onClickListener

    }//End OnCreate

}//End Main Activity
