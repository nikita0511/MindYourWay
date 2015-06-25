package in.ac.iiitd.mindyourway.Others;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by NikitaGupta on 6/22/2015.
 */
public class PreferencesData {

    public static final String PREFERENCE_FILE_PREFIX = "pref_file_prefix";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putString(key, value).commit();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putInt(key, value).commit();
    }


    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putBoolean(key, value).commit();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getString(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getBoolean(key, defaultValue);
    }
}
