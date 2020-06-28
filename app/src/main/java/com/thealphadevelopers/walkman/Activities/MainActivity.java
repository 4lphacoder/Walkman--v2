package com.thealphadevelopers.walkman.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    // DECLARING OBJECTS
    private int SelectedNavigationBtn;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Application-Status","[ INFO  ] Main Activity onCreate Method Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        if ( MPState.getCurrentState() == MPState.PLAYING_STATE )
                            MPState.setCurrentState(MPState.PAUSED_STATE);
                            // TOGGLING FROM PAUSE STATE TO PLAYING STATE
                        else if ( MPState.getCurrentState() == MPState.PAUSED_STATE )
                            MPState.setCurrentState(MPState.PLAYING_STATE);
                        else if ( MPState.getCurrentState() == MPState.IDLE_STATE ) {
                            MPState.setCurrentState(MPState.LOADING_STATE);
//                            minimalMC.fetchResourceURIFromVideoId(MPState.metadataOfCP.getVideoID(), MPState.audioQualityPreference);
                        }
                        break;
                    case R.id.home_btn:
                        selectedFragment = MPState.homeFragment;
                        break;
                    case R.id.explore_btn:
                        selectedFragment = MPState.exploreMusicFragment;
                        break;
                    case R.id.my_music_btn:
                        selectedFragment = MPState.homeFragment;
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
        Log.d("Application-Status","Main Activity OnBackPressed Method called");
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        Log.d("Application-Status","[ INFO  ] Main Activity onDestroy Method Called");
        super.onDestroy();
        MPState.save(this);
//        stopService(new Intent(this, MPService.class));
        MPState.audioManager.abandonAudioFocus(MPState.audioFocusChangeListener);
    }
}
