package com.example.kshaikh.criminalintent.views.crime_list;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kshaikh.criminalintent.R;
import com.example.kshaikh.criminalintent.models.Crime;
import com.example.kshaikh.criminalintent.models.CrimeLab;
import com.example.kshaikh.criminalintent.views.crime_details.CrimeFragment;
import com.example.kshaikh.criminalintent.views.crime_details.CrimePagerActivity;

import java.util.ArrayList;

/**
 * Created by kshaikh on 15-06-04.
 */
public class CrimeListFragment extends ListFragment {
    private static final String TAG = "CrimeListFragment";

    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible = false; // ActionBar sub-title state not saved on rotation.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        CrimeAdaptor adapter = new CrimeAdaptor(mCrimes);

        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, parent, false);
        //View v = super.onCreateView(inflater, container, savedInstanceState); // if u don't want to have empty view, use this.
        showSubtitle(mSubtitleVisible);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = ((CrimeAdaptor)getListAdapter()).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked");

        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);
    }

    private class CrimeAdaptor extends ArrayAdapter<Crime> {
        public CrimeAdaptor(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            Crime c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());

            CheckBox solvedCheckbox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckbox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdaptor)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if(showSubtitle != null && mSubtitleVisible) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
                return true;
            case R.id.menu_item_show_subtitle:
                ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
                if(bar.getSubtitle() == null) {
                    mSubtitleVisible = true;
                    showSubtitle(mSubtitleVisible);
                    item.setTitle(R.string.hide_subtitle);
                }
                else {
                    mSubtitleVisible = false;
                    showSubtitle(mSubtitleVisible);
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showSubtitle(boolean value)
    {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(value) {
            bar.setSubtitle(R.string.subtitle);
        }
        else {
            bar.setSubtitle(null);
        }
    }
}
