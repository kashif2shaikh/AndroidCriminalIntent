package com.example.kshaikh.criminalintent;

import android.app.ListFragment;
import android.os.Bundle;

/**
 * Created by kshaikh on 15-06-04.
 */
public class CrimeListFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
    }
}
