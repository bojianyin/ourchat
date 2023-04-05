package com.app.ourchat.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class AppUtil {

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        Iterator var3 = appProcesses.iterator();

        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            appProcess = (ActivityManager.RunningAppProcessInfo)var3.next();
        } while(!appProcess.processName.equals(context.getPackageName()));

        if (appProcess.importance == 100) {
            return false;
        } else {
            return true;
        }
    }
}