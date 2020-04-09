package com.example.groupproject1.NASAImageDay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.groupproject1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private ImageView img;
    private TextView titleTxt;
    private TextView explainTxt;
    private String ACTIVITY_NAME = "Details Fragment";
    String query;
    int id;
    ObjectHolder o;
    DataBase dbAdapter;


    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                getActivity().finish();
                // Handle the back button event
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image,container,false);
        img = view.findViewById(R.id.imgView);
        titleTxt = view.findViewById(R.id.titleText);
        explainTxt = view.findViewById(R.id.explainText);
        dbAdapter = new DataBase(this.getActivity().getApplicationContext());
        o  = new ObjectHolder();

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getBundleExtra("bundle");


        query = b.getString("query");
        id = (int)b.getLong("ID");

        new nasaQuery(this.getActivity(), o, img, titleTxt, explainTxt, 2, dbAdapter.getTitle(id), dbAdapter.getDate(id), dbAdapter.getDetail(id)).execute(query);
        if(o.getImg()!=null){

            img.setImageBitmap(o.getImg());
        }

        return view;
    }//end of onCreateView


}
