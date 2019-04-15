

package com.example.bingduoduo.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.bingduoduo.R;
import com.example.bingduoduo.base.BaseFragment;

import butterknife.Bind;

/**
 * 编辑预览界面
 */
public class EditorMarkdownFragment extends BaseFragment {
    @Bind(R.id.title)
    protected TextView mName;
    private String mContent;


    public EditorMarkdownFragment() {
    }

    public static EditorMarkdownFragment getInstance() {
        EditorMarkdownFragment editorFragment = new EditorMarkdownFragment();
        return editorFragment;
    }


    boolean isPageFinish = false;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_markdown;
    }
    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean hasMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor_preview_frag, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
