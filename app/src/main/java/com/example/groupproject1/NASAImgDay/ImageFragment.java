package com.example.groupproject1.NASAImgDay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.groupproject1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private ImageView img;
    private String ACTIVITY_NAME = "Details Fragment";
    String query;
    int pos;
    ObjectHolder o = new ObjectHolder();
    DataBase dbAdapter;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DataBase(this.getActivity());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image,container,false);
        img = view.findViewById(R.id.imgView);


        Intent intent = getActivity().getIntent();
        Bundle b = intent.getBundleExtra("bundle");


        query = b.getString("query");
        pos = (int)b.getLong("ID");

        new nasaQuery(this.getContext(), o, true, dbAdapter.getTitle(pos), dbAdapter.getDate(pos)).execute(query);

        Log.i("BundleQuery: ", dbAdapter.getTitle(pos));
        Log.i("BundleQuery: ", dbAdapter.getDate(pos));
        img.setImageBitmap(o.getImg());


        return view;
    }//end of onCreateView

}
