package com.yilos.nailstar.requirelession.Presenter;

import com.yilos.nailstar.requirelession.model.LessionService;
import com.yilos.nailstar.requirelession.model.LessionServiceImpl;
import com.yilos.nailstar.requirelession.view.LessionView;

/**
 * Created by yilos on 15/10/24.
 */
public class LessionPresenter {

    private LessionView view;
    private LessionService service;

    public LessionPresenter(LessionView view) {
        this.view = view;
        this.service = new LessionServiceImpl();
    }

}
