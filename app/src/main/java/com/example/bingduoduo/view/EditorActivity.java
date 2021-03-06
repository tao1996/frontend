
package com.example.bingduoduo.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.speech.util.FucUtil;
import com.iflytek.voicedemo.SpeechRecognitionIat;

import com.example.bingduoduo.R;
import com.example.bingduoduo.base.BaseApplication;
import com.example.bingduoduo.base.BaseToolbarActivity;
import com.example.bingduoduo.presenter.IEditorActivityView;
import com.example.bingduoduo.utils.Check;
import com.example.bingduoduo.utils.FileUtils;
import com.example.bingduoduo.utils.SystemBarUtils;

import java.io.File;

import butterknife.Bind;

import static android.content.ContentValues.TAG;


public class EditorActivity extends BaseToolbarActivity implements IEditorActivityView, View.OnClickListener {
    public static final String SHARED_ELEMENT_NAME = "SHARED_ELEMENT_NAME";
    public static final String SHARED_ELEMENT_COLOR_NAME = "SHARED_ELEMENT_COLOR_NAME";
    private static final String SCHEME_FILE = "file";
    private static final String SCHEME_Folder = "folder";

    private EditorFragment mEditorFragment;

    private String mName;
    private String currentFilePath;

    // 语音识别相关
    private SpeechRecognitionIat mReconition;
    private static StringBuffer mret= new StringBuffer();
    private Handler han = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mret.append(mReconition.getAction());
            if (mret.toString().equals("\n")) {
                Toast.makeText(EditorActivity.this, "No keywords, Please retry!", Toast.LENGTH_LONG).show();
                mret.setLength(0);
            } else {
                Toast.makeText(EditorActivity.this, mret.toString(), Toast.LENGTH_LONG).show();
                int pos = mEditorFragment.mContent.getSelectionStart();
                Editable e = mEditorFragment.mContent.getText();
                e.insert(pos, mret.toString());
                mret.setLength(0);
            }
            mReconition.stopRecognize();
        }
    };

    @Bind(R.id.pager)
    protected ViewPager mViewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_editor;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        ViewCompat.setTransitionName(mViewPager, SHARED_ELEMENT_NAME);
        getIntentData();
        mEditorFragment = EditorFragment.getInstance(currentFilePath);
        initViewPager();
        SpeechUtility.createUtility(this, "appid=5c9cc920");// appid需要和sdk的id相匹配
        mReconition = new SpeechRecognitionIat( EditorActivity.this,"userwords");

        Button btn_voice = (Button) findViewById(R.id.btn_voice);
        btn_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        //mReconition.cancelRecognize();
                        Log.d(TAG, "upup31312 : "+System.currentTimeMillis());
                        mReconition.startRecognize();
                        //han.sendEmptyMessageDelayed(0,1000);
                        //按住事件发生后执行代码的区域
                        //

                        break;
                        // 开始识别
                    }
                    case MotionEvent.ACTION_MOVE: {
                        //移动事件发生后执行代码的区域
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
//                        String ret = mReconition.getAction();
//                        Log.d(TAG, "return_message:"+ret);
                        Message message = new Message();
                        message.what=0;
                        han.sendMessageDelayed(message, 800);
                        // 松开事件发生后执行代码的区域
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void initData() {

    }

    private void initViewPager() {
        mViewPager.setAdapter(new EditFragmentAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void initStatusBar() {
        SystemBarUtils.tintStatusBar(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void otherSuccess(int flag) {
    }

    @Override
    public void onFailure(int errorCode, String message, int flag) {
        switch (flag) {
            default:
                BaseApplication.showSnackbar(getWindow().getDecorView(), message);
                break;
        }
    }


    @Override
    public void showWait(String message, boolean canBack, int flag) {
        super.showWaitDialog(message, canBack);
    }

    @Override
    public void hideWait(int flag) {
        super.hideWaitDialog();
    }

    @Override
    public void onNameChange(@NonNull String name) {
        this.mName = name;
    }

    @Override
    public void onClick(View v) {
    }



    private class EditFragmentAdapter extends FragmentPagerAdapter {

        public EditFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mEditorFragment;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }


    private void getIntentData() {
        Intent intent = this.getIntent();
        int flags = intent.getFlags();
        if ((flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0) {
            if (intent.getAction() != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
                if (SCHEME_FILE.equals(intent.getScheme())) {
                    // 文件
                    String type = getIntent().getType();
                    // mImportingUri=file:///storage/emulated/0/Vlog.xml
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = intent.getData();

                    if (uri != null && SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
                        // 这是一个文件
                        currentFilePath = FileUtils.uri2FilePath(getBaseContext(), uri);
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return "";
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private MenuItem mActionOtherOperate;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor_act, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mEditorFragment.onBackPressed()) {
                    return true;
                }
                break;
            case R.id.action_edit:// 编辑
                mViewPager.setCurrentItem(0, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
