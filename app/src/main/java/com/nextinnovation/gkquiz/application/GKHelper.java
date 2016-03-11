package com.nextinnovation.gkquiz.application;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nextinnovation.gkquiz.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public class GKHelper {
    /** this function will replace/add fragment to given container res id */
    public static void runFragment(@NonNull Activity activity, int containerResId,
                                   @NonNull Fragment fragment, boolean shouldAddToBackStack,
                                   boolean shouldReplace) {
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();

        if(shouldAddToBackStack) {
            ft.addToBackStack(fragment.getTag());
        }

        if(shouldReplace) {
            ft.replace(containerResId, fragment);
        }
        else {
            ft.add(containerResId, fragment);
        }

        ft.commit();
    }

    /** check calling thread, if this function called inside main thread, it will return true */
    public static boolean isOnUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /** return JSON string from JSON file inside assets folder */
    @Nullable
    public static String loadJSONFromAsset(@NonNull String filename) {
        try {
            String assetPath = "json/" + filename;
            InputStream is = App.getInstance().getApplicationContext().getAssets().open(assetPath);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");
        }
        catch(IOException e) {
            App.logError("Failed to load JSON from asset file " + filename, e);
        }

        return null;
    }

    /** show toast for long */
    public static void showToastLong(@NonNull Context context, int messageRes) {
        showToast(context, context.getResources().getString(messageRes), Toast.LENGTH_LONG);
    }

    /** show toast for short */
    public static void showToastShort(@NonNull Context context, int messageRes) {
        showToast(context, context.getResources().getString(messageRes), Toast.LENGTH_SHORT);
    }

    /** return an object of information about device dimensions */
    public static DisplayMetrics getDeviceDimensions(@NonNull Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /** INTERNAL HELPER: function to show toast */
    private static void showToast(Context context, String message, int length) {
        Toast toast = Toast.makeText(context, message, length);
        ViewGroup vGroup = (ViewGroup)toast.getView();
        TextView tvMessage = (TextView)vGroup.getChildAt(0);
        tvMessage.setTextSize(context.getResources().getDimension(R.dimen.toast_text_size));
        tvMessage.setGravity(Gravity.CENTER);
        tvMessage.setTypeface(App.getRegularTypeface(), Typeface.NORMAL);
        toast.show();
    }
}
