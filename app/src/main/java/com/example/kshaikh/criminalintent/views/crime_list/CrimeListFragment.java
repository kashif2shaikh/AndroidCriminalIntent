package com.example.kshaikh.criminalintent.views.crime_list;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
    private Callbacks mCallbacks;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.crime_list_fragment, parent, false);
        View v = super.onCreateView(inflater, parent, savedInstanceState); // if u don't want to have empty view, use this.
        showSubtitle(mSubtitleVisible);

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                // Required, but not used yet.
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.crime_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false; // Required but not used
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_delete_crime:
                        // This goes through all items of the listview that are checked and deletes them
                        deleteCheckedCrimes();
                        actionMode.finish();
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        return v;
    }
    private void deleteCheckedCrimes() {
        CrimeAdaptor adapter = (CrimeAdaptor)getListAdapter();
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        for(int i = adapter.getCount() - 1;i >= 0; i--) {
            if(getListView().isItemChecked(i)) {
                Crime crime = adapter.getItem(i);
                crimeLab.deleteCrime(crime);
                mCallbacks.onCrimeDeleted(crime);
            }
        }
        updateUI();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = ((CrimeAdaptor)getListAdapter()).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked");

        mCallbacks.onCrimeSelected(c);
//        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
//        startActivity(i);
    }

    public void updateUI() {
        ((CrimeAdaptor)getListAdapter()).notifyDataSetChanged();
    }

    private class CrimeAdaptor extends ArrayAdapter<Crime> {
        public CrimeAdaptor(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.crime_list_item, null);
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
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_fragment, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if(showSubtitle != null && mSubtitleVisible) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

//    @Override GINGERBREAD ONLY
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_new_crime:
                createNewCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                showSubtitleItemSelected(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void createNewCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        updateUI();
        mCallbacks.onCrimeSelected(crime);

//        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//        startActivityForResult(i, 0);
    }

    private void showSubtitleItemSelected(MenuItem item) {
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

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
        void onCrimeDeleted(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
