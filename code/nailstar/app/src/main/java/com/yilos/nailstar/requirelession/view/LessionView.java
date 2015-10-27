package com.yilos.nailstar.requirelession.view;

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
     * 刷新当前求教程活动
     * @param lessionActivity
     */
    void refreshActivityTopic(LessionActivity lessionActivity);

    /**
     * 刷新投票页面
     * @param voteLessionList
     */
    void refreshVoteLession(List<CandidateLession> voteLessionList);

    /**
     * 刷新投票页面
     * @param rankingLessionList
     */
    void refreshRankingLession(List<CandidateLession> rankingLessionList);
}
