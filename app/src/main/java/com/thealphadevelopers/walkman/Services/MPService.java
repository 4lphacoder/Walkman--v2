package com.thealphadevelopers.walkman.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.Models.MediaMetadata;
import com.thealphadevelopers.walkman.R;


public class MPService extends Service {

    public static final int IDLE_STATE = 0;
    public static final int LOADING_STATE = 5;
    public static final int PLAYING_STATE = 1;
    public static final int PAUSED_STATE= 2;
    public static final int FINISHED_STATE = 3;
    public static final int NULL_STATE = 6;

    // INITIALISING MEDIA PLAYER OBJECT
    private int currentState = IDLE_STATE;
    private MediaMetadata currentPlayingMedia;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MPServiceStateChangeListener stateChangeListener;


    @Override
    public void onCreate() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void load(final Context ctx) {
        if( this.currentPlayingMedia == null ) {
            // GENERATE ERROR TOAST OF PLAYER-QUEUE EMPTY
            Toast toast = Toast.makeText(ctx,
                        "Player queue gets exhausted, add other media files to the playlist",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
            setCurrentState(MPService.FINISHED_STATE);
        }
        else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource( this.currentPlayingMedia.getURI() );
                setCurrentState(MPService.LOADING_STATE);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        play();
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // THIS CALLBACK EXECUTES WHEN A PLAYING MEDIA GET FINISHES
                        // THIS FUNCTION WILL CHECK THE SIZE OF PLAYING QUEUE,
                        // IF NOTHING LEFTS :
                        //      -IT STOPS PLAYING AND COMES IN IDLE STATE
                        // ELSE :
                        //      -PLAY NEXT SONG FROM PLAYER-QUEUE
                        changeNext(ctx);
                    }
                });
            }
            catch ( Exception e ) {
                // EXCEPTION HANDLING
                Log.d(MPState.DEBUG_TAG, e.toString());
                setCurrentState(MPService.NULL_STATE);
            }
        }
    }

    public void pause() {
        this.mediaPlayer.pause();
        setCurrentState(MPService.PAUSED_STATE);
    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        setCurrentState(MPService.FINISHED_STATE);
        onDestroy();
        MPState.mediaPlayerService = null;
    }

    public void play() {
        this.mediaPlayer.start();
        setCurrentState(MPService.PLAYING_STATE);
    }

    public int getCurrentState() {
        return currentState;
    }

    public void addMPServiceStateChangeListener(MPServiceStateChangeListener listener) {
        this.stateChangeListener = listener;
    }

    public void setCurrentState(int newState) {
        this.currentState = newState;
        if( this.stateChangeListener != null )
            stateChangeListener.onStateChanges(newState);
    }

    public void changeNext(Context ctx) {
        this.currentPlayingMedia = MPState.playerQueue.getNextPlayingMedia();
        load(ctx);
    }

    public void changePrevious(Context ctx) {
        this.currentPlayingMedia = MPState.playerQueue.getPreviouslyPlayedMedia();
        load(ctx);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // CREATING DUMMY NOTIFICATION
        Notification notification;
        if( this.currentPlayingMedia != null ) {
            Bitmap thumbnail = BitmapFactory.decodeFile( this.currentPlayingMedia.getURI() );
            notification = new NotificationCompat.Builder(getApplicationContext(), MPState.PLAYBACK_CONTROLS)
                    .setSmallIcon(R.drawable.ic_walkman_white)
                    .setContentTitle( this.currentPlayingMedia.getChannelTitle() )
                    .setContentText( this.currentPlayingMedia.getSongTitle() )
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setLargeIcon(thumbnail)
                    .addAction(R.drawable.ic_favorite_border_white, "Favourite", null)
                    .addAction(R.drawable.ic_navigate_before_white_42dp, "Previous", null)
                    .addAction(R.drawable.ic_play_arrow_white_42dp, "Play/Pause", null)
                    .addAction(R.drawable.ic_navigate_next_white_42dp, "Next", null)
                    .setColorized(true)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1, 2, 3))
                    .build();
        }
        else {
            notification = new NotificationCompat.Builder(getApplicationContext(), MPState.PLAYBACK_CONTROLS)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setSmallIcon(R.drawable.ic_walkman_white)
                    .setColorized(true)
                    .build();
        }
        startForeground(MPState.NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

//MPState.audioManager.abandonAudioFocus(MPState.audioFocusChangeListener);

//    private static MPStateChangeListener mpStateChangeListener;
//    public static void addMPStateChangeListener(MPStateChangeListener listener ) { mpStateChangeListener = listener; }

//    public static void setCurrentState(int value) {
//        currentState = value;
//        if( mpStateChangeListener != null )     mpStateChangeListener.onStateChanges(value);
//        if( value == PAUSED_STATE ) {
//            mediaPlayer.pause();
//        }
//        else if( value == PLAYING_STATE && mediaPlayer != null ) {
//            // REQUESTING AUDIO FOCUS
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build();
//
//            int focusRequestResponse;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
//                        .setAudioAttributes(audioAttributes)
//                        .setAcceptsDelayedFocusGain(true)
//                        .setOnAudioFocusChangeListener(audioFocusChangeListener)
//                        .build();
//
//                focusRequestResponse = MPState.audioManager.requestAudioFocus(audioFocusRequest);;
//            }
//            else {
//                focusRequestResponse = MPState.audioManager.requestAudioFocus(
//                        audioFocusChangeListener,
//                        AudioManager.STREAM_MUSIC,
//                        AudioManager.AUDIOFOCUS_GAIN);
//            }
//            switch (focusRequestResponse) {
//                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
//                    mediaPlayer.start();
//                    break;
//                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
//                    Log.d("Application-Status","[ ERROR ] Audio focus request failed");
//            }
//        }
//    }


////    public static AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
////        @Override
////        public void onAudioFocusChange(int focusChange) {
////            Log.d("Application-Status","AudioManager Callback called");
////            switch (focusChange) {
////                case AudioManager.AUDIOFOCUS_LOSS:
////                    stateBeforeAudioFocusLoss = currentState;
////                    setCurrentState(MPState.PAUSED_STATE);
////                    break;
////
////                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
////                    stateBeforeAudioFocusLoss = currentState;
////                    setCurrentState(MPState.PAUSED_STATE);
////                    break;
////
////                case AudioManager.AUDIOFOCUS_GAIN:
////                    if ( stateBeforeAudioFocusLoss == PLAYING_STATE )
////                        setCurrentState(MPState.PLAYING_STATE);
////                    break;
////            }
//        }
//    };
