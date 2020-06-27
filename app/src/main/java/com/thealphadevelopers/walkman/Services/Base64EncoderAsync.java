package com.thealphadevelopers.walkman.Services;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

abstract public class Base64EncoderAsync extends AsyncTask<byte[], Void, String> {
    @Override
    protected String doInBackground(byte[]... bytes) {
        byte[] binaryData = bytes[0];       // BINARY DATA GOING TO ENCODE
        // ENCODING BINARY DATA TO BASE64 ENCODED STRING
        String base64EncodedString = Base64.encodeToString(binaryData, Base64.DEFAULT);
        return base64EncodedString;
    }

    // THIS METHOD WILL BE IMPLEMENTED AS A CALLBACK FUNCTION AT THE LOCATION OF ITS USAGE
    @Override
    abstract protected void onPostExecute(String s) ;
}
