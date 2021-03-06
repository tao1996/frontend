
package com.example.bingduoduo.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bingduoduo.R;
import com.example.bingduoduo.base.BaseDrawerLayoutActivity;
import com.example.bingduoduo.base.BaseFragment;
import com.example.bingduoduo.utils.Toast;

/**
 * The type Main activity.
 */
public class MainActivity extends BaseDrawerLayoutActivity {
    private BaseFragment mCurrentFragment;
    private int currentMenuId;
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            setDefaultFragment(R.id.content_fragment_container);
        }
    }

    @Override
    public void initData() {

    }

    private void setDefaultFragment(@IdRes int fragmentId) {
        mCurrentFragment = new FolderManagerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentId, mCurrentFragment)
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (onOptionsItemSelected(item)) {
            getDrawerLayout().closeDrawer(GravityCompat.START);
        }
        return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AboutActivity.startAboutActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private long customTime = 0;
    @Override
    public void onBackPressed() {// 返回按钮
        if (getDrawerLayout().isDrawerOpen(GravityCompat.START)) {// 侧滑菜单打开，关闭菜单
            getDrawerLayout().closeDrawer(GravityCompat.START);
            return;
        }
        if (mCurrentFragment != null && mCurrentFragment.onBackPressed()) {// 如果Fragment有处理，则不据需执行
            return;
        }
        // 没有东西可以返回了，剩下软件退出逻辑
        if (Math.abs(customTime - System.currentTimeMillis()) < 2000) {
            finish();
        } else {// 提示用户退出
            customTime = System.currentTimeMillis();
            Toast.showShort(mContext, "再按一次退出软件");
        }
    }
}
