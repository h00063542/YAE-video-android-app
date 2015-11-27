package com.yilos.nailstar.requirelession.view;

import android.support.annotation.StringRes;

import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;

import java.util.List;

/**
 * Created by yilos on 15/10/27.
 */
public interface LessionView {

    /**
     * 失败提示
     */
    void refreshFailed();

    /**
     * 刷新当前求教程活动(求教程榜首)
     *
     * @param lessionActivity
     */
    void refreshActivityTopic(LessionActivity lessionActivity);

    /**
     * 通知listview: 数据已刷新
     */
    void notifyRefreshListView();

    /**
     * 设置投票页面数据
     *
     * @param voteLessionList
     */
    void setVoteLession(List<CandidateLession> voteLessionList);

    /**
     * 设置投票页面数据
     *
     * @param rankingLessionList
     */
    void setRankingLession(List<CandidateLession> rankingLessionList);

    /**
     * 刷新倒计时
     *
     * @param time
     */
    void refreshCountDown(String time);

    /**
     * 刷新图片到图库中
     *
     * @param filePath
     */
    void mediaRefresh(String filePath);

    /**
     * 显示提示信息
     *
     * @param text
     */
    void showMessage(String text);

    /**
     * 显示提示信息
     *
     * @param resId
     */
    void showMessage(@StringRes int resId);

    /**
     * 转到登录界面
     */
    void gotoLoginPage();
}
