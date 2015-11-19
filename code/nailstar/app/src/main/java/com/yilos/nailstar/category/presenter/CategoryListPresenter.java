package com.yilos.nailstar.category.presenter;

import com.yilos.nailstar.category.model.CategoryListServiceImpl;
import com.yilos.nailstar.category.view.ICategoryListView;
import com.yilos.nailstar.framework.entity.CommonResult;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.util.TaskManager;

import java.util.List;

/**
 * Created by yangdan on 15/11/18.
 */
public class CategoryListPresenter {
    private static CategoryListPresenter instance = new CategoryListPresenter();

    private CategoryListServiceImpl service = CategoryListServiceImpl.getInstance();

    private ICategoryListView view;

    private String category;

    private int currentPage;

    /**
     * 获取单例
     * @param view
     * @return
     */
    public static CategoryListPresenter getInstance(ICategoryListView view, String category) {
        instance.view = view;
        instance.category = category;
        return instance;
    }

    private CategoryListPresenter(){}

    public void initData() {
        currentPage = 0;
        view.clearData();
        getNextPage();
    }

    public void getNextPage() {
        final int newPage = currentPage + 1;

        TaskManager.BackgroundTask loadData = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                CommonResult<List<Topic>> commonResult = new CommonResult<>();
                try {
                    List<Topic> topics = service.queryCategoryList(category, newPage);
                    commonResult.setResult(topics);
                    return commonResult;
                } catch (NetworkDisconnectException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                } catch (JSONParseException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                }
            }
        };

        TaskManager.UITask<CommonResult<List<Topic>>> uiTask = new TaskManager.UITask<CommonResult<List<Topic>>>() {
            @Override
            public Object doWork(CommonResult<List<Topic>> data) {
                if(data.isError()) {
                    view.showShortToast(data.getErrorMsg());
                    view.pauseMore();

                    return null;
                }

                view.addData(data.getResult());
                currentPage = newPage;

                return null;
            }
        };

        new TaskManager()
                .next(loadData)
                .next(uiTask)
                .start();
    }
}
