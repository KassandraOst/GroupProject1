package com.example.groupproject1.NASAImageDay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.groupproject1.R;

public class EmptyActivity2 extends AppCompatActivity {
    private FragmentTransaction fragTrans;
    private FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        DialogFragment newFragment = new SearchFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();

    }

}
