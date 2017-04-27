package com.ryx.ethan.fingerlessbrowser;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.PeopleDet;
import com.tzutalin.dlib.VisionDetRet;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BlinkDetectFragment.OnBlinkListener{
    //BrowserFragment myBrowserFragment;
    // ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BrowserFragment myBrowserFragment = (BrowserFragment) getFragmentManager().
                findFragmentById(R.id.browserFragment);
        if (myBrowserFragment==null || ! myBrowserFragment.isInLayout()) {
            // start new Activity
        }
        else {
            //fragment.update(...);
        }

        add(BlinkDetectFragment, "myBlinkDetectFragement");

        BlinkDetectFragment myBlinkDetectFragement = (BlinkDetectFragment) getFragmentManager().
                findFragmentById(R.id.browserFragment);
        if (myBlinkDetectFragement==null || ! myBlinkDetectFragement.isInLayout()) {
            // start new Activity
        }
        else {
            //fragment.update(...);
        }


        if (savedInstanceState == null) {
            getFragmentManager().add(BlinkDetectFragment, "myBlinkDetectFragement");
        }
    }

    public void onBlinked(int count) {
        //notify browser
    }

}
