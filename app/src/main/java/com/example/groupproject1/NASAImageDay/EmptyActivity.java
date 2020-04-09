package com.example.groupproject1.NASAImageDay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.groupproject1.R;

public class EmptyActivity extends AppCompatActivity {
    private FragmentTransaction fragTrans;
    private FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        fragManager = getSupportFragmentManager();
        fragManager.popBackStackImmediate();
        ImageFragment aFragment = new ImageFragment();
        fragTrans = fragManager.beginTransaction();
        fragTrans.replace(R.id.imgFrame, aFragment);
        fragTrans.addToBackStack(null);
        fragTrans.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();

    }

}
