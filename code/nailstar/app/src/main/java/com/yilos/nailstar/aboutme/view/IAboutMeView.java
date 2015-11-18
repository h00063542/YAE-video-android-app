package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;

import java.net.MalformedURLException;

/**
 * Created by sisilai on 15/10/28.
 */
public interface IAboutMeView {
    void initMessageCount(MessageCount messageCount);
    void getAboutMeNumber(AboutMeNumber aboutMeNumber);
    void judgeLogin();
}
