package com.yilos.nailstar.requirelession.Presenter;

import android.os.Handler;

import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.model.LessionService;
import com.yilos.nailstar.requirelession.model.LessionServiceImpl;
import com.yilos.nailstar.requirelession.view.LessionView;

import java.util.List;

/**
 * Created by yilos on 15/10/24.
 */
public class LessionPresenter {

    private LessionView view;
    private LessionService service;
    private Handler mHandler = new Handler();

    private LessionActivity lessionActivity;
    private List<CandidateLession> voteLessionList;
    private List<CandidateLession> rankingLessionList;

    public LessionPresenter(LessionView view) {
        this.view = view;
        this.service = new LessionServiceImpl();
    }

    /**
     * 查询当前求教程活动并刷新
     */
    public void queryActivityTopic() {
        new Thread() {
            @Override
            public void run() {
                try {
                    lessionActivity = service.queryLessionActivity();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshActivityTopic(lessionActivity);
                        }
                    });
                } catch (Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshFailed();
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 查询投票页面数据并刷新
     */
    public void queryAndRefreshVoteLession() {
        new Thread() {
            @Override
            public void run() {
                try {
                    voteLessionList = service.queryVoteLessionList();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshVoteLession(voteLessionList);
                        }
                    });
                } catch (Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshFailed();
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 查询投票排行榜并刷新
     */
    public void queryAndRefreshRankingLession() {
        new Thread() {
            @Override
            public void run() {
                try {
                    rankingLessionList = service.queryRankingLessionList(1);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshRankingLession(rankingLessionList);
                        }
                    });
                } catch (Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshFailed();
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 切换到投票页面
     */
    public void goVoteLessionList() {

        if (voteLessionList != null) {
            view.refreshVoteLession(voteLessionList);
        } else {
            queryAndRefreshVoteLession();
        }

    }

    /**
     * 切换到排行榜页面
     */
    public void goRankingLessionList() {
        if (rankingLessionList != null) {
            view.refreshRankingLession(rankingLessionList);
        } else {
            queryAndRefreshRankingLession();
        }
    }
}
