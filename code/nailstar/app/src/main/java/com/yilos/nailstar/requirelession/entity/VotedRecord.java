package com.yilos.nailstar.requirelession.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 已投票列表
 */
public class VotedRecord implements Serializable {

    /**
     * 期数
     */
    private int no;

    /**
     * 已投票Id列表
     */
    private List<String> candidateIdList;

    public VotedRecord() {
        candidateIdList = new ArrayList<>();
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public List<String> getCandidateIdList() {
        return candidateIdList;
    }

    public void setCandidateIdList(List<String> candidateIdList) {
        this.candidateIdList = candidateIdList;
    }

}
