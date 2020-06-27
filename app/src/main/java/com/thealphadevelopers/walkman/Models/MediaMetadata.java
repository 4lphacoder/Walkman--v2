package com.thealphadevelopers.walkman.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MediaMetadata {
    private int hash;
    private String thumbnailURI;
    private boolean isPlayingFromNetwork;
    private String songTitle;
    private String channelTitle;
    private String videoID;
    private int itag;
    private String URI;
    private boolean isFavouriteStateActive;
    private int playCounter;
    private String lastListened;
    private String language;
    private String genre;
    private String[] moodCategories;
    private String[] artists;

    public MediaMetadata() {
        this.hash = 0;
        this.thumbnailURI = null;
        this.isPlayingFromNetwork = false;
        this.songTitle = null;
        this.channelTitle = null;
        this.videoID = null;
        this.itag = 0;
        this.URI = null;
        this.isFavouriteStateActive = false;
        this.playCounter = 0;
        this.lastListened = null;
        this.language = null;
        this.genre = null;
        this.moodCategories = null;
        this.artists = null;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return  gson.toJson(this);
    }

    public static MediaMetadata parseMetaDataFromString(String str) {
        if( str == null ) {
            MediaMetadata metadata = new MediaMetadata();
            metadata.hash = 0;
            metadata.thumbnailURI = null;
            metadata.isPlayingFromNetwork = false;
            metadata.songTitle = null;
            metadata.channelTitle = null;
            metadata.videoID = null;
            metadata.itag = 0;
            metadata.URI = null;
            metadata.isFavouriteStateActive = false;
            metadata.playCounter = 0;
            metadata.lastListened = null;
            metadata.language = null;
            metadata.genre = null;
            metadata.moodCategories = null;
            metadata.artists = null;
            return metadata;
        }

        Gson gson = new Gson();
        MediaMetadata metadata = gson.fromJson(str, MediaMetadata.class);
        return metadata;
    }

    // GETTER METHODS OF THIS CLASS
    public int getHash()                    { return hash; }
    public String getThumbnailURI()         { return thumbnailURI; }
    public boolean isPlayingFromNetwork()   { return isPlayingFromNetwork; }
    public String getSongTitle()            { return songTitle; }
    public String getChannelTitle()         { return channelTitle; }
    public String getVideoID()              { return videoID; }
    public int getItag()                    { return itag; }
    public String getURI()                  { return URI; }
    public boolean isFavouriteStateActive() { return isFavouriteStateActive; }
    public int getPlayCounter()             { return playCounter; }
    public String getLanguage()             { return language; }
    public String getGenre()                { return genre; }
    public String[] getMoodCategories()     { return moodCategories; }
    public String[] getArtists()            { return artists; }

    // SETTERS OF THIS CLASS

    public void setThumbnailURI(String thumbnailURI)                    { this.thumbnailURI = thumbnailURI; }
    public void setPlayingFromNetwork(boolean playingFromNetwork)       { isPlayingFromNetwork = playingFromNetwork; }
    public void setSongTitle(String songTitle)                          { this.songTitle = songTitle; }
    public void setChannelTitle(String channelTitle)                    { this.channelTitle = channelTitle; }
    public void setURI(String URI)                                      { this.URI = URI; }
    public void setPlayCounter(int counter)                             { this.playCounter = counter; }
    public void setLastListened(String datatime)                        { this.lastListened = datatime; }
    public void setLanguage(String language)                            { this.language = language; }
    public void setGenre(String genre)                                  { this.genre = genre; }
    public void setMoodCategories(String[] moodCategories)              { this.moodCategories = moodCategories; }
    public void setArtists(String[] artists)                            { this.artists = artists; }
    public void toggleFavouriteState()                                  { isFavouriteStateActive = ! isFavouriteStateActive; }
    public void setItag(int itag) {
        this.itag = itag;
        if( videoID != null )       // IF VIDEO-ID IS PREVIOUSLY SET, THEN WE GENERATE THIS MEDIA HASH
            this.hash = System.identityHashCode(this.videoID + Integer.toString(this.itag));
    }
    public void setVideoID(String videoID) {
        this.videoID = videoID;
        if( this.itag != 0 )
            this.hash = System.identityHashCode(this.videoID + Integer.toString(this.itag));
    }
}


