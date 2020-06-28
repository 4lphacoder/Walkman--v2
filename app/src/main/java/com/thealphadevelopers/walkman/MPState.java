package com.thealphadevelopers.walkman;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.fragment.app.Fragment;

import com.thealphadevelopers.walkman.Models.MediaMetadata;
import com.thealphadevelopers.walkman.Models.PlayerQueue;
import com.thealphadevelopers.walkman.Models.UserInfo;
import com.thealphadevelopers.walkman.Models.Youtube.Search;
import java.util.ArrayList;


// THIS BASE-CLASS ACT AS A WRAPPER CLASS FOR THE WHOLE APPLICATION
// THIS CLASS LOADED AT THE LAUNCH OF APPLICATION

public class MPState extends Application {
    // ADDING DEBUG TAGS
    public static final String DEBUG_TAG = "application-status";

    // NOTIFICATION CHANNEL ID, FOR CLASSIFYING VARIOUS NOTIFICATIONS SEND BY APPLICATION
    public static final String PLAYBACK_CONTROLS = "PLAYBACK_CONTROLS";


    // THESE VALUES FETCHED FROM SHARED PREFERENCE FILES AND USED TO
    // STORE USER DETAILS AND PLAYER ON CLOSE STATE.
    public static UserInfo userInfo;

    // THESE VALUES ARE USED TO HANDLE ON-GOING STATE OF PLAYER LIKE WHETHER PLAY BTN PRESSED OR PAUSE
    // BTN PRESSED, LIKE STATE OF SONG, TITLE OF SONG, CHANNEL NAME ETC.

    public static MediaMetadata metadataOfCP;      // META-DATA OF CURRENTLY PLAYING MEDIA FILE

    // MEDIA PLAYER ACTUAL STATE HOLDERS
    private static int currentState;
    private static int stateBeforeAudioFocusLoss;
    public static final int IDLE_STATE = 0;
    public static final int LOADING_STATE = 5;
    public static final int PLAYING_STATE = 1;
    public static final int PAUSED_STATE= 2;
    public static final int FINISHED_STATE = 3;
    public static final int NULL_STATE = 6;

    // DEFINING ITAGS OF REQUIRED MEDIA FORMATS
    public static int audioQualityPreference;           // USERS PREFERENCE OF QUALITY OF MEDIA REQUIRED
    public static final int HIGH_QUALITY_AUDIO = 251;
    public static final int MEDIUM_QUALITY_AUDIO = 250;
    public static final int LOW_QUALITY_AUDIO = 249;

    // SOME OTHER MEDIA-PLAYER FEATURES LIKE QUEUE,SHUFFLE-STATE, ETC.
    public static Search lastYoutubeSearchResp;
    public static int exploreMusicScrollX;
    public static int exploreMusicScrollY;
    public static int homeRootScrollX;
    public static int homeRootScrollY;
    public static int homeRecommendationScrollX;
    public static int homeRecommendationScrollY;
    public static int homeMoodsCatScrollX;
    public static int homeMoodsCatScrollY;
    public static PlayerQueue playerQueue = new PlayerQueue();


    // CREATING A MEDIA PLAYER OBJECT
    public static Fragment homeFragment;
    public static Fragment exploreMusicFragment;
    public static AudioManager audioManager;
    public static MediaPlayer mediaPlayer;
    private static MPStateChangeListener mpStateChangeListener;
    public static int getCurrentState() { return currentState; }
    public static void addMPStateChangeListener(MPStateChangeListener listener ) { mpStateChangeListener = listener; }
    public static void setCurrentState(int value) {
        currentState = value;
        if( mpStateChangeListener != null )     mpStateChangeListener.onStateChanges(value);
        if( value == PAUSED_STATE ) {
            mediaPlayer.pause();
        }
        else if( value == PLAYING_STATE && mediaPlayer != null ) {
            // REQUESTING AUDIO FOCUS
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            int focusRequestResponse;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(audioAttributes)
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(audioFocusChangeListener)
                        .build();

                focusRequestResponse = MPState.audioManager.requestAudioFocus(audioFocusRequest);;
            }
            else {
                focusRequestResponse = MPState.audioManager.requestAudioFocus(
                        audioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
            }
            switch (focusRequestResponse) {
                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                    mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    Log.d("Application-Status","[ ERROR ] Audio focus request failed");
            }
        }
    }

    public static void save(Context context) {
        // STORES THE FOLLOWING IN SHARED PREFERENCES :-
        // 1. this.userInfo
        // 2. this.metaDataOfCP
        // 3. Config.currentlyInUseApiKeyIdx
        // 4. this.audioQualityPreference

        SharedPreferences userInfoSharedPref = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfoSharedPref.edit();
        userInfoEditor.putString("userInfo", userInfo.toString());
        userInfoEditor.commit();
        Log.d("Application-Status","[ INFO  ] Saving in user_info SharedPreferences");

        SharedPreferences onCloseStateSharedPref = context.getSharedPreferences("on_close_state",Context.MODE_PRIVATE);
        SharedPreferences.Editor onCloseStateEditor = onCloseStateSharedPref.edit();
        onCloseStateEditor.putString("lastPlayedSong",metadataOfCP.toString());
        onCloseStateEditor.putInt("currentlyInUseApiKeyIdx",Config.currentlyInUseApiKeyIdx);
        onCloseStateEditor.putInt("audioQualityPreference",audioQualityPreference);
        onCloseStateEditor.commit();
        Log.d("Application-Status","[ INFO  ] Saving in on_close_state SharedPreferences");
    }

    public static void load(Context context) {
        // LOADS THE FOLLOWING FROM SHARED PREFERENCES :-
        // 1. this.userInfo
        // 2. this.metaDataOfCP
        // 3. Config.currentlyInUseApiKeyIdx
        // 4. this.audioQualityPreference


        // FETCHING USER-DETAILS FROM SHARED PREFERENCE (FILE KEY - user_info) LIKE:-
        // 1. userInfo --> STRING (USER-INFO PARSED IN STRING DATA-TYPE)

        // AFTER FETCHING ALL THESE WE STORE THEM IN PUBLIC DATA-MEMBERS OF CLASS - MediaPLayerState
        SharedPreferences userInfoSharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        userInfo = UserInfo.parseUserInfoFromString(
                userInfoSharedPreferences.getString("userInfo",null));

        Log.d("Application-Status","[ INFO  ] Reading SharedPreference - user_info");

        // FETCHING ON-CLOSE-STATE OF MEDIA PLAYER FROM SHARED PREFERENCE (FILE KEY - on_close_state)
        // THIS SHARED PREFERENCE CONTAINS THE FOLLOWING KEYS :-
        // 1. lastPlayedSong --> STRING (METADATA OF LAST PLAYED SONG IN STRING DATA-TYPE)
        // 2. audioQualityPreference --> INTEGER
        // 3. currentlyInUseApiKeyIdx --> INTEGER (STORED THE IDX OF LAST YOUTUBE-DATA-API USED)

        // AFTER FETCHING ALL THESE WE STORE THEM IN PUBLIC DATA-MEMBERS OF CLASS - MediaPLayerState
        SharedPreferences onCloseStateSharedPreferences = context.getSharedPreferences("on_close_state",Context.MODE_PRIVATE);
        MPState.metadataOfCP = MediaMetadata.parseMetaDataFromString(
                onCloseStateSharedPreferences.getString("lastPlayedSong",null));
        Config.currentlyInUseApiKeyIdx = onCloseStateSharedPreferences.getInt("currentlyInUseApiKeyIdx",0); // SETTING DEFAULT TO ZERO
        MPState.audioQualityPreference = onCloseStateSharedPreferences.getInt("audioQualityPreference",HIGH_QUALITY_AUDIO); // SETTING DEFAULT TO HIGH-QUALITY AUDIO

        if (metadataOfCP.getSongTitle() == null)    MPState.setCurrentState(MPState.NULL_STATE);
        else                                        MPState.setCurrentState(MPState.IDLE_STATE);

        Log.d("Application-Status","[ INFO  ] Reading SharedPreference - on_close_state");
    }


    // EXECUTES WHEN THE APPLICATION GETS STARTED
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Application-Status","[ INFO  ] Base-Class onCreate Called");
        // HENCE, THE IDEA OF NOTIFICATION CHANNEL COMES FROM OREO VERSION, SO IT'S AVAILABLE IN
        // OREO AND ABOVE SDK(S)
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            // CREATING NOTIFICATION CHANNEL FOR PLAYBACK CONTROLS
            Log.d("Application-Status","[ INFO  ] Constructing Notification Channel");
            NotificationChannel playbackChannel = new NotificationChannel(PLAYBACK_CONTROLS,
                    "Playback Controls",NotificationManager.IMPORTANCE_LOW);

            playbackChannel.setShowBadge(false);
            playbackChannel.setSound(null,null);

            // CREATING NOTIFICATION MANAGER TO HANDLE NOTIFICATION CHANNEL
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(playbackChannel);
        }
    }

    public static AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d("Application-Status","AudioManager Callback called");
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    stateBeforeAudioFocusLoss = currentState;
                    setCurrentState(MPState.PAUSED_STATE);
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    stateBeforeAudioFocusLoss = currentState;
                    setCurrentState(MPState.PAUSED_STATE);
                    break;

                case AudioManager.AUDIOFOCUS_GAIN:
                    if ( stateBeforeAudioFocusLoss == PLAYING_STATE )
                        setCurrentState(MPState.PLAYING_STATE);
                    break;
            }
        }
    };

}

