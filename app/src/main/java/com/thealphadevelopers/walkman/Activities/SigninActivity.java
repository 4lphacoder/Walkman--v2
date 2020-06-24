package com.thealphadevelopers.walkman.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thealphadevelopers.walkman.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.thealphadevelopers.walkman.MPState;

// THIS CLASS/ACTIVITY FETCHES USER EMAIL ADDRESS, DISPLAY-NAME FROM OAUTH SERVICES LIKE :-
// GOOGLE OAUTH SERVICE AND FACEBOOK OAUTH SERVICE. AFTER SUCCESSFUL FETCH IT STORES THEM IN
// SHARED PREFERENCE FILE AND ROUTE TO OTHER INFORMATION FETCHING ACTIVITY LIKE:-
// FAVOURITE ARTISTS, USER-PREFERRED LANGUAGE SONGS.

public class SigninActivity extends AppCompatActivity {

    // DECLARING OBJECTS/VARIABLES
    private static final int RC_SIGN_IN = 1000;     // REQUEST-CODE OF GOOGLE SIGN-IN INTENT
    private LinearLayout SignInWithGoogle;          // USED AS A BUTTON FOR SIGN-IN WITH GOOGLE SERVICE
    private ProgressBar SignInWithGoogleProgressBar;    // THIS PROGRESS BAR SWAPPED WITH GOOGLE ICON WHEN USER CLICKS ON ABOVE BUTTON
    private ImageView GoogleLogoInSignInWithGoogle;     // GOOGLE-LOGO
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    private LinearLayout SignInWithFacebook;          // USED AS A BUTTON FOR SIGN-IN WITH FACEBOOK SERVICE
    private ProgressBar SignInWithFacebookProgressBar;    // THIS PROGRESS BAR SWAPPED WITH FACEBOOK ICON WHEN USER CLICKS ON ABOVE BUTTON
    private ImageView FacebookLogoInSignInWithGoogle;     // FACEBOOK-LOGO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Application-Status","[ INFO  ] SigninActivity onCreate Method Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // FETCHING ELEMENTS FROM XML RESOURCES
        SignInWithGoogle = findViewById(R.id.sign_in_with_google);
        SignInWithGoogleProgressBar = findViewById(R.id.sign_in_with_google_progressBar);
        GoogleLogoInSignInWithGoogle= findViewById(R.id.google_logo);
        SignInWithFacebook = findViewById(R.id.sign_in_with_facebook);
        SignInWithFacebookProgressBar = findViewById(R.id.sign_in_with_facebook_progressBar);
        FacebookLogoInSignInWithGoogle = findViewById(R.id.facebook_logo);

        // CONFIGURE SIGN-IN TO REQUEST THE USER'S ID, EMAIL-ADDRESS, AND BASIC PROFILE.
        // ID AND BASIC PROFILE ARE DISPLAYED IN DEFAULT_SIGN_IN
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // BUILDING A GOOGLE SIGN-IN CLIENT WITH THE OPTIONS SPECIFIED IN GSO
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // SETTING ON-CLICK LISTENER ON SignInWithGoogleLinearLayout
        SignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInWithGoogle.setClickable(false);   // TO AVOID MULTIPLE CLICKS BY USER
                SignInWithGoogleProgressBar.setVisibility(View.VISIBLE);    // SHOWING PROGRESS-BAR
                GoogleLogoInSignInWithGoogle.setVisibility(View.GONE);      // HIDING GOOGLE-LOGO
                SignInWithGoogle();
            }
        });

        // SETTING ON-CLICK LISTENER ON SignInWithFacebookLinearLayout
        SignInWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInWithFacebook.setClickable(false);   // TO AVOID MULTIPLE CLICKS BY USER
                SignInWithFacebookProgressBar.setVisibility(View.VISIBLE);    // SHOWING PROGRESS-BAR
                FacebookLogoInSignInWithGoogle.setVisibility(View.GONE);      // HIDING GOOGLE-LOGO
                SignInWithFacebook();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // CHECK FOR EXISTING GOOGLE SIGN-IN ACCOUNT, IF THE USER IS ALREADY SIGNED IN
        // THE GoogleSignInAccount WILL NOT BE NULL
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            // EXECUTES WHEN USER ALREADY SIGNED-IN
            // PROCEEDING TO MAIN-ACTIVITY
            MPState.userInfo.setFirstRun(true);
            MPState.save(this);
            // PROCEEDING TO THE NEXT-ACTIVITY
            startActivity(new Intent(this, UserPrefLanguagesActivity.class));
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            finish();
        }
    }

    private void SignInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInWithGoogleResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // SIGNED IN SUCCESSFULLY SHOWING AUTHENTICATED UI.
            Log.d("Application-Status","User logged-in via Google Oauth2.0 service");

            MPState.userInfo.setEmailAddress(account.getEmail());
            MPState.userInfo.setUserName(account.getDisplayName());
            MPState.userInfo.setUserAvatarFromURI(account.getPhotoUrl(),this);
            MPState.save(this);
            // PROCEEDING TO THE NEXT-ACTIVITY
            startActivity(new Intent(this, UserPrefLanguagesActivity.class));
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
            finish();

        } catch (ApiException e) {
            Log.d("Application-Status", "signInResult:failed code=" + e.getStatusCode());
            // DISPLAYING ERROR-TOAST-MSG TO USER
            Toast errorMsgToast = Toast.makeText(this,"Unable to sign-in user, please try again.",Toast.LENGTH_SHORT);
            errorMsgToast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,400);
            errorMsgToast.show();
        }
    }

    public void SignInWithFacebook() {
        // THIS FEATURE CURRENTLY UNAVAILABLE
        Toast errorMsgToast = Toast.makeText(this,
                "Sorry, this feature is currently unavailable, please try to sign-in with google",
                Toast.LENGTH_SHORT);
        errorMsgToast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,400);
        errorMsgToast.show();
        SignInWithFacebook.setClickable(true);   // ENABLING BUTTON BACK TO ORIGINAL STATE
        SignInWithFacebookProgressBar.setVisibility(View.GONE);    // HIDING PROGRESS-BAR
        FacebookLogoInSignInWithGoogle.setVisibility(View.VISIBLE);      // SHOWING FACEBOOK-LOGO
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RESULT RETURNED FROM LAUNCHING THE INTENT FROM GoogleSignInClient.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInWithGoogleResult(task);
            SignInWithGoogleProgressBar.setVisibility(View.GONE);       // HIDING PROGRESS-BAR AGAIN
            GoogleLogoInSignInWithGoogle.setVisibility(View.VISIBLE);   // DISPLAYING GOOGLE-LOGO BACK
            SignInWithGoogle.setClickable(true);        // MAKING SignInWithGoogle BUTTON CLICKABLE AGAIN
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("Application-Status","[ INFO  ] Sign-in onBackPressed Method Called");
    }
}
