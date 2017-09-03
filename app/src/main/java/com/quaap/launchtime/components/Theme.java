package com.quaap.launchtime.components;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;

import com.quaap.launchtime.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Copyright (C) 2017   Tom Kliethermes
 *
 * This file is part of LaunchTime and is is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

public class Theme {

    private Context ctx;


    private Map<String, BuiltinTheme> builtinThemes = new LinkedHashMap<>();

    private IconsHandler iconsHandler;

    public Theme(Context ctx, IconsHandler ich) {
        this.ctx = ctx;
        iconsHandler = ich;
        initBuiltinIconThemes();
    }



    //TODO: load these from a file / other package

    private void initBuiltinIconThemes() {
        builtinThemes.put(IconsHandler.DEFAULT_PACK, new DefaultTheme(IconsHandler.DEFAULT_PACK, ctx.getString(R.string.icons_pack_default_name)));

        BuiltinTheme bw = new MonochromeTheme("bw", "BW")
                .setColor(Thing.Mask, Color.TRANSPARENT)
                .setColor(Thing.Text, Color.WHITE)
                .setColor(Thing.AltText, Color.WHITE)
                .setColor(Thing.Background, Color.BLACK)
                .setColor(Thing.AltBackground, Color.parseColor("#ff222222"));

        builtinThemes.put(bw.getPackKey(), bw);

        BuiltinTheme bwicon = new MonochromeTheme("bwicon", ctx.getString(R.string.theme_bw))
                .setColor(Thing.Mask, Color.WHITE)
                .setColor(Thing.Text, Color.WHITE)
                .setColor(Thing.AltText, Color.WHITE)
                .setColor(Thing.Background, Color.BLACK)
                .setColor(Thing.AltBackground, Color.parseColor("#ff222222"));

        builtinThemes.put(bwicon.getPackKey(), bwicon);


        BuiltinTheme termcap = new MonochromeTheme("termcap", ctx.getString(R.string.theme_termcap))
                .setColor(Thing.Mask, Color.parseColor("#dd22ff22"))
                .setColor(Thing.Text, Color.parseColor("#dd22ff22"))
                .setColor(Thing.AltText, Color.parseColor("#dd22ff22"))
                .setColor(Thing.Background, Color.BLACK)
                .setColor(Thing.AltBackground, Color.parseColor("#dd112211"));

        builtinThemes.put(termcap.getPackKey(), termcap);


        BuiltinTheme coolblue = new MonochromeTheme("coolblue", ctx.getString(R.string.theme_coolblue))
                .setColor(Thing.Mask, Color.parseColor("#ff1111ff"))
                .setColor(Thing.Text, Color.parseColor("#eeffffff"))
                .setColor(Thing.AltText, Color.parseColor("#eeffffff"))
                .setColor(Thing.Background, Color.parseColor("#88000077"))
                .setColor(Thing.AltBackground, Color.parseColor("#881111ff"));

        builtinThemes.put(coolblue.getPackKey(), coolblue);

        BuiltinTheme redplanet = new MonochromeTheme("redplanet", ctx.getString(R.string.theme_redplanet))
                .setColor(Thing.Mask, Color.parseColor("#ffff2222"))
                .setColor(Thing.Text, Color.parseColor("#eeff2222"))
                .setColor(Thing.AltText, Color.parseColor("#eeff2222"))
                .setColor(Thing.Background, Color.parseColor("#99aa1111"))
                .setColor(Thing.AltBackground, Color.parseColor("#22121111"));

        builtinThemes.put(redplanet.getPackKey(), redplanet);

        BuiltinTheme ladypink = new MonochromeTheme("ladypink", ctx.getString(R.string.theme_ladypink))
                .setColor(Thing.Mask, Color.parseColor("#ffff1493"))
                .setColor(Thing.Text, Color.parseColor("#eeffffff"))
                .setColor(Thing.AltText, Color.parseColor("#eeffc0cb"))
                .setColor(Thing.Background, Color.parseColor("#ffff69b4"))
                .setColor(Thing.AltBackground, Color.parseColor("#ffff1493"));

        builtinThemes.put(ladypink.getPackKey(), ladypink);
    }

    public  Map<String, BuiltinTheme> getBuiltinIconThemes() {
        return builtinThemes;
    }


    public boolean isBuiltinTheme(String packagename) {
        return builtinThemes.containsKey(packagename);
    }

    public BuiltinTheme getBuiltinTheme(String packagename) {
        return builtinThemes.get(packagename);
    }



    private final String [] COLOR_PREFS = {"cattab_background", "cattabselected_background", "cattabselected_text",  "cattabtextcolor", "cattabtextcolorinv",
            "wallpapercolor",  "textcolor"};

    private Thing [] THING_MAP = {Thing.AltBackground, Thing.AltBackground, Thing.AltText, Thing.Text, Thing.Background, Thing.Background, Thing.Text};


    private int [] getColorDefaults()  {
        return new int [] {getResColor(R.color.cattab_background), getResColor(R.color.cattabselected_background),
                getResColor(R.color.cattabselected_text),  getResColor(R.color.textcolor), getResColor(R.color.textcolorinv),
                Color.TRANSPARENT,  getResColor(R.color.textcolor)};
    };


    private int getResColor(int res) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ctx.getColor(res);
        } else {
            return ctx.getResources().getColor(res);
        }
    }


    private int getCurrentThemeColor(String pref) {
        BuiltinTheme theme = builtinThemes.get(iconsHandler.getIconsPackPackageName());
        if (theme!=null && theme.hasColors()) {
            int max = COLOR_PREFS.length;
            for (int i=0; i<max; i++) {
                if (pref.equals(COLOR_PREFS[i])) {
                    return theme.getColor(THING_MAP[i]);
                }
            }
        }

        int [] colorDefaults = getColorDefaults();
        int max = COLOR_PREFS.length;
        for (int i=0; i<max; i++) {
            if (pref.equals(COLOR_PREFS[i])) {
                return colorDefaults[i];
            }
        }
        throw new IllegalArgumentException("No such preference '" + pref + "'");
    }


    private String getThemePrefName(String pref) {
        return "theme_" + iconsHandler.getIconsPackPackageName() + "_" + pref;
    }


    public void resetUserColors() {


        SharedPreferences.Editor themeedit = ctx.getSharedPreferences("theme", Context.MODE_PRIVATE).edit();
        SharedPreferences.Editor appedit = PreferenceManager.getDefaultSharedPreferences(ctx).edit();

        try {

            int max = COLOR_PREFS.length;
            for (int i=0; i<max; i++) {
                appedit.putInt(COLOR_PREFS[i],  getCurrentThemeColor(COLOR_PREFS[i]));
                //themeedit.putInt(getThemePrefName(COLOR_PREFS[i]),  getCurrentThemeColor(COLOR_PREFS[i]));
                themeedit.remove(getThemePrefName(COLOR_PREFS[i]));
            }

        } finally {
            appedit.apply();
            themeedit.apply();
        }
    }


    public void saveUserColors() {

        SharedPreferences appprefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor themeedit = ctx.getSharedPreferences("theme",Context.MODE_PRIVATE).edit();

        try {

            int max = COLOR_PREFS.length;
            for (int i=0; i<max; i++) {
                themeedit.putInt(getThemePrefName(COLOR_PREFS[i]),  appprefs.getInt(COLOR_PREFS[i], getCurrentThemeColor(COLOR_PREFS[i])));
            }

        } finally {
            themeedit.apply();
        }
    }


    public boolean restoreUserColors() {

        SharedPreferences themeprefs = ctx.getSharedPreferences("theme",Context.MODE_PRIVATE);
        SharedPreferences.Editor appedit = PreferenceManager.getDefaultSharedPreferences(ctx).edit();

        try {

            int max = COLOR_PREFS.length;
            for (int i=0; i<max; i++) {
                appedit.putInt(COLOR_PREFS[i],  themeprefs.getInt(getThemePrefName(COLOR_PREFS[i]), getCurrentThemeColor(COLOR_PREFS[i])));
            }

        } finally {
            appedit.apply();
        }
        return themeprefs.contains(getThemePrefName(COLOR_PREFS[0]));
    }


    private enum Thing {Mask, Text, AltText, Background, AltBackground}


    abstract class BuiltinTheme {

        private String mKey;
        private String mName;

        private Map<Thing,Integer> mColors = new HashMap<>();

        BuiltinTheme(String key, String name) {
            this(key, name, null);
        }

        BuiltinTheme(String key, String name, Map<Thing, Integer> colors) {
            mKey = key;
            mName = name;
            if (colors != null) {
                mColors.putAll(colors);
            }

        }

        String getPackKey() {
            return mKey;
        }

        String getPackName() {
            return mName;
        }

        public abstract Drawable getDrawable(ComponentName componentName, String uristr);

        boolean hasColors() {
            return mColors.size()>0;
        }

        BuiltinTheme setColor(Thing thing, int color) {
            mColors.put(thing, color);
            return this;
        }

        Integer getColor(Thing thing) {
            Integer val = mColors.get(thing);
            if (val == null) val = Color.BLACK;
            return val;
        }



        void applyTheme() {

            //SharedPreferences themeprefs = ctx.getSharedPreferences("theme",Context.MODE_PRIVATE);

            SharedPreferences appprefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            SharedPreferences.Editor appedit = appprefs.edit();
            try {

                int[] colorDefaults = getColorDefaults();
                int max = COLOR_PREFS.length;
                for (int i = 0; i < max; i++) {
                    if (hasColors()) {
                        appedit.putInt(COLOR_PREFS[i], getColor(THING_MAP[i]));
                    } else {
                        appedit.putInt(COLOR_PREFS[i], colorDefaults[i]);
                    }
                }
            } finally {
                appedit.apply();
            }

        }


    }


    private class DefaultTheme extends BuiltinTheme {

        DefaultTheme(String key, String name) {
            super(key, name);
        }

        public DefaultTheme(String key, String name, Map<Thing, Integer> colors) {
            super(key, name, colors);
        }
        @Override
        public Drawable getDrawable(ComponentName componentName, String uristr) {
            return iconsHandler.getDefaultAppDrawable(componentName, uristr);
        }

    }



    private class MonochromeTheme extends BuiltinTheme {
        MonochromeTheme(String key, String name) {
            super(key, name);
        }

        public MonochromeTheme(String key, String name, Map<Thing, Integer> colors) {
            super(key, name, colors);
        }

        @Override
        public Drawable getDrawable(ComponentName componentName, String uristr) {

            //Log.d(TAG, "getDrawable called for " + componentName.getPackageName());

            Drawable app_icon = iconsHandler.getDefaultAppDrawable(componentName, uristr);


            int mask_color = getColor(Thing.Mask);

            if (mask_color != Color.TRANSPARENT) {

                app_icon = app_icon.mutate();
                if (mask_color == Color.WHITE) {
                    app_icon = convertToGrayscale(app_icon);
                } else {
                    PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                    app_icon.setColorFilter(getColor(Thing.Mask), mode);
                }
            }


            return app_icon;
        }


    }

    public class PolychromeTheme extends BuiltinTheme {
        private int [] mFGColors;
        private int mBGColor;

        public PolychromeTheme(String key, String name, int [] color, int bgcolor) {
            super(key, name);
            mFGColors = Arrays.copyOf(color, color.length);
            mBGColor = bgcolor;
        }

        @Override
        public Drawable getDrawable(ComponentName componentName, String uristr) {

            //Log.d(TAG, "getDrawable called for " + componentName.getPackageName());

            Drawable app_icon = iconsHandler.getDefaultAppDrawable(componentName, uristr);

            app_icon = app_icon.mutate();


            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;

            int color = Math.abs(componentName.getPackageName().hashCode()) % mFGColors.length;
            app_icon.setColorFilter(mFGColors[color], mode);

            return app_icon;
        }


    }

    private Drawable convertToGrayscale(Drawable drawable)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }

}