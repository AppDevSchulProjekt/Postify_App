package com.appdev.postify;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.Secure;

import com.appdev.postify.activities.MainActivity;
import com.appdev.postify.datastorage.PreferencesManagement;
import com.appfarms.android.afpushmanager.AFPushConfig;
import com.appfarms.android.afpushmanager.AFPushManager;
import com.appfarms.android.afpushmanager.data.AFPushNotification;
import com.appfarms.android.afpushmanager.enums.AFPushEnvironment;

import java.util.HashMap;

/**
 * Created by daniel on 13.04.16.
 */
public class BaseApplication extends Application {
    public static final String BADGE_KEY = "badge";
    private static BaseApplication instance;
    private final String API_KEY = "ab1fe97c2579cb11304e4d265665398b";
    private final String SENDER_ID = "895602186875";
    private final String ATTRIUTE_KEY_DEVICE_ID = "DEVICE_ID";
    private String deviceID;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Get a 64-bit number as a hex string (randomly generated when the user first sets up)
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        // AFPushEnvironment set Live Mode
        AFPushEnvironment environment = AFPushEnvironment.LIVE;
        // Create AppFarms Push Config
        AFPushConfig config = new AFPushConfig(this, API_KEY, SENDER_ID, environment) {
            @Override
            public PendingIntent getNotificationPendingIntent(AFPushNotification notification) {
                // Create an intent for specific components
                Intent intent = new Intent(BaseApplication.this, MainActivity.class);
                // launching a new instance of that activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // A description of an Intent and target action to perform with it
                PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                incrementBadge();
                return pendingIntent;
            }
        };
        // Create AFPushManager and Register Activity Lifecycle Callbacks
        AFPushManager.getInstance().init(config);
        // A Map is a data structure consisting of a set of keys and values in which each key is mapped to a single value.
        HashMap<String,Object> deviceIDAttribute = new HashMap<String,Object>();
        // Maps the specified key to the specified value.
        deviceIDAttribute.put(ATTRIUTE_KEY_DEVICE_ID, deviceID);
        // Create AFPushManager and put Device Attributes
        AFPushManager.getInstance().putDeviceAttributes(deviceIDAttribute);
        AFPushManager.getInstance().setBadgeCount(0);
        PreferencesManagement.saveStringPreferences(BADGE_KEY, "0");
    }


    public static BaseApplication getInstance() {
        return instance;
    }

    private void incrementBadge() {
        Integer badge = Integer.valueOf(PreferencesManagement.getStringPreferences(BADGE_KEY));
        badge++;
        PreferencesManagement.saveStringPreferences(BADGE_KEY, String.valueOf(badge));
        AFPushManager.getInstance().setBadgeCount(badge);
    }

    public static void removeAllBadges() {
        AFPushManager.getInstance().setBadgeCount(0);
        PreferencesManagement.saveStringPreferences(BADGE_KEY, "0");
    }
}
