package com.waracle.androidtest;

/**
 * Created by tiagoornelas on 13/02/2018.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends ListFragment {

    private static final String ARG_CAKES = "ARG_CAKES"; //for saved instance state purposes
    private static String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private static final String TAG = PlaceholderFragment.class.getSimpleName();

    private ListView mListView;
    private MyAdapter mAdapter;
    private ProgressBar loader;
    private ArrayList<Cake> cakes;

    public PlaceholderFragment() { /**/ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = rootView.findViewById(android.R.id.list);
        loader = rootView.findViewById(R.id.progressBar);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // Create and set the list adapter.
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

        //handle screen rotation
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_CAKES)){

            ArrayList<Cake> cakes = savedInstanceState.getParcelableArrayList(ARG_CAKES);

            mAdapter.setItems(cakes);
            mAdapter.notifyDataSetChanged();
            this.cakes = cakes;
        }else {

            // Load data from net.
            new CakesAsyncTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (this.cakes != null){

            outState.putParcelableArrayList(ARG_CAKES, this.cakes);
        }

        super.onSaveInstanceState(outState);
    }

    private JSONArray loadData() throws IOException, JSONException {

        URL url = new URL(JSON_URL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Can you think of a way to improve the performance of loading data
            // using HTTP headers???

            // Also, Do you trust any utils thrown your way????

            byte[] bytes = StreamUtils.readUnknownFully(in);

            // Read in charset of HTTP content.
            String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

            // Convert byte array to appropriate encoded string.
            String jsonText = new String(bytes, charset);

            // Read string as JSON.
            return new JSONArray(jsonText);
        } finally {

            urlConnection.disconnect();
        }
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(String contentType) {

        if (contentType != null) {
            String[] params = contentType.split(",");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return "UTF-8";
    }

    public void refresh() {

        new CakesAsyncTask().execute();
    }

    private class CakesAsyncTask extends AsyncTask<Void,Void,JSONArray>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            try {

                return loadData();
            } catch (IOException | JSONException e) {

                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            super.onPostExecute(jsonArray);
            loader.setVisibility(View.GONE);

            if (jsonArray == null)
                return;

            Log.i(TAG, "onPostExecute: " + jsonArray.toString());
            ArrayList<Cake> networkCakes = convertJsonArrayToCakes(jsonArray);
            PlaceholderFragment.this.cakes =  networkCakes;
            mAdapter.setItems(networkCakes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Cake> convertJsonArrayToCakes(JSONArray items) {

        ArrayList<Cake> cakes = new ArrayList<>();

        for (int i = 0; i < items.length(); i ++){
            try {
                JSONObject cakeJsonObject = items.getJSONObject(i);
                cakes.add(new Cake(cakeJsonObject.getString("title"),
                        cakeJsonObject.getString("desc"),
                        cakeJsonObject.getString("image")));
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

        return cakes;
    }
}