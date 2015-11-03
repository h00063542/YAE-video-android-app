package com.yilos.nailstar.requirelession.Presenter;

import android.os.Handler;

import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.model.LessionService;
import com.yilos.nailstar.requirelession.model.LessionServiceImpl;
import com.yilos.nailstar.requirelession.view.LessionView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private Timer timer = new Timer();
    private boolean stopCountDown = false;
    private TimerTask countDownTask;

    public LessionPresenter(LessionView view) {
        this.view = view;
        this.service = new LessionServiceImpl();
    }

    public void setStopCountDown(boolean stopCountDown) {
        this.stopCountDown = stopCountDown;
    }

    /**
     * 查询当前求教程活动并刷新
     */
    public void queryAndRefreshActivityTopic() {
        new Thread() {
            @Override
            public void run() {
                try {
                    lessionActivity = service.queryLessionActivity();
                    // 刷新页面
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshActivityTopic(lessionActivity);
                        }
                    });
                    // 倒计时
                    startCountDown(lessionActivity.getEndTime());
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

    // 开始倒计时
    private void startCountDown(final long endTime) {

        if (countDownTask != null) {
            countDownTask.cancel();
        }

        countDownTask = new TimerTask() {

            @Override
            public void run() {

                int leftSeconds = (int)(endTime - System.currentTimeMillis()) / 1000;

                if (stopCountDown) {
                    this.cancel();
                    return;
                }

                if (leftSeconds < 0) {
                    leftSeconds = 0;
                }
                int hours = leftSeconds / (60 * 60);
                int minutes = (leftSeconds / 60) % 60;
                int seconds = leftSeconds % 60;

                final StringBuilder leftTime = new StringBuilder();

                if (hours < 10) {
                    leftTime.append("0");
                }
                leftTime.append(hours);
                leftTime.append(":");

                if (minutes < 10) {
                    leftTime.append("0");
                }
                leftTime.append(minutes);
                leftTime.append(":");

                if (seconds < 10) {
                    leftTime.append("0");
                }
                leftTime.append(seconds);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.refreshCountDown(leftTime.toString());
                    }
                });
            }
        };

        timer.schedule(countDownTask, 0, 1000);


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
