package com.thealphadevelopers.walkman.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

abstract  public class FetchImageFromURI extends AsyncTask<Uri, Void, String> {
    @Override
    protected String doInBackground(Uri... uris) {
        String userAvatarURL = uris[0].toString();      // CONVERTING ANDROID.NET.URI OBJECT TO STRING
        Bitmap userAvatar = null;                       // THIS BITMAP OBJECT STORES THE FETCHED IMAGE
        String base64EncodedImage = null;
        try {
            // OPENING STREAM FOR IMAGE DOWNLOAD FOR THE GIVEN URL
            InputStream istream = new java.net.URL(userAvatarURL).openStream();
            // FETCHING IMAGE FROM OPENED STREAM AND STORING BACK INTO BITMAP OBJECT
            userAvatar = BitmapFactory.decodeStream(istream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // CONVERTING BITMAP INTO BYTE-ARRAY-OUTPUT-STREAM
            userAvatar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            base64EncodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        }
        catch (Exception e) {
            // ERROR OCCURS WHILE OPENING STREAM WITH THE GIVEN URL OF USER-AVATAR
        }
        return base64EncodedImage;
    }

    @Override
    abstract protected void onPostExecute(String base64EncodedImage);
}
