package com.example.kshaikh.criminalintent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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
