package com.thealphadevelopers.walkman.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.R;


// THIS CLASS/ACTIVITY IS USED TO SELECT THE PREFERRED LANGUAGES IN WHICH USER IS WILLING TO
// LISTEN SONGS AND STORE THAT DATA IN SHARED PREFERENCES FOR BETTER USER-EXPERIENCE

public class UserPrefLanguagesActivity extends AppCompatActivity {

    // DECLARING OBJECTS AND VARIABLES
    private ConstraintLayout hindiSongs;
    private ConstraintLayout englishSongs;
    private ConstraintLayout punjabiSongs;
    private ConstraintLayout spanishSongs;
    private ConstraintLayout arablicSongs;

    private ImageView checkMarkForHindiSongs;
    private ImageView checkMarkForEnglishSongs;
    private ImageView checkMarkForPunjabiSongs;
    private ImageView checkMarkForSpanishSongs;
    private ImageView checkMarkForArabicSongs;

    private ConstraintLayout nextButton;
    private ProgressBar progressBar;

    private boolean isHindiSelected = false;
    private boolean isEnglishSelected = false;
    private boolean isPunjabiSelected = false;
    private boolean isSpanishSelected = false;
    private boolean isArabicSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Application-Status","[ INFO  ] UserPrefLanguages onCreate Method Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pref_languages);

        // FETCHING ELEMENTS FROM XML LAYOUT
        hindiSongs = findViewById(R.id.hindi_songs_container);
        englishSongs = findViewById(R.id.english_songs_container);
        punjabiSongs = findViewById(R.id.punjabi_songs_container);
        spanishSongs = findViewById(R.id.spanish_songs_container);
        arablicSongs = findViewById(R.id.arabic_songs_container);

        checkMarkForHindiSongs = findViewById(R.id.check_mark_for_hindi_songs);
        checkMarkForEnglishSongs = findViewById(R.id.check_mark_for_english_songs);
        checkMarkForPunjabiSongs = findViewById(R.id.check_mark_for_punjabi_songs);
        checkMarkForSpanishSongs = findViewById(R.id.check_mark_for_spanish_songs);
        checkMarkForArabicSongs = findViewById(R.id.check_mark_for_arabic_songs);

        nextButton = findViewById(R.id.next_button);
        progressBar = findViewById(R.id.next_btn_progressBar);

        // SETTING EVENT LISTENERS ON HINDI SONGS TYPE CONTAINER
        hindiSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isHindiSelected) {
                    isHindiSelected = true;
                    checkMarkForHindiSongs.setVisibility(View.VISIBLE);
                }
                else {
                    isHindiSelected = false;
                    checkMarkForHindiSongs.setVisibility(View.GONE);
                }
            }
        });

        // SETTING EVENT LISTENERS ON ENGLISH SONGS TYPE CONTAINER
        englishSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEnglishSelected) {
                    isEnglishSelected = true;
                    checkMarkForEnglishSongs.setVisibility(View.VISIBLE);
                }
                else {
                    isEnglishSelected = false;
                    checkMarkForEnglishSongs.setVisibility(View.GONE);
                }
            }
        });

        // SETTING EVENT LISTENERS ON PUNJABI SONGS TYPE CONTAINER
        punjabiSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPunjabiSelected) {
                    isPunjabiSelected = true;
                    checkMarkForPunjabiSongs.setVisibility(View.VISIBLE);
                }
                else {
                    isPunjabiSelected = false;
                    checkMarkForPunjabiSongs.setVisibility(View.GONE);
                }
            }
        });

        // SETTING EVENT LISTENERS ON SPANISH SONGS TYPE CONTAINER
        spanishSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSpanishSelected) {
                    isSpanishSelected = true;
                    checkMarkForSpanishSongs.setVisibility(View.VISIBLE);
                }
                else {
                    isSpanishSelected = false;
                    checkMarkForSpanishSongs.setVisibility(View.GONE);
                }
            }
        });

        // SETTING EVENT LISTENERS ON ARABIC SONGS TYPE CONTAINER
        arablicSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isArabicSelected) {
                    isArabicSelected = true;
                    checkMarkForArabicSongs.setVisibility(View.VISIBLE);
                }
                else {
                    isArabicSelected = false;
                    checkMarkForArabicSongs.setVisibility(View.GONE);
                }
            }
        });

        // SETTING EVENT LISTENERS ON NEXT BUTTON, TO PROCEED FURTHER
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextButton.setClickable(false);     // DISABLING BUTTON FROM FURTHER CLICKS
                progressBar.setVisibility(View.VISIBLE);    // ENABLING PROGRESS BAR

                // INSERTING USER-PREFERRED LANGUAGES IN ARRAY-LIST
                if( isHindiSelected )   MPState.userInfo.addLanguage("hindi");
                if( isEnglishSelected ) MPState.userInfo.addLanguage("english");
                if( isPunjabiSelected ) MPState.userInfo.addLanguage("punjabi");
                if( isSpanishSelected ) MPState.userInfo.addLanguage("spanish");
                if( isArabicSelected )  MPState.userInfo.addLanguage("arabic");
                // CHECKING FOR NO-LANGUAGE SELECTED CONDITION BY THE USER
                if( MPState.userInfo.getLanguages() == null
                        || MPState.userInfo.getLanguages().size() == 0 ) {
                    Toast errorMsgToast =Toast.makeText(UserPrefLanguagesActivity.this,
                            "Please select at least one language, to proceed further",
                            Toast.LENGTH_SHORT);

                    // DISPLAYING ERROR MSG TOAST TO USER
                    errorMsgToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                    errorMsgToast.show();

                    nextButton.setClickable(true);     // DISABLING BUTTON FROM FURTHER CLICKS
                    progressBar.setVisibility(View.GONE);    // ENABLING PROGRESS BAR
                    return;
                }
                // SETTING FIRST-RUN STATE TO FALSE, FOR DIRECTLY ROUTE TO MAIN-ACTIVITY FROM
                // LAUNCHER-ACTIVITY
                MPState.userInfo.setFirstRun(false);
                MPState.save(UserPrefLanguagesActivity.this);   // SAVE MPState TO SHARED-PREFERENCES

                // ROUTING TO MAIN-ACTIVITY
                startActivity(new Intent(UserPrefLanguagesActivity.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                finish();

                nextButton.setClickable(true);     // DISABLING BUTTON FROM FURTHER CLICKS
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("Application-Status","[ INFO  ] UserPrefLanguages onBackPressed Method Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Application-Status","[ INFO  ] UserPrefLanguages onPause Method Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Application-Status","[ INFO  ] UserPrefLanguages onResume Method Called");
        // LOADING CHECK MARKED STATE AGAIN
        if(isHindiSelected)     checkMarkForHindiSongs.setVisibility(View.VISIBLE);
        if(isEnglishSelected)   checkMarkForEnglishSongs.setVisibility(View.VISIBLE);
        if(isPunjabiSelected)   checkMarkForPunjabiSongs.setVisibility(View.VISIBLE);
        if(isSpanishSelected)   checkMarkForSpanishSongs.setVisibility(View.VISIBLE);
        if(isArabicSelected)    checkMarkForArabicSongs.setVisibility(View.VISIBLE);
    }
}
