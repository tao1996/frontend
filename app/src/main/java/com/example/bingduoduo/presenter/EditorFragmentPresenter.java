

package com.example.bingduoduo.presenter;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

import com.example.bingduoduo.base.mvp.BasePresenter;

/**
 * 编辑界面Presenter
 */
public class EditorFragmentPresenter extends BasePresenter<IEditorFragmentView> {
    //当前文件路径
    private String filePath;
    //当前本地文件名字(如果创建,则为"",可以和当前标题输入框的值不同)
    private String fileName;
    //时候为新创建文件
    private boolean isCreateFile;

    public EditorFragmentPresenter(File file) {
        if (file.isDirectory()) {
            this.filePath = file.getAbsolutePath();
            this.fileName = "";
            isCreateFile = true;
        } else {
            this.fileName = file.getName();
            this.filePath = file.getParent();
        }
    }


    /**
     * 加载当前文件
     */
    public void loadFile() {
        mCompositeSubscription.add(mDataManager.readFile(getMDFile())
                .subscribe(content -> {
                    if (getMvpView() == null) return;
                    getMvpView().onReadSuccess(fileName, content);
                }, throwable -> {
                    callFailure(-1, throwable.getMessage(), IEditorFragmentView.CALL_LOAOD_FILE);
                }));

    }

    @NonNull
    public File getMDFile() {
        return new File(filePath, fileName);
    }

    private boolean textChanged = false;

    /**
     * 刷新保存图标的状态
     */
    public void refreshMenuIcon() {
        if (getMvpView() != null) return;
        if (textChanged)
            getMvpView().otherSuccess(IEditorFragmentView.CALL_NO_SAVE);
        else
            getMvpView().otherSuccess(IEditorFragmentView.CALL_SAVE);

    }

    public void textChange() {
        textChanged = true;
        if (getMvpView() != null) {
            getMvpView().otherSuccess(IEditorFragmentView.CALL_NO_SAVE);
        }

    }

    /**
     * 保存当前内容
     *
     * @param name    the name
     * @param content the content
     */
    public void save(String name, String content) {
        saveForExit(name, content, false);
    }

    /**
     * 保存当前内容并退出
     *
     * @param name    the name
     * @param content the content
     * @param exit    the exit
     */
    public void saveForExit(String name, String content, boolean exit) {
        if (TextUtils.isEmpty(name)) {
            callFailure(-1, "名字不能为空", IEditorFragmentView.CALL_SAVE);
            return;
        }
        if (content == null) return;

        // 上一次文件名为空
        if (TextUtils.isEmpty(fileName)) {
            // 新建文件
            if (isCreateFile) {
                // 新创建文件，但是文件已经存在了
                File file = new File(filePath, name + ".md");
                if (!file.isDirectory() && file.exists()) {
                    callFailure(-1, "文件已经存在", IEditorFragmentView.CALL_SAVE);
                    return;
                }
            }
            fileName = name;
        }


        if (!fileName.endsWith(".py")) {
            fileName = fileName + ".py";
        }

        mDataManager.saveFile(getMDFile(), content).subscribe(success -> {
            if (success) {
                isCreateFile = false;
                textChanged = false;
                if (!rename(name)) {
                    callFailure(-1, "重命名失败", IEditorFragmentView.CALL_SAVE);
                    return;
                }
                if (getMvpView() != null) {
                    if (exit)
                        getMvpView().otherSuccess(IEditorFragmentView.CALL_EXIT);
                    else
                        getMvpView().otherSuccess(IEditorFragmentView.CALL_SAVE);
                }
            } else {
                callFailure(-1, "保存失败", IEditorFragmentView.CALL_SAVE);
            }
        }, throwable -> {
            callFailure(-1, "保存失败", IEditorFragmentView.CALL_SAVE);
        });
    }

    private boolean rename(String newName) {

        int end = fileName.lastIndexOf(".");
        String name = fileName.substring(0, end);
        if (newName.equals(name)) return true;

        String suffix = fileName.substring(end, fileName.length());
        if (suffix.endsWith(".py")) {
            // 重命名
            File oldFile = getMDFile();
            File newPath = new File(filePath, newName + suffix);
            if (oldFile.getAbsolutePath().equals(newPath.getAbsolutePath())) return true;

            fileName = newPath.getName();

            if (newPath.exists())//文件已经存在了
                return false;
            boolean b = oldFile.renameTo(newPath);
            return b;
        }
        return false;
    }

    public boolean isSave() {
        return !textChanged;
    }
}
