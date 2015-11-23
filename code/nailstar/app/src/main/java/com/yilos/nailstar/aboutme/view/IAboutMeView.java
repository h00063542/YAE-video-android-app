package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;

/**
 * Created by sisilai on 15/10/28.
 */
public interface IAboutMeView {
    void initMessageCount(MessageCount messageCount);
    void getAboutMeNumber(AboutMeNumber aboutMeNumber);
    void judgeLogin();

    int getLatestMessageCount();
    void setLatestMessageCount(int lt);

    long getLatestMessageCountTime();
    void setLatestMessageCountTime(long lt);
}
