package com.example.kshaikh.criminalintent.views.crime_details;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.kshaikh.criminalintent.R;
import com.example.kshaikh.criminalintent.models.Crime;
import com.example.kshaikh.criminalintent.models.CrimeLab;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_ID = "com.example.kshaikh.criminalintent.crime_id";
    private static final String DATEPICKER_FRAGMENT = "datepicker";
    private static final int DATEPICKER_REQUEST = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;

    public CrimeFragment() {
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if(NavUtils.getParentActivityName(activity) != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mCrime.setTitle(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // nothing
                getActivity().setTitle(editable);
            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();

        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDatePicker();
            }
        });

        mSolvedCheckbox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    private void showDatePicker()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
        
        // setting this fragment as the target to handle responses.
        dialog.setTargetFragment(CrimeFragment.this, DATEPICKER_REQUEST);
        dialog.show(fm, DATEPICKER_FRAGMENT);
    }

    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == DATEPICKER_REQUEST) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate()
    {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }
}
