package com.example.kshaikh.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class CrimeActivity extends AppCompatActivity {

    private FragmentManager mFm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        mFm = getFragmentManager();
        addCrimeFragment();
    }

    private void addCrimeFragment()
    {
        Fragment fragment = mFm.findFragmentById(R.id.fragmentContainer);
        if(fragment == null) {
            // This looks confusing, but when activity is re-created on rotation for instance, the fragments
            // are not destroyed
            fragment = new CrimeFragment();
            mFm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    private CrimeFragment getCrimeFragment()
    {
        Fragment fragment = mFm.findFragmentById(R.id.fragmentContainer);
        if(fragment != null) {
            return (CrimeFragment)fragment;
        }
        return null;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
