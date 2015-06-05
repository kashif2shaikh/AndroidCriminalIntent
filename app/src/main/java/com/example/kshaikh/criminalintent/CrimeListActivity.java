package com.example.kshaikh.criminalintent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;


public class CrimeListActivity extends SingleFragmentActivity {

    private FragmentManager mFm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}
