package com.hfad.vkinfo.utils;

import android.net.Uri;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author yuliiamelnyk on 07/09/2020
 * @project VKInfo
 */
public class NetworkUtils {
    public static TextView name, surname;
    private static ParseXML mParseXML;


    private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/";
    private static final String FLICKR_USERS_GET = "?method=flickr.profile.getProfile";
    private static final String FLICKR_FOTOS_GET = "?method=flickr.interestingness.getList";
    private static final String API_KEY = "6a0d1973ce0751f2cb32813281ac3b21";
    private static final String FORMAT = "json";
    private static final String JSON = "nojsoncallback";
    //private static final String USER_ID = "189765691@N02";
    private static final String USER_ID = "35034351496@N01";

    //public static URL generateURL(String userId) {
    public static URL generateURL() {
        Uri builtUri = Uri.parse(FLICKR_BASE_URL + FLICKR_FOTOS_GET)
                .buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                //.appendQueryParameter("user_id", userId)
                .appendQueryParameter("format", FORMAT)
                .appendQueryParameter(JSON, "1")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponceFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        StringBuilder response = new StringBuilder();
        String line = "";
        try {
            InputStream inputStream = urlConnection.getInputStream();
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(urlConnection.getResponseMessage() + ": with " + url.toString());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
            //return mParseXML.parse(inputStream);
            return response.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
        /*
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }*/

            }
        }
        return response.toString();
    }
}
