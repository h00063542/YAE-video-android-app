package com.yilos.nailstar.requirelession.entity;

import java.io.Serializable;

/**
 * 当期活动详情
 */
public class LessionActivity implements Serializable {

    /**
     * 当期活动详情阶段
     */
    private int stage;

    /**
     * 当前阶段剩余时间
     */
    private long endTime;

    /**
     * 当前期数
     */
    private int no;

    /**
     * 上期已录制视频
     */
    private VideoLession previousLession;

    /**
     * 本期投票榜首
     */
    private CandidateLession currentTop;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public VideoLession getPreviousLession() {
        return previousLession;
    }

    public void setPreviousLession(VideoLession previousLession) {
        this.previousLession = previousLession;
    }

    public CandidateLession getCurrentTop() {
        return currentTop;
    }

    public void setCurrentTop(CandidateLession currentTop) {
        this.currentTop = currentTop;
    }
}
