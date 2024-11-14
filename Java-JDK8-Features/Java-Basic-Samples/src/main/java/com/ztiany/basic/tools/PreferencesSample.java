package com.ztiany.basic.tools;

import java.util.prefs.Preferences;

public class PreferencesSample {

    public static void main(String... args) {
        Preferences preferences = Preferences.systemRoot();
        preferences.addPreferenceChangeListener(evt -> System.out.println(evt.getNewValue()));
        read(preferences);
        write(preferences);
    }

    private static void read(Preferences preferences) {
        System.out.println(preferences.get("key", "default"));
        System.out.println(preferences.get("key1", "default"));
        System.out.println(preferences.get("key2", "default"));
        System.out.println(preferences.get("key3", "default"));
    }

    private static void write(Preferences preferences) {
        preferences.put("key", "Value");
        preferences.put("key1", "Value1");
        preferences.put("key2", "Value2");
        preferences.put("key3", "Value3");
    }

}