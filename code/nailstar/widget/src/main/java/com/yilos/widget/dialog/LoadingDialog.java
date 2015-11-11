package com.yilos.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

import com.yilos.widget.R;

/**
 * Created by yangdan on 15/11/11.
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        this(context, R.style.CustomLoadingDialog);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context, R.style.CustomLoadingDialog);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    private void initDialog() {
        setCancelable(false);
        setContentView(R.layout.loading_dialog_view);
        getWindow().getAttributes().gravity = Gravity.CENTER;

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((CircleView) findViewById(R.id.cricleView)).start();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((CircleView) findViewById(R.id.cricleView)).stop();
            }
        });
    }

    /**
     * 设置提示语
     * @param content
     */
    public void setContent(String content) {
        ((TextView)findViewById(R.id.loadingText)).setText(content);
    }
}
