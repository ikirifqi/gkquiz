package com.nextinnovation.gkquiz.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by rifqi on Feb 14, 2016.
 */
public class App extends Application {
    private static App sInstance;
    private static Typeface sBoldTypeface;
    private static Typeface sRegularTypeface;
    private static Database sDatabase;

    /**
     * public static methods
     */

    /** return true if the application is running on debug mode */
    public static boolean isDebug() {
        return Config.APP_MODE == Config.AppMode.DEBUG;
    }

    /** return true if the application is running on release mode */
    public static boolean isRelease() {
        return Config.APP_MODE == Config.AppMode.RELEASE;
    }

    /** return the application instance */
    public static App getInstance() {
        return sInstance;
    }

    /** return application database instance */
    public static Database getDatabase() {
        return sDatabase;
    }

    /** write standard log output */
    public static void log(@NonNull String message, @Nullable Throwable throwable) {
        if(isDebug()) Log.v(Config.APP_LOG_TAG_VERBOSE, message, throwable);
    }

    /** write information log output */
    public static void logInfo(@NonNull String message, @Nullable Throwable throwable) {
        if(isDebug()) Log.i(Config.APP_LOG_TAG_INFO, message, throwable);
    }

    /** write error log output */
    public static void logError(@NonNull String message, @Nullable Throwable throwable) {
        if(isDebug()) Log.e(Config.APP_LOG_TAG_ERROR, message, throwable);
    }

    /** write warning log output */
    public static void logWarning(@NonNull String message, @Nullable Throwable throwable) {
        if(isDebug()) Log.w(Config.APP_LOG_TAG_WARNING, message, throwable);
    }

    /** return bold font typeface */
    public static Typeface getBoldTypeface() {
        return sBoldTypeface;
    }

    /** return regular font typeface */
    public static Typeface getRegularTypeface() {
        return sRegularTypeface;
    }

    /** start the given runnable function in UI thread */
    public static void runOnUiThread(@NonNull Runnable runnable) {
        new Handler(sInstance.getApplicationContext().getMainLooper()).post(runnable);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize static instance
        sInstance = this;
        sDatabase = new Database(getApplicationContext());

        // initialize custom font family
        sBoldTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/" + Config.APP_FONT_BOLD);
        sRegularTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/" + Config.APP_FONT_REGULAR);
    }
}
