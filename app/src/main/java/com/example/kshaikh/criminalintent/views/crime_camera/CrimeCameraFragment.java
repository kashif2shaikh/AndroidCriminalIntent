package com.example.kshaikh.criminalintent.views.crime_camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kshaikh.criminalintent.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by kshaikh on 15-06-12.
 */
public class CrimeCameraFragment extends Fragment implements SurfaceHolder.Callback, Camera.PictureCallback, Camera.ShutterCallback {
    private static final String TAG = "CrimeCameraFragment";

    public static final String EXTRA_PHOTO_FILENAME = "com.example.kshaikh.criminalintent.photo_filename";

    private Camera mCamera; // deprecated in API21, use Camera2 in API21+
    private SurfaceView mSurfaceView;
    private View mProgressContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.crime_camera_fragment, parent, false);

        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCamera == null)
                    return;
                mCamera.takePicture(CrimeCameraFragment.this, null, CrimeCameraFragment.this);
            }
        });

        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(this);

        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            mCamera = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera is not available or does not exist", e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(mCamera == null)
            return;
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
        }catch (IOException exception) {
            Log.e(TAG, "Error setting up preview display", exception);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        if(mCamera == null)
            return;

        Camera.Parameters params = mCamera.getParameters();
        Size s = getBestSupportedSize(params.getSupportedPreviewSizes(), w, h);
        params.setPreviewSize(s.width, s.height);

        s = getBestSupportedSize(params.getSupportedPictureSizes(), w, h);
        params.setPictureSize(s.width, s.height);

        mCamera.setParameters(params);
        try {
            mCamera.startPreview();
        }catch(Exception e) {
            Log.e(TAG, "Could not start preview", e);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(mCamera == null)
            return;

        mCamera.stopPreview();
    }

    // Picks the largest size to use
    // width, height are ignored.
    private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
        Size largestSize = sizes.get(0);
        int largestArea = largestSize.width * largestSize.height;
        for(Size s : sizes) {
            int area = s.width * s.height;
            if(area > largestArea) {
                largestSize = s;
                largestArea = area;
            }
        }
        return largestSize;
    }

    @Override
    public void onShutter() {
        mProgressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        String filename = UUID.randomUUID().toString() + ".jpg";

        FileOutputStream os = null;
        boolean success = true;
        try {
            os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            os.write(data);

        }catch(Exception e) {
            Log.e(TAG, "Error writing to a file " + filename, e);
            success = false;
        }finally {
            try {
                if(os != null)
                    os.close();

            }catch(Exception e) {
                Log.e(TAG, "Error closing file " + filename, e);
                success = false;
            }
        }
        if(success) {
            Log.i(TAG, "JPEG saved at " + filename);
            Intent i = new Intent();
            i.putExtra(EXTRA_PHOTO_FILENAME, filename);
            getActivity().setResult(Activity.RESULT_OK, i);
        }
        else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        getActivity().finish();
    }


}
