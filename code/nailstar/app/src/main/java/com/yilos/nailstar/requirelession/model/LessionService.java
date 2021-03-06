package com.yilos.nailstar.requirelession.model;

import com.yilos.nailstar.framework.exception.NotLoginException;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.entity.VideoLession;
import com.yilos.nailstar.requirelession.entity.VotedRecord;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yilos on 15/10/24.
 */
public interface LessionService {

    /**
     * 查询当期活动详情
     *
     * @return LessionActivity
     * @throws IOException
     * @throws JSONException
     */
    LessionActivity queryLessionActivity() throws IOException, JSONException;

    /**
     * 查询随机九宫格（投票列表）
     *
     * @return List<CandidateLession>
     * @throws IOException
     * @throws JSONException
     */
    List<CandidateLession> queryVoteLessionList() throws IOException, JSONException;

    /**
     * 查询排行榜
     *
     * @param page
     * @return List<CandidateLession>
     * @throws IOException
     * @throws JSONException
     */
    List<CandidateLession> queryRankingLessionList(int page) throws IOException, JSONException;

    /**
     * 查看往期活动
     *
     * @param page
     * @return List<VideoLession>
     * @throws IOException
     * @throws JSONException
     */
    List<VideoLession> queryHistoryLessionList(int page) throws IOException, JSONException;

    /**
     * 求教程投票
     *
     * @param id
     * @throws IOException
     * @throws JSONException
     */
    void vote(String id) throws IOException, JSONException;

    /**
     * 举报
     *
     * @param id
     * @throws IOException
     * @throws JSONException
     */
    void reportIllegal(String id) throws IOException, JSONException;

    /**
     * 读取已投票列表
     *
     * @return
     * @throws IOException
     */
    VotedRecord queryVotedRecord(File fileName) throws IOException, JSONException;

    /**
     * 保存已投票列表
     *
     * @param fileName
     * @param votedRecord
     * @throws IOException
     */
    void saveVotedRecord(File fileName, VotedRecord votedRecord) throws IOException;

    /**
     * 提交求教程请求
     *
     * @param imageUrl
     * @throws IOException
     */
    void postCandidate(String imageUrl) throws IOException, JSONException, NotLoginException;
}
