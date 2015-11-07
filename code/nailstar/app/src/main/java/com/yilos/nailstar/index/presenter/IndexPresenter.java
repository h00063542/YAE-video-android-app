package com.yilos.nailstar.index.presenter;

import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.entity.Poster;
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
    private IndexService indexService = new IndexServiceImpl();

    /**
     * 首页内容缓存
     */
    private IndexContent indexContentCache = indexService.getIndexContentFromCache();

    private IIndexView iIndexView = null;

    /**
     * 私有化构造函数
     */
    private IndexPresenter(){

    }

    /**
     * 读取单例
     * @return
     */
    public static IndexPresenter getInstance(IIndexView indexView){
        indexPresenter.iIndexView = indexView;
        return indexPresenter;
    }

    public void setIndexContentCache(IndexContent indexContent){
        indexContentCache = indexContent;
    }

    public void refreshIndexContent(boolean flush){
        refreshPosters(flush);
    }

    public void refreshPosters(final boolean flush){
        TaskManager.Task loadPosters = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    List<Poster> posters = indexService.getIndexPostersFromNet();
                    return posters;
                } catch (NetworkDisconnectException e) {
                    //e.printStackTrace();
                } catch (JSONParseException e) {
                    //e.printStackTrace();
                }

                return indexContentCache.getPosters();
            }
        };

        TaskManager.UITask<List<Poster>> updateUi = new TaskManager.UITask<List<Poster>>() {
            @Override
            public Object doWork(List<Poster> data) {
                if(!flush && (indexContentCache.getPosters() != null && indexContentCache.getPosters().equals(data))) {
                    // 不需要更新
                }
                else {
                    iIndexView.initPosters(data);
                }

                return null;
            }
        };

        new TaskManager()
                .next(loadPosters)
                .next(updateUi)
                .start();
    }

    public void refreshCategory(final boolean flush){
        TaskManager.Task loadCategory = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return indexService.getIndexCategoriesFromNet();
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
                if(!flush && (indexContentCache.getCategories() != null && indexContentCache.getCategories().equals(data))) {
                    // 不需要更新
                }
                else {
                    iIndexView.initCategoriesMenu(data);
                }

                return null;
            }
        };

        new TaskManager()
                .next(loadCategory)
                .next(updateUi)
                .start();
    }

    public void saveIndexContentCache(){
        indexService.saveIndexContent(indexContentCache);
    }
}
