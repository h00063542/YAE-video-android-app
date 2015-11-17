package com.yilos.nailstar.framework.view;

import android.content.Context;

/**
 * Created by yangdan on 15/11/11.
 */
public interface IView {
    Context getViewContext();

    void showMessageDialog(String title, String content);

    void showLoading(String tip);

    void hideLoading();

    void showShortToast(CharSequence text);

    void showLongToast(CharSequence text);
}
