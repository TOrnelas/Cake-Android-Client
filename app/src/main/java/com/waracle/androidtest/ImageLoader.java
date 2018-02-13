package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    public ImageLoader() { /**/ }

    /**
     * Simple function for loading a bitmap image from the web
     *
     * @param url       image url
     * @param imageView view to set image too.
     */
    public void load(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??

        new LoadImageAsyncTask(imageView).execute(url);
//        try {
//            setImageView(imageView, convertToBitmap(loadImageData(url)));
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//        }
    }

//    private static byte[] loadImageData(String url) throws IOException {
//
//
//    }

    private static class LoadImageAsyncTask extends AsyncTask<String,Void,byte[]>{

        private final ImageView imageView;

        public LoadImageAsyncTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected byte[] doInBackground(String... strings) {

            HttpURLConnection connection = null;
            InputStream inputStream = null;

            try {

                connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                // Read data from workstation
                inputStream = connection.getInputStream();

                return StreamUtils.readUnknownFully(inputStream);
            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }finally {

                StreamUtils.close(inputStream);
                if(connection != null) connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(byte[] bytes) {

            if (bytes == null)
                return;

            super.onPostExecute(bytes);
            setImageView(imageView, convertToBitmap(bytes));
        }
    }

    private static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void setImageView(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
