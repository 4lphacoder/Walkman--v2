package com.thealphadevelopers.walkman.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;


public class PlayerQueue {

    // THIS DATA STRUCTURE IS USED TO PROVIDE FUNCTIONALITY OF PLAYER QUEUE
    // FOLLOWING FUNCTIONALITIES ARE INTRODUCED HERE :-
    // ------------------------------------------------------------------------------------------------
    //  1. toggleShuffle() -- set isShuffleActive data member true to false and false to true
    //  2. toggleRepeat() -- set isRepeatActive data member true to false and false to true
    //  3. toggleLoop() -- set isLoopActive data member true to false and false to true
    //  4. remove(idx) -- removes a MediaMetadata object at index idx in queue data member,
    //                      if this object also presents in toPlay HashMap, this removes that as well
    //                      and return updated queue

    // 5. add(media) -- add MediaMetadata object media to the last of queue ArrayList
    // 6. addNext(media) -- add MediaMetadata object media to the position of iterator in queue
    //                      and to play HashMap.

    // 7. isShuffleActive() -- returns whether shuffle is active or not
    // 8. isLoopActive() -- return whether looping over a currentPlayingMedia is active or not
    // 9. isRepeatActive() -- return whether repeating whole queue again is active or not
    // 10. getNextPlayingMedia() -- returns media to be played next, according to the value of
    //                               isLoopActive, isShuffleActive & isRepeatActive

    // 11. getPreviouslyPlayedMedia() -- returns last media played
    // 12. getMediaAt(idx) -- returns a media object stored at index idx in queue.

    private ArrayList<MediaMetadata> queue;
    private int iterator;
    private boolean isShuffleActive;
    private boolean isRepeatActive;
    private boolean isLoopActive;
    private Stack<MediaMetadata> lastPlayedMedia;
    private MediaMetadata currentPlayingMedia;
    HashMap<Integer, MediaMetadata> played;
    HashMap<Integer, MediaMetadata> toPlay;

    public PlayerQueue() {
        queue = new ArrayList<>();
        iterator = 0;
        isShuffleActive = false;
        isRepeatActive = false;
        isLoopActive = false;
        lastPlayedMedia = new Stack<>();
        currentPlayingMedia = null;
        played = new HashMap<>();
        toPlay = new HashMap<>();
    }

    // DEFINING GETTER METHODS
    public boolean isShuffleActive()            { return isShuffleActive; }
    public boolean isRepeatActive()             { return isRepeatActive; }
    public boolean isLoopActive()               { return isLoopActive; }
    public ArrayList<MediaMetadata> getQueue()  { return queue; }

    public MediaMetadata getNextPlayingMedia() {
        if( isLoopActive )
            return currentPlayingMedia;

        if( isShuffleActive && !isRepeatActive ) {
            int mediaFilesLeftToPlay = toPlay.size();
            Random random = new Random();
            int nextMediaIdx = random.nextInt(mediaFilesLeftToPlay);

            int idx = 0;
            for( Integer hashValue : toPlay.keySet() ) {
                if (idx++ == nextMediaIdx) {
                    MediaMetadata media = toPlay.remove(hashValue);
                    played.put(media.getHash(), media);
                    lastPlayedMedia.push(currentPlayingMedia);
                    currentPlayingMedia = media;
                    return currentPlayingMedia;
                }
            }
            // RETURNS NULL ONLY WHEN toPlay HASH-MAP GETS EXHAUSTED AND REPEAT IS DISABLED
            return null;
        }

        if( isShuffleActive && isRepeatActive ) {
            Random random = new Random();
            int nextPlayingMediaIdx = random.nextInt(queue.size());
            lastPlayedMedia.push(currentPlayingMedia);
            currentPlayingMedia = queue.get(nextPlayingMediaIdx);
            return currentPlayingMedia;
        }

        if( !isRepeatActive && iterator < queue.size() ) {
            lastPlayedMedia.push(currentPlayingMedia);
            currentPlayingMedia = queue.get(iterator++);
            return currentPlayingMedia;
        }

        if( isRepeatActive ) {
            iterator = (iterator + 1) % queue.size();
            lastPlayedMedia.push(currentPlayingMedia);
            currentPlayingMedia = queue.get(iterator);
            return currentPlayingMedia;
        }

        // RETURNS NULL ONLY WHEN QUEUE GETS EXHAUSTED AND REPEAT IS DISABLED
        return null;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static PlayerQueue parseFromString(String str) {
        if( str == null )
            return new PlayerQueue();

        Gson gson = new Gson();
        PlayerQueue playerQueue = gson.fromJson(str, PlayerQueue.class);
        return playerQueue;
    }

    public MediaMetadata getPreviouslyPlayedMedia() {
        MediaMetadata media = lastPlayedMedia.pop();
        return media;
    }

    public MediaMetadata getMediaAt(int idx) {
        lastPlayedMedia.push(currentPlayingMedia);
        currentPlayingMedia = queue.get(idx);
        return currentPlayingMedia;
    }

    // DEFINING SETTER METHODS
    public void toggleShuffle()             { isShuffleActive = ! isShuffleActive; }
    public void toggleRepeat()              { isRepeatActive = ! isRepeatActive; }
    public void toggleLoop()                { isLoopActive = ! isLoopActive; }

    public ArrayList<MediaMetadata> remove( int idx ) {
        MediaMetadata media = queue.get(idx);
        queue.remove(idx);
        if( toPlay.containsKey(media.getHash()) )
            toPlay.remove(media.getHash());

        return getQueue();
    }

    public void add(MediaMetadata media) {
        queue.add(media);
        toPlay.put(media.getHash(), media);
    }

    public void addNext(MediaMetadata media) {
        queue.add(iterator,media);
        toPlay.put(media.getHash(), media);
    }

    public void clearQueue() {
        queue = new ArrayList<>();
        lastPlayedMedia = new Stack<>();
        currentPlayingMedia = null;
        played = new HashMap<>();
        toPlay = new HashMap<>();
    }
}
