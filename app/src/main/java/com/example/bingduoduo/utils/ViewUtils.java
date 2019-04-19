package com.example.bingduoduo.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

public class ViewUtils {



    public static void startActivity(Intent intent, Activity activity,
                                     View sharedElement, String sharedElementName) {
        ActivityOptionsCompat optionsCompat
                = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, sharedElement, sharedElementName);
        try {
            ActivityCompat.startActivity(activity, intent,
                    optionsCompat.toBundle());
            //界面共享该图片元素
        } catch (IllegalArgumentException e) {
            activity.startActivity(intent);//如果异常 直接启动
        }
    }

}