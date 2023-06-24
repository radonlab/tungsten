package com.radonlab.tungsten.syntax;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class ColorTheme {

    private final Map<String, String> colorMap;

    public ColorTheme() {
        this.colorMap = new HashMap<>();
    }

    public int getColor(String name) {
        String value = colorMap.getOrDefault(name, "0xFF000000");
        return Color.parseColor(value);
    }
}