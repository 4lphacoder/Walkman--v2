package com.thealphadevelopers.walkman.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.Models.MediaMetadata;
import com.thealphadevelopers.walkman.Models.PlayerQueue;
import com.thealphadevelopers.walkman.R;
import com.thealphadevelopers.walkman.Services.MPService;

// THIS FRAGMENT PRESENTS VARIOUS PLAYLISTS LIKE FAVOURITE SONGS, MOST-LISTENED SONGS, LANGUAGE
// CATEGORISED SONGS, SONGS BASED ON MOOD OF USER, VARIOUS PLAYLISTS OF FAVOURITE ARTISTS OF USER, ETC.

public class Home extends Fragment {

    private TextView personalisedGreeting;
    private ScrollView rootScrollView;
    private HorizontalScrollView recommendationScrollView;
    private HorizontalScrollView moodsCatScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Application-Status","[ INFO  ] Home-Fragment onCreate Method Called");
        View homeFragment = inflater.inflate(R.layout.home_fragment,container,false);

        // FETCHING RESOURCES FROM HOME-FRAGMENT LAYOUT
        personalisedGreeting = homeFragment.findViewById(R.id.personalised_greeting);
        rootScrollView = homeFragment.findViewById(R.id.root_scroll_view);
        recommendationScrollView = homeFragment.findViewById(R.id.recommendation_based_on_user_taste);
        moodsCatScrollView = homeFragment.findViewById(R.id.moods_categories);

        // INITIALISING LAYOUT
        personalisedGreeting.setText("Hi There, " + MPState.userInfo.getUserFirstName() + ".");
        rootScrollView.smoothScrollTo( MPState.homeRootScrollX,MPState.homeRootScrollY );
        recommendationScrollView.smoothScrollTo( MPState.homeRecommendationScrollX,MPState.homeRecommendationScrollY);
        moodsCatScrollView.smoothScrollTo( MPState.homeMoodsCatScrollX,MPState.homeMoodsCatScrollY );

        return homeFragment;
    }

    @Override
    public void onResume() {
        Log.d("Application-Status","[ INFO  ] Home-Fragment onResume Method Called");

        // SCROLLING USER-INTERFACE BACK TO WHERE USER LEAVES
        rootScrollView.smoothScrollTo( MPState.homeRootScrollX,MPState.homeRootScrollY );
        recommendationScrollView.smoothScrollTo( MPState.homeRecommendationScrollX,MPState.homeRecommendationScrollY);
        moodsCatScrollView.smoothScrollTo( MPState.homeMoodsCatScrollX,MPState.homeMoodsCatScrollY );
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("Application-Status","[ INFO  ] Home-Fragment onPause Method Called");
        MPState.homeRootScrollX = rootScrollView.getScrollX();
        MPState.homeRootScrollY = rootScrollView.getScrollY();
        MPState.homeRecommendationScrollX = rootScrollView.getScrollX();
        MPState.homeRecommendationScrollY = rootScrollView.getScrollY();
        MPState.homeMoodsCatScrollX = rootScrollView.getScrollX();
        MPState.homeMoodsCatScrollY = rootScrollView.getScrollY();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Application-Status","[ INFO  ] Home-Fragment onDestroy Method Called");
    }
}

