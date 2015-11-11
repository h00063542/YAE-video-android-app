package com.yilos.nailstar.framework.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by yangdan on 15/10/20.
 */
public class BaseActivity extends android.app.Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
