package com.yilos.nailstar.framework.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.yilos.nailstar.R;
import com.yilos.widget.dialog.LoadingDialog;

/**
 * Created by yangdan on 15/10/20.
 */
public class BaseActivity extends AppCompatActivity implements IView{
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 修改标题栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.orange);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getLocalClassName());
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showMessageDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    /**
     * @param title 弹框标题
     * @param content 弹框内容
     * @param sureEvent 确定事件
     * @param cancelEvent 取消事件
     */
    public void showMessageDialogWithEvent(String title, String content, DialogInterface.OnClickListener sureEvent, DialogInterface.OnClickListener cancelEvent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setPositiveButton("确定", sureEvent);

        builder.setNegativeButton("取消", cancelEvent);
        builder.show();
    }

    /**
     * 显示loading图标
     *
     * @param tip
     */
    @Override
    public void showLoading(String tip) {
        hideLoading();

        LoadingDialog dialog = new LoadingDialog(this);
        if (null != tip && !tip.trim().equals("")) {
            dialog.setContent(tip);
        }
        dialog.show();
        loadingDialog = dialog;
    }


    /**
     * 隐藏loading图标
     */
    @Override
    public void hideLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }

    @Override
    public void showShortToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showShortToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showLongToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    }

    public void showLongToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}
