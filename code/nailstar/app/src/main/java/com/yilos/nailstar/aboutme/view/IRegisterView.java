package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.framework.view.IView;

/**
 * Created by yangdan on 15/11/11.
 */
public interface IRegisterView extends IView {
    String getPhoneNumber();

    String getValidateCode();

    String getPassword();

    void timeValidateCodeButton(int seconds);

    void close();

    void showToast(String content);
}
