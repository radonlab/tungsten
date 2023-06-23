package com.radonlab.tungsten.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestUtil {
    private static final OkHttpClient httpClient = new OkHttpClient();

    public static @Nullable String get(Context context, String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                return "";
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}