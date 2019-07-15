package com.example.hp.edufun;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.PolylineOptions;

import androidx.lifecycle.LiveData;

public class JsonLiveData extends LiveData<PolylineOptions> {
    private final Context context;
    private String url;

    public JsonLiveData(Context context,String url) {
        this.context = context;
  this.url = url;
        loadData();
    }
    private void loadData() {
        new AsyncTask<String, Void, PolylineOptions>() {
            @Override
            protected PolylineOptions doInBackground(String... strings) {
                String url = strings[0];
                PolylineOptions polylineOptions = null;
                try {
                  polylineOptions =  DirectionParser.getPoints(url);
                }
                catch (Exception e)
                {

                }

                return polylineOptions;
            }

            @Override
            protected void onPostExecute(PolylineOptions polylineOptions) {
             setValue(polylineOptions);
            }
        }.execute(this.url);

    }
}
