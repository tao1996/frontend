
package com.example.bingduoduo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import butterknife.Bind;
import com.example.bingduoduo.R;
import com.example.bingduoduo.base.BaseToolbarActivity;
import com.example.bingduoduo.utils.SystemBarUtils;
import com.example.bingduoduo.utils.SystemUtils;

public class AboutActivity extends BaseToolbarActivity {
    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.description)
    TextView description;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    public static void startAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        version.setText(String.format(getString(R.string.version_string), "0.0.1"));
        String fromAssets = SystemUtils.getAssertString(mContext.getApplicationContext(), "description.txt");
        description.setText(R.string.app_name);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initStatusBar() {
        SystemBarUtils.tintStatusBar(this, getResources().getColor(R.color.colorPrimary));
    }







    @NonNull
    @Override
    protected String getTitleString() {
        return this.getString(R.string.about);
    }


}
