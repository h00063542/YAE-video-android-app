package com.yilos.nailstar.aboutme.view;

/**
 * Created by yangdan on 15/11/10.
 */
public interface ILoginView {
    String getUserAccount();

    String getPassword();

    void showMessageDialog(String content);

    CharSequence getResourceStringById(int id);
}
