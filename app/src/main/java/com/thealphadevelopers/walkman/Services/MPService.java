package com.thealphadevelopers.walkman.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.Services.Sanitizer;
import com.thealphadevelopers.walkman.R;

import java.io.IOException;

public class MPService extends Service {
//    @Override
//    public void onCreate() {
//        Log.v("Application-Status","MPService is created");
//        // INITIALISING MEDIA-PLAYER OBJECT
//        MPState.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        MPState.mediaPlayer = new MediaPlayer();
//        MPState.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        MPState.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                MPState.setCurrentState(MPState.IDLE_STATE);
//            }
//        });
//    }

    @Override
    public void onCreate() {
        Log.d(MPState.DEBUG_TAG, "MPService on create method called");
        // INITIALISING MEDIA PLAYER OBJECT
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // THIS CALLBACK EXECUTES WHEN A PLAYING MEDIA GET FINISHES
                // THIS FUNCTION WILL CHECK THE SIZE OF PLAYING QUEUE,
                // IF NOTHING LEFTS :
                //      -IT STOPS PLAYING AND COMES IN IDLE STATE
                // ELSE :
                //      -PLAY NEXT SONG FROM PLAYER-QUEUE
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // CREATING DUMMY NOTIFICATION
//        Bitmap artwork = BitmapFactory.decodeResource(getResources(), SearchItemLayout.fetchThumbnailId());
        Notification notification = new NotificationCompat.Builder(this, MPState.PLAYBACK_CONTROLS)
                .setSmallIcon(R.drawable.ic_walkman_white)
                .setContentTitle(Sanitizer.getSanitizedString(MPState.metadataOfCP.getChannelTitle()))
                .setContentText(Sanitizer.toTitleCase(Sanitizer.getSanitizedString(MPState.metadataOfCP.getSongTitle())))
//                .setLargeIcon(artwork)
                .addAction(R.drawable.ic_favorite_border_white,"Favourite",null)
                .addAction(R.drawable.ic_navigate_before_white_42dp,"Previous",null)
                .addAction(R.drawable.ic_play_arrow_white_42dp,"Play/Pause",null)
                .addAction(R.drawable.ic_navigate_next_white_42dp,"Next",null)
                .setColorized(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1,2,3))
                .build();

        startForeground(1,notification);

        try {
            Log.v("Status","Start Preparing Music File");
            MPState.mediaPlayer.reset();
            MPState.mediaPlayer.setDataSource(MPState.metadataOfCP.getURI());
            MPState.mediaPlayer.prepareAsync();
            MPState.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.v("Status","Prepared Music File");
                    MPState.setCurrentState(MPState.PLAYING_STATE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v("Application-Status","MPService is being destroyed");
        stopForeground(true);
        MPState.mediaPlayer.stop();
        MPState.mediaPlayer.release();
        MPState.mediaPlayer = null;
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

