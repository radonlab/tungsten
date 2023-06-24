package com.radonlab.tungsten.syntax;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

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

    private final Map<String, String> colorMap;

    public ColorTheme() {
        this.colorMap = new HashMap<>();
    }

    public int getColor(String name) {
        String value = colorMap.getOrDefault(name, "0xFF000000");
        return Color.parseColor(value);
    }
}