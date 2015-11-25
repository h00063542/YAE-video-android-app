package com.yilos.nailstar.requirelession.Presenter;

import android.graphics.Bitmap;
import android.os.Handler;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.exception.NotLoginException;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.entity.VotedRecord;
import com.yilos.nailstar.requirelession.model.LessionService;
import com.yilos.nailstar.requirelession.model.LessionServiceImpl;
import com.yilos.nailstar.requirelession.view.LessionView;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.FileUtils;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.OSSUtil;
import com.yilos.nailstar.util.UUIDUtil;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yilos on 15/10/24.
 */
public class LessionPresenter {

    private static Logger logger = LoggerFactory.getLogger(LessionPresenter.class);

    private static final String VOTED_RECORD_FILE = "voted_record";

    private LessionView view;
    private LessionService service;
    private Handler mHandler = new Handler();

    private LessionActivity lessionActivity;
    private List<CandidateLession> voteLessionList;
    private List<CandidateLession> rankingLessionList;

    private File cacheDir;

    private VotedRecord votedRecord;

    private Timer timer = new Timer();
    private boolean stopCountDown;
    private TimerTask countDownTask;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public LessionPresenter(LessionView view) {

        this.view = view;
        this.service = new LessionServiceImpl();

    }

    public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
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
                    logger.error("queryAndRefreshActivityTopic failed", e);
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

        setStopCountDown(false);

        countDownTask = new TimerTask() {

            @Override
            public void run() {

                int leftSeconds = (int) (endTime - System.currentTimeMillis()) / 1000;

                if (stopCountDown) {
                    this.cancel();
                    return;
                }

                if (leftSeconds < 0) {
                    leftSeconds = 0;
                    setStopCountDown(true);
                }
                int hours = leftSeconds / (60 * 60);
                int minutes = (leftSeconds / 60) % 60;
                int seconds = leftSeconds % 60;

                final StringBuilder leftTime = new StringBuilder();

                if (hours < 10) {
                    leftTime.append("0");
                }
                leftTime.append(hours);
                leftTime.append(" : ");

                if (minutes < 10) {
                    leftTime.append("0");
                }
                leftTime.append(minutes);
                leftTime.append(" : ");

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
                    // 设置已投票状态
                    if (votedRecord != null) {
                        for (String candidateId : votedRecord.getCandidateIdList()) {
                            for (CandidateLession item : voteLessionList) {
                                if (item.getCandidateId().equals(candidateId)) {
                                    item.setVoted(1);
                                }
                            }
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshVoteLession(voteLessionList);
                        }
                    });
                } catch (Exception e) {
                    logger.error("queryAndRefreshVoteLession failed", e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.query_failed);
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
                    // 设置已投票状态
                    if (votedRecord != null) {
                        for (String candidateId : votedRecord.getCandidateIdList()) {
                            for (CandidateLession item : rankingLessionList) {
                                if (item.getCandidateId().equals(candidateId)) {
                                    item.setVoted(1);
                                }
                            }
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.refreshRankingLession(rankingLessionList);
                        }
                    });
                } catch (Exception e) {
                    logger.error("queryAndRefreshRankingLession failed", e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.query_failed);
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

    /**
     * 投票
     *
     * @param candidateLession
     */
    public void vote(final CandidateLession candidateLession) {

        new Thread() {
            @Override
            public void run() {
                try {
                    if (candidateLession.getVoted() != 0) {
                        // 已经投票
                        return;
                    }
                    service.vote(candidateLession.getCandidateId());
                    // 设置为已投票
                    if (rankingLessionList != null) {
                        for (CandidateLession item : rankingLessionList) {
                            if (item.getCandidateId().equals(candidateLession.getCandidateId())) {
                                item.setVoted(1);
                                item.setVoteCount(item.getVoteCount() + 1);
                            }
                        }
                    }
                    if (voteLessionList != null) {
                        for (CandidateLession item : voteLessionList) {
                            if (item.getCandidateId().equals(candidateLession.getCandidateId())) {
                                item.setVoted(1);
                                item.setVoteCount(item.getVoteCount() + 1);
                            }
                        }
                    }

                    saveVotedRecord(candidateLession);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.notifyRefreshListView();
                            view.showMessage(R.string.vote_success);
                        }
                    });
                } catch (Exception e) {
                    logger.error("vote failed", e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.vote_failed);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param path
     * @param fileName
     */
    public void saveImage(final Bitmap bitmap, final String path, final String fileName) {

        new Thread() {
            @Override
            public void run() {

                final String result = FileUtils.saveBitMap(bitmap, path, fileName);
                if (result != null) {
                    // 保存成功
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.mediaRefresh(result);
                            view.showMessage(R.string.save_success);
                        }
                    });
                } else {
                    // 保存失败
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.save_fail);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 保存图片
     *
     * @param url
     * @param path
     * @param fileName
     */
    public void saveImage(final String url, final String path, final String fileName) {

        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = imageLoader.loadImageSync(url);
                final String result = FileUtils.saveBitMap(bitmap, path, fileName);
                if (result != null) {
                    // 保存成功
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.mediaRefresh(result);
                            view.showMessage(R.string.save_success);
                        }
                    });
                } else {
                    // 保存失败
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.save_fail);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 举报图片
     *
     * @param candidateLession
     */
    public void reportIllegal(final CandidateLession candidateLession) {

        new Thread() {
            @Override
            public void run() {
                try {
                    service.reportIllegal(candidateLession.getCandidateId());

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.report_success);
                        }
                    });
                } catch (Exception e) {
                    logger.error("reportIllegal failed", e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.report_failed);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 查询已投票列表
     */
    public void queryVotedRecord() {
        new Thread() {
            @Override
            public void run() {
                try {
                    votedRecord = service.queryVotedRecord(new File(cacheDir, VOTED_RECORD_FILE));
                } catch (Exception e) {
                    logger.error("queryVoteRecord failed", e);
                }
                queryAndRefreshVoteLession();
            }
        }.start();
    }

    /**
     * 保存已投票列表
     *
     * @param candidateLession
     */
    public void saveVotedRecord(CandidateLession candidateLession) {

        if (votedRecord == null || (lessionActivity != null && lessionActivity.getNo() != votedRecord.getNo())) {
            votedRecord = new VotedRecord();
            votedRecord.setNo(lessionActivity.getNo());
        }

        votedRecord.getCandidateIdList().add(candidateLession.getCandidateId());

        try {
            service.saveVotedRecord(new File(cacheDir, VOTED_RECORD_FILE), votedRecord);
        } catch (IOException e) {
            logger.error("saveVotedRecord failed", e);
        }
    }

    /**
     * 提交求教程请求
     *
     * @param file
     */
    public void postCandidate(final File file) {
        OSSService ossService = OSSUtil.getDefaultOssService();
        OSSUtil.resumableUpload(ossService, ossService.getOssBucket(OSSUtil.BUCKET_YPICTURE), file.getAbsolutePath(), UUIDUtil.getUUID(), new SaveCallback() {
            @Override
            public void onSuccess(String s) {
                try {
                    service.postCandidate(Constants.YILOS_PIC_URL + s);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.upload_image_success);
                        }
                    });
                } catch (NotLoginException notLoginException) {
                    view.gotoLoginPage();
                } catch (Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(R.string.upload_image_failed);
                        }
                    });
                    logger.error("postCandidate failed", e);
                }
            }

            @Override
            public void onProgress(String s, int i, int i1) {

            }

            @Override
            public void onFailure(String s, OSSException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.showMessage(R.string.upload_image_failed);
                    }
                });
            }
        });
    }
}
