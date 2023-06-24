package com.radonlab.tungsten.syntax;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ColorTheme {
    public static final String BLACK = "black";
    public static final String WHITE = "white";
    public static final String ACCENT1 = "accent1";
    public static final String ACCENT2 = "accent2";
    public static final String ACCENT3 = "accent3";
    public static final String ACCENT4 = "accent4";
    public static final String ACCENT5 = "accent5";
    public static final String ACCENT6 = "accent6";
    public static final String DIMMED1 = "dimmed1";
    public static final String DIMMED2 = "dimmed2";
    public static final String DIMMED3 = "dimmed3";
    public static final String DIMMED4 = "dimmed4";
    public static final String DIMMED5 = "dimmed5";

    private final JSONObject colorMap;

    public ColorTheme(JSONObject colorMap) {
        this.colorMap = colorMap;
    }

    public int getColor(String name) {
        try {
            String value = colorMap.getString(name);
            return Color.parseColor(value);
        } catch (JSONException e) {
            Log.e("ColorTheme", "getColor", e);
            return Color.MAGENTA;
        }
    }
}