package com.thealphadevelopers.walkman.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.thealphadevelopers.walkman.Fragments.Home;
import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.R;

// THIS CLASS/ ACTIVITY PRESENTS SPLASH-SCREEN TO THE USER AND IN THAT TIME IT LOADS
// MPState FROM SHARED-PREFERENCES STORED AT THE TIME OF CLOSING THIS APPLICATION

public class LauncherActivity extends AppCompatActivity {

    private int minDelayInLaunch = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Application-Status","[ INFO  ] Launcher Activity onCreate Method Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // CHANGES THE BACKGROUND COLOR OF STATUS BAR (TOP-MOST BAR)
        Window window = getWindow();
        window.setStatusBarColor( getResources().getColor(R.color.colorAccent));

        // LOADING MPState from SharedPreferences
        MPState.load(this);
        // GENERATING DEFAULT FRAGMENTS
        MPState.homeFragment = new Home();
        MPState.exploreMusicFragment = new Home();
        // MPState.exploreMusicFragment = new ExploreMusicFragment();

        // APPLICATION INITIALIZATION COMPLETES, PROCEEDING TO FURTHER ACTIVITIES
        if( MPState.userInfo.isFirstRun() ) {
            // ROUTE TO INFORMATION COLLECTING ACTIVITIES
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // START INFORMATION COLLECTING ONE TIME ACTIVITIES SO THAT WE GOT THE TASTE OF
                    // OUR USER AND STORE HIS/HER PLAYLISTS OR HISTORY ACCORDINGLY
                    Log.d("Application-Status","[ INFO  ] Routing to information collecting activities");
                    startActivity(new Intent(LauncherActivity.this, SigninActivity.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    finish();
                }
            },minDelayInLaunch);
        }
        else {
            // ROUTE TO MAIN-ACTIVITY
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // STARTING MAIN-ACTIVITY
                    Log.d("Application-Status","[ INFO  ] Routing to main activity");
                    startActivity(new Intent(LauncherActivity.this,MainActivity.class));
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    finish();
                }
            },minDelayInLaunch);
        }
    }

}
