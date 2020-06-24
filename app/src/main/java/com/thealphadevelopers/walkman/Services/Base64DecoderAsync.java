package com.thealphadevelopers.walkman.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

abstract public class Base64DecoderAsync extends AsyncTask<String , Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings) {
        String encodedString = strings[0];
        byte binaryBytes[] = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap userAvatar = BitmapFactory.decodeByteArray(binaryBytes, 0, binaryBytes.length);
        return userAvatar;
    }

    // THIS METHOD WILL BE IMPLEMENTED AS A CALLBACK FUNCTION AT THE LOCATION OF ITS USAGE
    @Override
    abstract protected void onPostExecute(Bitmap bitmap);
}
