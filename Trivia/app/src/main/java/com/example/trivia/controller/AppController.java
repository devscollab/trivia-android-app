package com.example.trivia.controller;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    public  static  final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mrequestQueue;

    public static synchronized AppController getInstance() {
//        if (mInstance == null) {
//            mInstance = new AppController();
//        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mrequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mrequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mrequestQueue;
    }

    public  <T> void addToRequestQueue(Request<T> req, String tag){
        //set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if(mrequestQueue==null){
            mrequestQueue.cancelAll(tag);
        }

    }
//
//    public ImageLoader getImageLoader() {
//        ImageLoader imageLoader;
//        return imageLoader;
//    }
}
