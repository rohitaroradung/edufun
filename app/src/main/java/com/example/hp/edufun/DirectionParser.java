package com.example.hp.edufun;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class DirectionParser {
    final static String routes="routes";
    final static String legs="legs";
    final static String steps="steps";
    String url;
    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"problem",e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);//converting into String from bunch of data
            } else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }





    public static PolylineOptions getPoints(String ur)throws IOException
    {
        URL u = createUrl(ur);
        String SAMPLE_JSON_RESPONSE = makeHttpRequest(u);


        PolylineOptions polylineOptions = new PolylineOptions();
       try {
           JSONObject jsonObject = new JSONObject(SAMPLE_JSON_RESPONSE);

           JSONArray Jsonroutes = jsonObject.getJSONArray(routes);
           JSONObject object = Jsonroutes.getJSONObject(0);
          JSONArray Jsonlegs = object.getJSONArray(legs);
           JSONObject object1 = Jsonlegs.getJSONObject(0);
          JSONArray Jsonsteps = object1.getJSONArray(steps);
          Double lat,lon;
          for(int i =0;i<Jsonsteps.length();i++)
          {
            JSONObject stepi =  Jsonsteps.getJSONObject(i);
               JSONObject polyline =  stepi.getJSONObject("polyline");
               String point = polyline.getString("points");
               polylineOptions.addAll(PolyUtil.decode(point));


          }




       }
       catch (Exception e)
       {

       }
       polylineOptions.width(15);
       polylineOptions.color(Color.BLUE);
        return polylineOptions;
    }


}
