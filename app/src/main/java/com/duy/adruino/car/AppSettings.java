package com.duy.adruino.car;

import android.content.Context;
import android.preference.PreferenceManager;

class AppSettings {
    private static final String KEY_CONNECTED_DEVICE = "connected_device";

    static String getLastConnectedDevice(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_CONNECTED_DEVICE, "");
    }

    static void setLastConnectedDevice(Context context, String address) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(KEY_CONNECTED_DEVICE, address).apply();
    }
}
