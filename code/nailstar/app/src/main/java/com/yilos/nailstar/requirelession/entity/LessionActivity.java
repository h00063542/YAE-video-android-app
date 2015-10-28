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
    private VideoLession previous;

    /**
     * 本期投票榜首
     */
    private CandidateLession current;

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

    public VideoLession getPrevious() {
        return previous;
    }

    public void setPrevious(VideoLession previous) {
        this.previous = previous;
    }

    public CandidateLession getCurrent() {
        return current;
    }

    public void setCurrent(CandidateLession current) {
        this.current = current;
    }
}
