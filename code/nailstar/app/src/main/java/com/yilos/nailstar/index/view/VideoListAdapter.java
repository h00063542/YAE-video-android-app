package com.yilos.nailstar.index.view;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.entity.CommonResult;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.util.TaskManager;

import java.util.List;

/**
 * Created by yangdan on 15/11/17.
 */
public class VideoListAdapter extends RecyclerArrayAdapter<Topic> implements RecyclerArrayAdapter.OnLoadMoreListener {
    private OnLoadPagedDataListener onLoadPagedDataListener;

    private int currentPage = 1;

    public VideoListAdapter(Context context, List<Topic> objects, OnLoadPagedDataListener onLoadPagedDataListener) {
        super(context);
        this.onLoadPagedDataListener = onLoadPagedDataListener;

        setMore(R.layout.view_more, this);
        setNoMore(R.layout.view_nomore);
        setError(R.layout.view_error);

        addAll(objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoListItemHolder(viewGroup);
    }

    @Override
    public void onLoadMore() {
        final int newPage = currentPage + 1;

        TaskManager.BackgroundTask loadTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                CommonResult<List<Topic>> commonResult = new CommonResult<>();
                try {
                    List<Topic> result = onLoadPagedDataListener.loadPagedData(newPage);
                    commonResult.setResult(result);
                    return commonResult;
                } catch (CommonException e) {
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
                    pauseMore();
                    return null;
                }

                if(null == data.getResult() || data.getResult().size() == 0) {
                    stopMore();
                    return null;
                }

                currentPage = newPage;
                addAll(data.getResult());
                return null;
            }
        };

        new TaskManager()
                .next(loadTask)
                .next(uiTask)
                .start();
    }

    public interface OnLoadPagedDataListener {
        List<Topic> loadPagedData(int page) throws CommonException;
    }
}
