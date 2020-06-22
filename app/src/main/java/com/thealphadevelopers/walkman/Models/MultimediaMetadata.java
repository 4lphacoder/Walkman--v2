package com.thealphadevelopers.walkman.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MultimediaMetadata {
    private boolean isPlayingFromNetwork;
    private String songTitle;
    private String channelTitle;
    private String videoID;
    private String URI;
    private boolean isFavouriteStateActive;

    public String parseMetadataToString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return  gson.toJson(this);
    }

    public static MultimediaMetadata parseMetaDataFromString(String str) {
        if( str == null ) {
            MultimediaMetadata metadata = new MultimediaMetadata();
            metadata.isPlayingFromNetwork = false;
            metadata.songTitle = null;
            metadata.channelTitle = null;
            metadata.videoID = null;
            metadata.URI = null;
            metadata.isFavouriteStateActive = false;
            return metadata;
        }
        Gson gson = new Gson();
        MultimediaMetadata metadata = gson.fromJson(str,MultimediaMetadata.class);
        return metadata;
    }

    // GETTER METHODS OF THIS CLASS
    public boolean isPlayingFromNetwork()   { return isPlayingFromNetwork; }
    public String getSongTitle()            { return songTitle; }
    public String getChannelTitle()         { return channelTitle; }
    public String getVideoID()              { return videoID; }
    public String getURI()                  { return URI; }
    public boolean isFavouriteStateActive() { return isFavouriteStateActive; }

    // SETTERS OF THIS CLASS
    public void setPlayingFromNetwork(boolean playingFromNetwork)       { isPlayingFromNetwork = playingFromNetwork; }
    public void setSongTitle(String songTitle)                          { this.songTitle = songTitle; }
    public void setChannelTitle(String channelTitle)                    { this.channelTitle = channelTitle; }
    public void setVideoID(String videoID)                              { this.videoID = videoID; }
    public void setURI(String URI)                                      { this.URI = URI; }
    public void setFavouriteStateActive(boolean favouriteStateActive)   { isFavouriteStateActive = favouriteStateActive; }
}


