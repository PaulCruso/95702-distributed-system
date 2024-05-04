package ds.edu.hearthstoneandriodapp;

/**
 * GetInfo.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: April 3, 2024
 *
 * Class responsible for fetching Hearthstone card information,
 * including images and textual data like flavor and card text.
 * Utilizes an asynchronous task to perform network operations on a background thread
 * and updates the UI thread upon completion.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class GetInfo {
    HearthstoneInfo ip = null;   // for callback
    String searchTerm = null;    // search HearthStone card for this word
    Bitmap picture = null;      // returned from HearthStoneApi
    String cardFlavor = null;
    String cardText = null;

    /**
     * Initiates a search for Hearthstone card information based on the provided searchTerm. This method sets up
     * the background task for fetching card data.
     * idea is learnt from AndroidInterestingPicture Lab
     *
     * @param searchTerm The search term or card name to query the API for.
     * @param activity The activity context used for updating the UI thread upon task completion.
     * @param ip The callback interface through which results are communicated back.
     */
    public void search(String searchTerm, Activity activity, HearthstoneInfo ip) {
        this.ip = ip;
        this.searchTerm = searchTerm;
        new BackgroundTask(activity).execute();
    }
    private class BackgroundTask {

        // Reference to the activity for UI updates
        private Activity activity;

        public BackgroundTask(Activity activity) {
            this.activity = activity;
        }

        /**
         * Starts the background thread for network operations, then switches back to the UI thread to update the UI with results.
         * idea is learnt from AndroidInterestingPicture Lab
         */
        private void startBackground() {
            new Thread(new Runnable() {
                public void run() {

                    doInBackground();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            onPostExecute();
                        }
                    });
                }
            }).start();
        }
        /**
         * Placeholder for initial setup before executing the background task. Directly calls startBackground.
         */
        private void execute(){
            startBackground();
        }
        /**
         * Performs the network operation on a background thread. Fetches card data based on the searchTerm.
         */
        private void doInBackground() {
            picture = search(searchTerm);
        }
        /**
         * Called after doInBackground completes. Notifies the callback interface with the fetched data.
         */
        public void onPostExecute() {
            ip.pictureReady(picture, cardFlavor, cardText);
        }

        /**
         * Searches for a Hearthstone card by the searchTerm.
         * Parses the JSON response to extract image URL, flavor text, and card text.
         *
         * @param searchTerm The card name to search for.
         * @return A Bitmap of the card image if found, null otherwise.
         */
        private Bitmap search(String searchTerm) {
            String pictureUrl = null;
            String[] data = searchTerm.split(" ");
            StringBuilder sb = new StringBuilder("https://curly-space-doodle-jvwr59wq95g2q5pw-8080.app.github.dev/getCard?cardName=");
            sb.append(data[0]);
            for (int i = 1; i < data.length; i++) {
                sb.append("+").append(data[i]);
            }
            String urlString = sb.toString();
            String fetch = fetch(urlString);

            try {
                JSONObject obj = new JSONObject(fetch);
                pictureUrl = obj.getString("cardImageUrl");
                cardFlavor = obj.getString("cardFlavor");
                cardText = obj.getString("cardText");
            } catch (JSONException e) {
                return null;
            }

            try {
                URL u = new URL(pictureUrl);
                return getRemoteImage(u);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        /*
         * Given a URL referring to an image, return a bitmap of that image
         * This method is copied from lab AndroidInterestingPicture
         */
        @RequiresApi(api = Build.VERSION_CODES.P)
        private Bitmap getRemoteImage(final URL url) {
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Copy from Lab2 InterestingPicture.
         * Make an HTTP request to a given URL.
         *
         * @param urlString The URL of the request
         * @return A string of the response from the HTTP GET.  This is identical
         * to what would be returned from using curl on the command line.
         */
        private String fetch(String urlString) {
            String response = "";
            try {
                URL url = new URL(urlString);
                /*
                 * Create an HttpURLConnection.  This is useful for setting headers
                 * and for getting the path of the resource that is returned (which
                 * may be different than the URL above if redirected).
                 * HttpsURLConnection (with an "s") can be used if required by the site.
                 */
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String str;
                // Read each line of "in" until done, adding each to "response"
                while ((str = in.readLine()) != null) {
                    // str is one line of text readLine() strips newline characters
                    response += str;
                }
                in.close();
            } catch (IOException e) {
                System.out.println("Eeek, an exception");
                // Do something reasonable.  This is left for students to do.
            }
            return response;
        }
    }
}

