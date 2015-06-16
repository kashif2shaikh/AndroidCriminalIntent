package com.example.kshaikh.criminalintent.views.crime_list;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.kshaikh.criminalintent.R;
import com.example.kshaikh.criminalintent.common.MasterDetailFragmentActivity;
import com.example.kshaikh.criminalintent.common.SingleFragmentActivity;
import com.example.kshaikh.criminalintent.models.Crime;
import com.example.kshaikh.criminalintent.views.crime_details.CrimeFragment;
import com.example.kshaikh.criminalintent.views.crime_details.CrimePagerActivity;

import java.util.UUID;


public class CrimeListActivity extends MasterDetailFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    private FragmentManager mFm;
    private UUID selectedCrimeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment()
    {
        // This is master fragment
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(!detailFragmentExists()) {
            // Running on a Phone
            Intent i = new Intent(this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(i);
        }
        else {
            // Running on tablet
            setDetailFragment(CrimeFragment.newInstance(crime.getId()));
        }
        selectedCrimeId = crime.getId();
    }

    @Override
    public void onCrimeDeleted(Crime crime) {
        if(selectedCrimeId == crime.getId()) {
            clearDetailFragment();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment)getFragment();
        listFragment.updateUI();
    }
}
