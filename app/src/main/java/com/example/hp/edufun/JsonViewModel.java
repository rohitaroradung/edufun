package com.example.hp.edufun;

import android.app.Application;

import com.google.android.gms.maps.model.PolylineOptions;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class JsonViewModel extends AndroidViewModel {
    private final JsonLiveData data;
    static String url;

    public JsonViewModel(Application application) {
        super(application);
        data = new JsonLiveData(application,url);
    }
    public LiveData<PolylineOptions> getData() {
        return data;
    }
   static  void  doAction(String ur)
    {
        url=ur;
    }
}
