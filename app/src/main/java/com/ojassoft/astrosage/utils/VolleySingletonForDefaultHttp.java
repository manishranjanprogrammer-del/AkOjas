package com.ojassoft.astrosage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.collection.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by ojas on 24/5/16.
 */
public class VolleySingletonForDefaultHttp {

    private static VolleySingletonForDefaultHttp volleySingletonForDefaultHttp;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleySingletonForDefaultHttp(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(5);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }


    public static VolleySingletonForDefaultHttp getInstance(Context context) {
        if (volleySingletonForDefaultHttp == null) {
            volleySingletonForDefaultHttp = new VolleySingletonForDefaultHttp(context);
        }
        return volleySingletonForDefaultHttp;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
