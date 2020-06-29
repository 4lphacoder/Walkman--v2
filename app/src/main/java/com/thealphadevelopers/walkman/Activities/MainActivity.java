package com.thealphadevelopers.walkman.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.Models.MediaMetadata;
import com.thealphadevelopers.walkman.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thealphadevelopers.walkman.Services.MPService;


public class MainActivity extends AppCompatActivity {

    // DECLARING OBJECTS
    private int SelectedNavigationBtn;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // STARTING MEDIA-PLAYER-SERVICE
        startService(new Intent(this,MPService.class));

        // DISPLAYING DEFAULT FRAGMENT
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,MPState.homeFragment)
                .commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.home_btn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.play_pause_button:
                        // TOGGLING FROM PLAYING STATE TO PAUSE STATE
                        if ( MPState.mediaPlayerService.getCurrentState() == MPService.PLAYING_STATE )
                            MPState.mediaPlayerService.pause();
                            // TOGGLING FROM PAUSE STATE TO PLAYING STATE
                        else if ( MPState.mediaPlayerService.getCurrentState() == MPService.PAUSED_STATE )
                            MPState.mediaPlayerService.play();
                            // TOGGLING FROM IDLE STATE TO LOADING STATE
                        else if ( MPState.mediaPlayerService.getCurrentState() == MPService.IDLE_STATE )
                            MPState.mediaPlayerService.changeNext(MainActivity.this);
                        else if ( MPState.mediaPlayerService.getCurrentState() == MPService.FINISHED_STATE )
                            MPState.mediaPlayerService.changeNext(MainActivity.this);
                        else if ( MPState.mediaPlayerService.getCurrentState() == MPService.NULL_STATE )
                            MPState.mediaPlayerService.changeNext(MainActivity.this);
                        break;

                    case R.id.home_btn:
                        selectedFragment = MPState.homeFragment;
                        break;

                    case R.id.explore_btn:
                        selectedFragment = MPState.exploreMusicFragment;
                        break;
                }

                if( SelectedNavigationBtn == menuItem.getItemId() || selectedFragment == null )
                    return false;

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,selectedFragment)
                        .addToBackStack(null)
                        .commit();
                SelectedNavigationBtn = menuItem.getItemId();
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if( SelectedNavigationBtn == R.id.explore_btn ) {
            EditText searchBar = findViewById(R.id.search_bar);
            if( searchBar.hasFocus() ) {
                searchBar.clearFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MPState.mediaPlayerService.stop();
        MPState.save(this);
    }
}
