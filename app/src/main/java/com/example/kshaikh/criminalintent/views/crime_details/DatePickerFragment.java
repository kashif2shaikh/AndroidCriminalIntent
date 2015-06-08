package com.example.kshaikh.criminalintent.views.crime_details;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import com.example.kshaikh.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.example.kshaikh.criminalintent.date";

    private Date mDate;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);

        final Calendar c = Calendar.getInstance();
        c.setTime(mDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePicker picker = (DatePicker)getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        picker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                // Translate year/month/day to date object
                mDate = new GregorianCalendar(year, month, day).getTime();

                // Update argument to preserve selected value on rotation.
                // NOTE: This doesn't send results back to caller. We could also use
                // RETAINED fragments so we don't have to deal with this mess but BNR
                // says this is buggy, for some reason.
                getArguments().putSerializable(EXTRA_DATE, mDate);

                // NOTE2: While we could send result back to target fragment, that is not good, as
                // the dialog fragment can be dismissed (either by clicking cancel or tapping outside
                // the frame).
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(picker)
                .setTitle(R.string.crime_datepicker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
    private void sendResult(int resultCode) {

        // Caller must have set themselves as the target fragment.
        if(getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }


}
