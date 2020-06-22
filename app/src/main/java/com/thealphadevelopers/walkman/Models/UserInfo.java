package com.thealphadevelopers.walkman.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

// THIS CLASS PROVIDES ABSTRACTION TO HANDLE USER-INFORMATION LIKE,
// 1. isFirstRun --> BOOLEAN (STORES WHETHER USER IS RUNNING THIS APP FOR FIRST TIME OR NOT)
// 2. userName --> STRING (STORES WHAT'S THE NAME OF USER WHO ARE GOING TO USE IT)
// 3. languages --> ARRAY-LIST (STORES THE LANGUAGES OF WHICH SONGS, USER LIKES TO LISTEN)
// 4. favouriteArtist --> ARRAY-LIST (STORES FAVOURITE ARTIST OF USER WHOM WHICH SONGS WE
//                                      ARE GOING TO DISPLAY IN HOME SECTION/ MAIN_ACTIVITY)
// 5. emailAddress --> STRING ( TO UNIQUELY IDENTIFY USER ACOOUNT ON SERVER SIDE TO SAVE USER'S
//                            HISTORY OF SONG'S LISTENED, PLAYLISTS USER CREATED, ETC. )
public class UserInfo {
    private boolean isFirstRun;     // USED TO CHECK FIRST-RUN OF APPLICATION AND REDIRECTS TO SIGN-IN AND INFORMATION COLLECTING ACTIVITIES
    private String userName;            // APPLICATION USER'S COMPLETE NAME
    private ArrayList<String> languages;    // LANGUAGES IN WHICH USER LIKES TO LISTEN SONGS
    private ArrayList<String> favouriteArtists;     // ARTISTS WHOM USER LIKES TO FOLLOW
    private String emailAddress;        // JUST TO UNIQUELY IDENTIFY USER IN BACKEND DATABASE

    // PRIVATE DATA MEMBER SETTER METHODS
    public void setFirstRun(boolean firstRun)           { isFirstRun = firstRun; }
    public void setUserName(String userName)            { this.userName = userName; }
    public void setEmailAddress(String emailAddress)    { this.emailAddress = emailAddress; }

    public void addLanguage(String language) {
        if ( this.languages == null )       // CREATING LANGUAGES ARRAY-LIST
            this.languages = new ArrayList<String>();
        this.languages.add(language);
    }
    public void setFavouriteArtists(String artist) {
        if( this.favouriteArtists == null )     // CREATING FAVOURITE ARTISTS ARRAY-LIST
            this.favouriteArtists = new ArrayList<String>();
        this.favouriteArtists.add(artist);
    }

    // PRIVATE DATA MEMBER GETTER METHODS
    public String getUserFirstName() {
        String[] userName = this.userName.split(" ");   // SPLITTING USER-NAME ON THE BASIS OF ' ' IN BETWEEN STRING CHARS
        return userName[0];     // RETURNING FIRST NAME OF USER
    }
    public String getUserCompleteName()             { return userName; }
    public boolean isFirstRun()                     { return isFirstRun; }
    public String getEmailAddress()                 { return emailAddress; }
    public ArrayList<String> getFavouriteArtists()  { return favouriteArtists; }
    public ArrayList<String> getLanguages()         { return languages; }


    // PARSING USER-INFO FROM STRING COLLECTED FROM SHARED-PREFERENCES
    public static UserInfo parseUserInfoFromString(String str) {
        // EXECUTES WHEN SHARED-PREFERENCE DOESN'T CONTAINS USER-INFO
        if( str == null ) {
            UserInfo userInfo = new UserInfo();
            userInfo.isFirstRun = true;
            userInfo.userName = null;
            userInfo.languages = null;
            userInfo.favouriteArtists = null;
            userInfo.emailAddress = null;
            return userInfo;
        }
        else {
            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(str,UserInfo.class);
            return userInfo;
        }
    }

    // PARSING USER-INFO INTO STRING TO STORE USER-INFO OBJECT IN SHARED-PREFERENCES
    public String parseUserInfoToString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}

