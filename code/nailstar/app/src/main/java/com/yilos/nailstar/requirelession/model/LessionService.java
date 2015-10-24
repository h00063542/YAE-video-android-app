package com.yilos.nailstar.requirelession.model;

import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;

import java.util.List;

/**
 * Created by yilos on 15/10/24.
 */
public interface LessionService {

    LessionActivity queryLessionActivity();

    List<CandidateLession> queryVoteLessionList();

    List<CandidateLession> queryRankingLessionList(int page);

}
