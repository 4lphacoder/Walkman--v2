package com.thealphadevelopers.walkman.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import com.thealphadevelopers.walkman.MPState;
import com.thealphadevelopers.walkman.Models.MediaMetadata;
import com.thealphadevelopers.walkman.Models.PlayerQueue;
import com.thealphadevelopers.walkman.R;


public class MPService extends Service {

    PlayerQueue playerQueue;
    MediaPlayer mediaPlayer;
    MediaMetadata currentPlayingMedia;


    @Override
    public void onCreate() {
        // INITIALISING MEDIA PLAYER OBJECT
        this.playerQueue = MPState.playerQueue;
        this.currentPlayingMedia = playerQueue.getNextPlayingMedia();
        this.mediaPlayer = new MediaPlayer();

        Log.d(MPState.DEBUG_TAG, "MPService on create method called");
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
                MediaMetadata media = MPState.playerQueue.getNextPlayingMedia();
                if( media == null )
                    MPState.setCurrentState(MPState.FINISHED_STATE);
                else {
                     currentPlayingMedia = media;
                     play();
                }
            }
        });
    }

    public void play() {
        // CREATING DUMMY NOTIFICATION
        // Bitmap artwork = BitmapFactory.decodeResource(getResources(), SearchItemLayout.fetchThumbnailId());
        Notification notification = new NotificationCompat.Builder(this, MPState.PLAYBACK_CONTROLS)
                .setSmallIcon(R.drawable.ic_walkman_white)
                .setContentTitle(Sanitizer.getSanitizedString(MPState.metadataOfCP.getChannelTitle()))
                .setContentText(Sanitizer.toTitleCase(Sanitizer.getSanitizedString(MPState.metadataOfCP.getSongTitle())))
                //.setLargeIcon(artwork)
                .addAction(R.drawable.ic_favorite_border_white,"Favourite",null)
                .addAction(R.drawable.ic_navigate_before_white_42dp,"Previous",null)
                .addAction(R.drawable.ic_play_arrow_white_42dp,"Play/Pause",null)
                .addAction(R.drawable.ic_navigate_next_white_42dp,"Next",null)
                .setColorized(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1,2,3))
                .build();

        startForeground(1,notification);

        if( currentPlayingMedia == null ) {
            // GENERATE ERROR TOAST OF PLAYER-QUEUE EMPTY
            Toast toast = Toast.makeText(getApplicationContext(),
                        "Player queue gets exhausted, add other media files to the playlist",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(currentPlayingMedia.getURI());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        start();
                    }
                });
            }
            catch ( Exception e ) {
                // EXCEPTION HANDLING
            }
        }
    }

    public void pause() {
        this.mediaPlayer.pause();
    }

    public void start() {
        this.mediaPlayer.start();
    }

    public void changeNext() {
        this.currentPlayingMedia = this.playerQueue.getNextPlayingMedia();
        play();
    }

    public void changePrevious() {
        this.currentPlayingMedia = this.playerQueue.getPreviouslyPlayedMedia();
        play();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MPState.DEBUG_TAG,"MPService onStart is executing");
        play();
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

