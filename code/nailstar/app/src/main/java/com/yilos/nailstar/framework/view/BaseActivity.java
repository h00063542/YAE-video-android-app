package com.yilos.nailstar.framework.view;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.yilos.widget.dialog.LoadingDialog;

/**
 * Created by yangdan on 15/10/20.
 */
public class BaseActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;

    public void showMessageDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setNegativeButton("确定", null);
        builder.show();
    }

    /**
     * 显示loading图标
     * @param tip
     */
    public void showLoading(String tip) {
        hideLoading();

        LoadingDialog dialog = new LoadingDialog(this);
        if(null != tip && tip.trim().equals("")) {
            dialog.setContent(tip);
        }
        dialog.show();
        loadingDialog = dialog;
    }

    /**
     * 隐藏loading图标
     */
    public void hideLoading() {
        if(loadingDialog != null){
            if(loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }
}
