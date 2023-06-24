package com.radonlab.tungsten.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.Scanner;

public class AssetUtil {
    public static String readAsString(Context context, int resId) {
        Resources resources = context.getResources();
        try (Scanner scanner = new Scanner(resources.openRawResource(resId))) {
            return scanner.useDelimiter("\\A").next();
        } catch (Resources.NotFoundException e) {
            Log.e("AssetUtil", "readAsString failed", e);
            return null;
        }
    }
}