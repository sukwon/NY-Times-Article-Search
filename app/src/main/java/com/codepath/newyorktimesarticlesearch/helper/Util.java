package com.codepath.newyorktimesarticlesearch.helper;

import android.app.Activity;
import android.widget.Toast;

import com.codepath.newyorktimesarticlesearch.R;

import java.io.IOException;

public class Util {
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public static void showNetworkFailure(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,
                        activity.getString(R.string.network_failure), Toast.LENGTH_LONG).show();
            }
        });
    }
}
