package com.yilos.nailstar.index.presenter;

import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.entity.CommonResult;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.framework.exception.NoWatchException;
import com.yilos.nailstar.framework.exception.NotLoginException;
import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.index.model.IndexService;
import com.yilos.nailstar.index.model.IndexServiceImpl;
import com.yilos.nailstar.index.view.IIndexView;
import com.yilos.nailstar.util.TaskManager;

import java.util.List;

/**
 * Created by yangdan on 15/10/20.
 */
public class IndexPresenter {
    /**
     * 单例
     */
    private static IndexPresenter indexPresenter = new IndexPresenter();

    /**
     * 首页相关服务
     */
    private IndexService service = new IndexServiceImpl();

    /**
     * 首页内容缓存
     */
    private IndexContent indexContentCache;

    private IIndexView view = null;
    /**
     * 已经完成的刷新任务
     */
    private int finishRefreshTaskCount;
    private boolean isRefreshing;

    /**
     * 私有化构造函数
     */
    private IndexPresenter(){
        if(NailStarApplicationContext.getInstance().getIndexContent() != null){
            // 闪屏出现时已经加载了数据
            IndexContent indexContent = NailStarApplicationContext.getInstance().getIndexContent();
            NailStarApplicationContext.getInstance().setIndexContent(null);
            setIndexContentCache(indexContent);
        } else {
            setIndexContentCache(service.getIndexContentFromCache());
        }
    }

    /**
     * 读取单例
     * @return
     */
    public static IndexPresenter getInstance(IIndexView indexView){
        indexPresenter.view = indexView;
        return indexPresenter;
    }

    public void setIndexContentCache(IndexContent indexContent){
        indexContentCache = indexContent;
    }

    public void refreshIndexContent(boolean flush){
        isRefreshing = true;
        finishRefreshTaskCount = 0;

        if(flush || indexContentCache == null) {
            refreshPosters();
            refreshCategory();
            refreshLatestTopic();
            refreshHotestTopic();
            refreshWatchTopic();
        }
        else {
            view.initPosters(indexContentCache.getPosters());
            view.initCategoriesMenu(indexContentCache.getCategories());
            view.initLatestTopics(indexContentCache.getLatestTopics());
            view.initHotestTopics(indexContentCache.getHotestTopics());
            refreshWatchTopic();

            view.finishRefresh();
        }
    }

    public void refreshPosters(){
        TaskManager.Task loadPosters = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    List<Poster> posters = service.getIndexPostersFromNet();
                    return posters;
                } catch (NetworkDisconnectException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                } catch (JSONParseException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                }

                return indexContentCache.getPosters();
            }
        };

        TaskManager.UITask<List<Poster>> updateUi = new TaskManager.UITask<List<Poster>>() {
            @Override
            public Object doWork(List<Poster> data) {
                if(indexContentCache.getPosters() == null || !indexContentCache.getPosters().equals(data)){
                    view.initPosters(data);
                }
                completeRefresh();
                return null;
            }
        };

        new TaskManager()
                .next(loadPosters)
                .next(updateUi)
                .start();
    }

    public void refreshCategory(){
        TaskManager.Task loadCategory = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return service.getIndexCategoriesFromNet();
                } catch (NetworkDisconnectException e) {
                    //e.printStackTrace();
                } catch (JSONParseException e) {
                    //e.printStackTrace();
                }

                return indexContentCache.getCategories();
            }
        };

        TaskManager.UITask<List<Category>> updateUi = new TaskManager.UITask<List<Category>>() {
            @Override
            public Object doWork(List<Category> data) {
                if((indexContentCache.getCategories() != null && indexContentCache.getCategories().equals(data))) {
                    // 不需要更新
                }
                else {
                    view.initCategoriesMenu(data);
                }

                completeRefresh();
                return null;
            }
        };

        new TaskManager()
                .next(loadCategory)
                .next(updateUi)
                .start();
    }

    public void refreshLatestTopic() {
        TaskManager.Task loadLatestTopic = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    List<Topic> topics = service.getLatestTopicFromNet(1);
                    return topics;
                } catch (NetworkDisconnectException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                } catch (JSONParseException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                }

                return indexContentCache.getLatestTopics();
            }
        };

        TaskManager.UITask<List<Topic>> updateUi = new TaskManager.UITask<List<Topic>>() {
            @Override
            public Object doWork(List<Topic> data) {
                if(indexContentCache.getLatestTopics() == null || !indexContentCache.getLatestTopics().equals(data)){
                    view.initLatestTopics(data);
                }
                completeRefresh();
                return null;
            }
        };

        new TaskManager()
                .next(loadLatestTopic)
                .next(updateUi)
                .start();
    }

    public void refreshHotestTopic() {
        TaskManager.Task loadHotestTopic = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    List<Topic> topics = service.getHotestTopicFromNet(1);
                    return topics;
                } catch (NetworkDisconnectException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                } catch (JSONParseException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                }

                return indexContentCache.getHotestTopics();
            }
        };

        TaskManager.UITask<List<Topic>> updateUi = new TaskManager.UITask<List<Topic>>() {
            @Override
            public Object doWork(List<Topic> data) {
                if(indexContentCache.getHotestTopics() == null || !indexContentCache.getHotestTopics().equals(data)){
                    view.initLatestTopics(data);
                }
                completeRefresh();
                return null;
            }
        };

        new TaskManager()
                .next(loadHotestTopic)
                .next(updateUi)
                .start();
    }

    public void refreshWatchTopic() {
        TaskManager.Task loadHotestTopic = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                CommonResult<List<Topic>> commonResult = new CommonResult<>();
                try {
                    List<Topic> topics = getWatchTopicByPage(1);
                    commonResult.setResult(topics);
                    return commonResult;
                } catch (NetworkDisconnectException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                } catch (JSONParseException e) {
                    // 首页刷新因为有缓存机制，失败了也不需要提醒
                    //e.printStackTrace();
                } catch (NotLoginException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg("NotLogin");
                } catch (NoWatchException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg("NotWatch");
                } catch (CommonException e) {

                }

                return commonResult;
            }
        };

        TaskManager.UITask<CommonResult<List<Topic>>> updateUi = new TaskManager.UITask<CommonResult<List<Topic>>>() {
            @Override
            public Object doWork(CommonResult<List<Topic>> data) {
                if(data.isError()) {
                    if("NotLogin".equals(data.getErrorMsg())) {
                        view.showNotLoginView();
                    } else if("NotWatch".equals(data.getErrorMsg())) {
                        view.showNotWatchView();
                    }
                } else {
                    view.initWatchTopics(data.getResult());
                }

                completeRefresh();
                return null;
            }
        };

        new TaskManager()
                .next(loadHotestTopic)
                .next(updateUi)
                .start();
    }

    public void saveIndexContentCache(){
        service.saveIndexContent(indexContentCache);
    }

    public List<Topic> getLatestTopicByPage(int page) throws NetworkDisconnectException, JSONParseException {
        return service.getLatestTopicFromNet(page);
    }

    public List<Topic> getHotestTopicByPage(int page) throws NetworkDisconnectException, JSONParseException {
        return service.getHotestTopicFromNet(page);
    }

    public List<Topic> getWatchTopicByPage(int page) throws NetworkDisconnectException, JSONParseException, CommonException {
        LoginAPI loginAPI = LoginAPI.getInstance();
        if(!loginAPI.isLogin()) {
            throw new NotLoginException("没有登录");
        }

        try {
            return service.getWatchTopicFromNet(loginAPI.getLoginUserId(), page);
        } catch (CommonException e) {
            if("该用户还没关注过其他用户".equals(e.getMessage())) {
                throw new NoWatchException("该用户还没关注过其他用户", e);
            }
        }
        return null;
    }

    private synchronized void completeRefresh() {
        finishRefreshTaskCount++;
        if(finishRefreshTaskCount == 5) {
            isRefreshing = false;
            view.finishRefresh();
        }
    }
}
