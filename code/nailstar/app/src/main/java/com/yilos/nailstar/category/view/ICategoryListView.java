package com.yilos.nailstar.category.view;

import com.yilos.nailstar.framework.view.IView;
import com.yilos.nailstar.index.entity.Topic;

import java.util.List;

/**
 * Created by yangdan on 15/11/18.
 */
public interface ICategoryListView extends IView {
    void pauseMore();

    void stopMore();

    void addData(List<Topic> topics);

    void clearData();
}
