package com.example.groupproject1.NASAImageDay;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.widget.DatePicker;
import java.util.Calendar;


    public class SearchFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            if(year < 2020 && year > 1995 || year == 2020 && (month+1) < 5 && day < 11  ) {
                String date = year + "-" + (month+1) + "-" + day;
                Intent in = new Intent();
                in.putExtra("date", date);
                getActivity().setResult(600, in);
                getActivity().finish();
            } else
            {
                getActivity().setResult(500);
                getActivity().finish();

            }
        }
    }
//}