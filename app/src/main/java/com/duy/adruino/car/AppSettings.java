package com.duy.adruino.car;

import android.content.Context;
import android.preference.PreferenceManager;

class AppSettings {
    public static String getLastConnectedDevice(Context context) {
        String device = PreferenceManager.getDefaultSharedPreferences(context).getString("connected_device", "");
        return device;
    }
}
