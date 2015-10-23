package com.yilos.nailstar.index.presenter;

import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.model.IndexService;
import com.yilos.nailstar.index.model.IndexServiceImpl;

/**
 * Created by yangdan on 15/10/20.
 */
public class IndexPresenter {
    private static IndexPresenter indexPresenter = new IndexPresenter();

    private IndexService indexService = new IndexServiceImpl();

    public static IndexPresenter getInstance(){
        return indexPresenter;
    }

    public IndexContent getIndexContent() throws NetworkDisconnectException, JSONParseException {
        return indexService.getIndexContent();
    }

    public void preloadIndexContent() throws NetworkDisconnectException, JSONParseException {
        NailStarApplicationContext.getInstance().setIndexContent(indexService.getIndexContent());
    }
}
