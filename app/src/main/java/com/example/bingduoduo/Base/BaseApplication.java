package com.example.bingduoduo.Base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.bingduoduo.utils.Check;

public class BaseApplication extends Application {
    static Context context;
    static Resources resource;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resource = context.getResources();
    }

    public static synchronized Context context() {
        return context;
    }

    public static String string(int id) {
        return resource.getString(id);
    }

    public static int color(int id) {
        return resource.getColor(id);
    }

    public static Drawable drawable(int id) {
        return resource.getDrawable(id);
    }

    //======Snackbar
    public static Snackbar showSnackbar(@NonNull View view, @NonNull String message, @Snackbar.Duration int duration, @Nullable View.OnClickListener listener, @Nullable String actionStr) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        if (listener != null && Check.isEmpty(actionStr)) {
            snackbar.setAction(actionStr, listener);
        }
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showSnackbar(@NonNull View view, @NonNull int messageRes, @Snackbar.Duration int duration, @Nullable View.OnClickListener listener, @Nullable String actionStr) {
        Snackbar snackbar = Snackbar.make(view, messageRes, duration);
        if (listener != null && Check.isEmpty(actionStr)) {
            snackbar.setAction(actionStr, listener);
        }
        snackbar.show();
        return snackbar;
    }

}
