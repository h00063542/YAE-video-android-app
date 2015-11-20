package com.yilos.nailstar.mall.view;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.etsy.android.grid.StaggeredGridView;
import com.yilos.nailstar.R;
import com.yilos.nailstar.mall.presenter.MallIndexPresenter;
import com.yilos.nailstar.mall.presenter.MallIndexPresenterImpl;
import com.yilos.widget.titlebar.TitleBar;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by ganyue on 15/11/20.
 */
public class MallIndexFragment  extends Fragment implements MallIndexView{

    private static final String MALL = "mall";
    // 屏幕宽度
    private int screenWidth;

    // title bar
    private TitleBar mallTitleBar;

    private View view;

    private StaggeredGridView recommendListView;
    MallIndexCommodityListAdapter adapter;
    private MallIndexPresenter mallIndexPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        view = inflater.inflate(R.layout.fragment_mall, container, false);
        mallIndexPresenter = new MallIndexPresenterImpl(this);
        adapter = new MallIndexCommodityListAdapter(view.getContext());
        return view;

    }
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        bindControl();
    }

    private void initView() {
        recommendListView= (StaggeredGridView) view.findViewById(R.id.index_commodity_recommend_list);
        recommendListView.setAdapter(adapter);
    }
    private void initData() {
        mallIndexPresenter.loadPageData(10, 1);
    }
    private void bindControl() {

    }
    public MallIndexCommodityListAdapter getMallIndexCommodityListAdapter(){
        return adapter;
    }
}
