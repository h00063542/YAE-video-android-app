package com.yilos.nailstar.requirelession.model;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.entity.VideoLession;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by yilos on 15/10/24.
 */
public interface LessionService {

    /**
     * 查询当期活动详情
     * @return LessionActivity
     * @throws IOException
     * @throws JSONException
     */
    LessionActivity queryLessionActivity() throws IOException, JSONException;

    /**
     * 查询随机九宫格（投票列表）
     * @return List<CandidateLession>
     * @throws IOException
     * @throws JSONException
     */
    List<CandidateLession> queryVoteLessionList() throws IOException, JSONException;

    /**
     * 查询排行榜
     * @param page
     * @return List<CandidateLession>
     * @throws IOException
     * @throws JSONException
     */
    List<CandidateLession> queryRankingLessionList(int page) throws IOException, JSONException;

    /**
     * 查看往期活动
     * @param page
     * @return List<VideoLession>
     * @throws IOException
     * @throws JSONException
     */
    List<VideoLession> queryHistoryLessionList(int page) throws IOException, JSONException;


}
