package com.yilos.nailstar.framework.view;

/**
 * Created by yangdan on 15/11/11.
 */
public interface IView {
    void showMessageDialog(String title, String content);

    void showLoading(String tip);

    void hideLoading();
}
