package com.wf.irulu.module.homenew1_3_0;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;

/**
 * Created by dtw on 16/5/4.
 */
public final class FontsOverride {

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        Typeface t1 = Typeface.DEFAULT;
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);

        replaceFont(staticTypefaceFieldName, regular);
        Typeface t2 = Typeface.DEFAULT;

        Log.v("hello", "t1" + t1 + ",t2" + t2);
        Log.v("hellolove", (t1 == t2) + "");
    }

    protected static void replaceFont(String staticTypefaceFieldName,final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}