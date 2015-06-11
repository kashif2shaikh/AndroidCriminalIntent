package com.example.kshaikh.criminalintent.models;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kshaikh on 15-06-04.
 */
public class CrimeLab {

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private ArrayList<Crime> mCrimes;
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private CriminalIntentJSONSerializer mSerializer;

    private CrimeLab(Context appContext)
    {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        }catch(Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }
        //mCrimes = new ArrayList<Crime>();
        //generateCrimes();
    }

    private void generateCrimes()
    {
        for(int i = 0; i < 10; i++) {
            Crime c = new Crime();
            c.setTitle("Crime #" + i);
            c.setSolved(i % 2 == 0); // Every other one
            mCrimes.add(c);
        }
    }

    public static CrimeLab get(Context c) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for(Crime c : mCrimes) {
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }
    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
    }

    public boolean saveCrimes() {
     try {
         mSerializer.saveCrimes(mCrimes);
         Log.d(TAG, "crimes saved to file");
         return true;

     }catch (Exception e) {
         Log.e(TAG, "Error saving crimes: ", e);
         return false;
     }
    }

//    public void setCrimes(ArrayList<Crime> crimes) {
//        mCrimes = crimes;
//    }
}
