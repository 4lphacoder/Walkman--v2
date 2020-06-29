package com.thealphadevelopers.walkman;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.fragment.app.Fragment;
import com.thealphadevelopers.walkman.Models.PlayerQueue;
import com.thealphadevelopers.walkman.Models.UserInfo;
import com.thealphadevelopers.walkman.Services.MPService;


public class MPState extends Application {
    // THIS BASE-CLASS ACT AS A WRAPPER CLASS FOR THE WHOLE APPLICATION
    // THIS CLASS LOADED AT THE LAUNCH OF APPLICATION
    // ADDING DEBUG TAGS
    public static final String DEBUG_TAG = "application-status";

    // NOTIFICATION CHANNEL ID, FOR CLASSIFYING VARIOUS NOTIFICATIONS SEND BY APPLICATION &
    // UPDATING THEM
    public static final String PLAYBACK_CONTROLS = "PLAYBACK_CONTROLS";
    public static final int NOTIFICATION_ID = 1;

    // THESE VALUES FETCHED FROM SHARED PREFERENCE FILES AND USED TO
    // STORE USER DETAILS AND PLAYER ON CLOSE STATE.
    public static UserInfo userInfo;

    // DEFINING ITAGS OF REQUIRED MEDIA FORMATS
    public static int audioQualityPreference;           // USERS PREFERENCE OF QUALITY OF MEDIA REQUIRED
    public static final int HIGH_QUALITY_AUDIO = 251;
    public static final int MEDIUM_QUALITY_AUDIO = 250;
    public static final int LOW_QUALITY_AUDIO = 249;

    // SOME OTHER MEDIA-PLAYER FEATURES LIKE QUEUE, ETC.
    public static int homeRootScrollX;
    public static int homeRootScrollY;
    public static int homeRecommendationScrollX;
    public static int homeRecommendationScrollY;
    public static int homeMoodsCatScrollX;
    public static int homeMoodsCatScrollY;
    public static PlayerQueue playerQueue = new PlayerQueue();
    public static MPService mediaPlayerService = new MPService();


    // CREATING A MEDIA PLAYER OBJECT
    public static Fragment homeFragment;
    public static Fragment exploreMusicFragment;

    public static void save(Context context) {
        // STORES THE FOLLOWING IN SHARED PREFERENCES :-
        // 1. this.userInfo
        // 2. this.playerQueue
        // 3. Config.currentlyInUseApiKeyIdx
        // 4. this.audioQualityPreference

        SharedPreferences userInfoSharedPref = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfoSharedPref.edit();
        userInfoEditor.putString("userInfo", userInfo.toString());
        userInfoEditor.commit();

        SharedPreferences onCloseStateSharedPref = context.getSharedPreferences("on_close_state",Context.MODE_PRIVATE);
        SharedPreferences.Editor onCloseStateEditor = onCloseStateSharedPref.edit();
        onCloseStateEditor.putString("playerQueue", playerQueue.toString());
        onCloseStateEditor.putInt("currentlyInUseApiKeyIdx",Config.currentlyInUseApiKeyIdx);
        onCloseStateEditor.putInt("audioQualityPreference",audioQualityPreference);
        onCloseStateEditor.commit();
    }

    public static void load(Context context) {
        // LOADS THE FOLLOWING FROM SHARED PREFERENCES :-
        // 1. this.userInfo
        // 2. this.playerQueue
        // 3. Config.currentlyInUseApiKeyIdx
        // 4. this.audioQualityPreference


        // FETCHING USER-DETAILS FROM SHARED PREFERENCE (FILE KEY - user_info) LIKE:-
        // 1. userInfo --> STRING (USER-INFO PARSED IN STRING DATA-TYPE)

        // AFTER FETCHING ALL THESE WE STORE THEM IN PUBLIC DATA-MEMBERS OF CLASS - MediaPLayerState
        SharedPreferences userInfoSharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        userInfo = UserInfo.parseUserInfoFromString(
                userInfoSharedPreferences.getString("userInfo",null));

        // FETCHING ON-CLOSE-STATE OF MEDIA PLAYER FROM SHARED PREFERENCE (FILE KEY - on_close_state)
        // THIS SHARED PREFERENCE CONTAINS THE FOLLOWING KEYS :-
        // 1. lastPlayedSong --> STRING (METADATA OF LAST PLAYED SONG IN STRING DATA-TYPE)
        // 2. audioQualityPreference --> INTEGER
        // 3. currentlyInUseApiKeyIdx --> INTEGER (STORED THE IDX OF LAST YOUTUBE-DATA-API USED)

        // AFTER FETCHING ALL THESE WE STORE THEM IN PUBLIC DATA-MEMBERS OF CLASS - MediaPLayerState
        SharedPreferences onCloseStateSharedPreferences = context.getSharedPreferences("on_close_state",Context.MODE_PRIVATE);
        MPState.playerQueue = PlayerQueue.parseFromString(
                onCloseStateSharedPreferences.getString("playerQueue",null));
        Config.currentlyInUseApiKeyIdx = onCloseStateSharedPreferences.getInt("currentlyInUseApiKeyIdx",0); // SETTING DEFAULT TO ZERO
        MPState.audioQualityPreference = onCloseStateSharedPreferences.getInt("audioQualityPreference",HIGH_QUALITY_AUDIO); // SETTING DEFAULT TO HIGH-QUALITY AUDIO
    }


    // EXECUTES WHEN THE APPLICATION GETS STARTED
    @Override
    public void onCreate() {
        super.onCreate();
        // HENCE, THE IDEA OF NOTIFICATION CHANNEL COMES FROM OREO VERSION, SO IT'S AVAILABLE IN
        // OREO AND ABOVE SDK(S)
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            // CREATING NOTIFICATION CHANNEL FOR PLAYBACK CONTROLS
            NotificationChannel playbackChannel = new NotificationChannel(PLAYBACK_CONTROLS,
                    "Playback Controls",NotificationManager.IMPORTANCE_LOW);

            playbackChannel.setShowBadge(false);
            playbackChannel.setSound(null,null);

            // CREATING NOTIFICATION MANAGER TO HANDLE NOTIFICATION CHANNEL
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(playbackChannel);
        }
    }
}

