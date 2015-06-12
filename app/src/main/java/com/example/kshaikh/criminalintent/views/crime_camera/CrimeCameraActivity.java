package com.example.kshaikh.criminalintent.views.crime_camera;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.example.kshaikh.criminalintent.common.SingleFragmentActivity;

/**
 * Created by kshaikh on 15-06-12.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // must be done before super call, and can't be done in fragment obviously
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        // must be done after create
        getSupportActionBar().hide();
    }


    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
