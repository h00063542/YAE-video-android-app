package com.yilos.nailstar.framework.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.StringRes;
import android.widget.Toast;
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
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setNegativeButton("确定", null);
        builder.show();
    }

    public void showMessageDialogWithEvent(String title, String content, final Activity activity,String function) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * 显示loading图标
     *
     * @param tip
     */
    public void showLoading(String tip) {
        hideLoading();

        LoadingDialog dialog = new LoadingDialog(this);
        if (null != tip && tip.trim().equals("")) {
            dialog.setContent(tip);
        }
        dialog.show();
        loadingDialog = dialog;
    }


    /**
     * 隐藏loading图标
     */
    public void hideLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }

    public void showShortToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showShortToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();

    }

    public void showLongToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    }

    public void showLongToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}
