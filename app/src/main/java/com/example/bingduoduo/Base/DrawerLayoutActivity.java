package com.example.bingduoduo.Base;

import android.content.res.ColorStateList;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bingduoduo.R;

import java.lang.reflect.Method;

import butterknife.BindView;

public class DrawerLayoutActivity extends AppCompatActivity {
    private static boolean hasBackButton = false;
    @BindView(R.id.id_toolbar)
    protected android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.id_appbarLayout)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.id_navigation_view)
    protected NavigationView navigationView;

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    protected void init(){
        initToolbar(toolbar);
        initAppbarLayout(appBarLayout);
        initDrawer();
    }
    protected void initToolbar(Toolbar toolbar)
    {
        toolbar.setTitle(R.string.app_name);
        if(hasBackButton){
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    protected void initAppbarLayout(AppBarLayout appBarLayout)
    {
        this.appBarLayout.setElevation(0f);
    }
    protected void initDrawer()
    {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{{android.R.attr.state_checked, android.R.attr.state_enabled},
                        {android.R.attr.state_enabled},
                        {}},
                new int[]{BaseApplication.color(R.color.colorPrimary),
                        BaseApplication.color(R.color.colorSecondaryText), 0xffDCDDDD});
        navigationView.setItemIconTintList(colorStateList);//设置图标的颜色变化
        navigationView.setItemTextColor(colorStateList);//设置item的颜色变化
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish();
                onBackPressed();// 返回
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        setOverflowIconVisible(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setOverflowIconVisible(Menu menu) {
        try {
            Class clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(menu, true);
        } catch (Exception e) {
            Log.d("OverflowIconVisible", e.getMessage());
        }
    }

    public void onBackPressed() {//返回按钮
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
