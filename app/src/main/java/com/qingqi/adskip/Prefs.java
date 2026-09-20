package com.qingqi.adskip;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public final class Prefs {
    private static final String NAME = "qingqi_prefs";
    private static final String KEY_KEYWORDS = "keywords";
    private static final String KEY_DELAY = "delay_ms";

    private static final String DEFAULT_KEYWORDS =
            "跳过,跳过广告,跳过 >,skip,skip ad,close,关闭,跳过此广告,免广告,×,x";

    private Prefs() {
    }

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static List<String> getKeywords(Context c) {
        String raw = sp(c).getString(KEY_KEYWORDS, DEFAULT_KEYWORDS);
        List<String> out = new ArrayList<>();
        for (String s : raw.split(",")) {
            String t = s.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    public static void setKeywords(Context c, String raw) {
        sp(c).edit().putString(KEY_KEYWORDS, raw == null ? DEFAULT_KEYWORDS : raw).apply();
    }

    public static int getDelay(Context c) {
        return sp(c).getInt(KEY_DELAY, 300);
    }

    public static void setDelay(Context c, int ms) {
        sp(c).edit().putInt(KEY_DELAY, Math.max(0, ms)).apply();
    }
}
