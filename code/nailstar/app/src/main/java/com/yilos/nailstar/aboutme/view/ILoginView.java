package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.framework.view.IView;

/**
 * Created by yangdan on 15/11/10.
 */
public interface ILoginView extends IView {
    String getUserAccount();

    String getPassword();

    CharSequence getResourceStringById(int id);

    void setLoginButtonEnable(boolean enable);

    void setWeiboLoginButtonEnable(boolean enable);

    void setWeixinLoginButtonEnable(boolean enable);

    void setQQLoginButtonEnable(boolean enable);

    void close();
}
