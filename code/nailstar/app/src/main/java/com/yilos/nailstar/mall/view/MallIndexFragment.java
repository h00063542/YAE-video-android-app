package com.yilos.nailstar.mall.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.trade.ItemService;
import com.alibaba.sdk.android.trade.TradeConstants;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.item.ItemType;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.webview.UiSettings;
import com.etsy.android.grid.StaggeredGridView;
import com.yilos.nailstar.R;
import com.yilos.nailstar.mall.presenter.MallIndexPresenter;
import com.yilos.nailstar.mall.presenter.MallIndexPresenterImpl;
import com.yilos.nailstar.topic.view.OrderFinishDialog;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.titlebar.TitleBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ganyue on 15/11/20.
 */
public class MallIndexFragment  extends Fragment implements MallIndexView{

    private static final String MALL = "mall";



    private int screenWidth;
    // title bar
    private TitleBar mallTitleBar;

    private View view;


    LayoutInflater inflater;

    private LinearLayout headerView;
    private LinearLayout footerView;



    private LinearLayout indexCommodityHot1;
    private LinearLayout indexCommodityHot2;
    private LinearLayout indexCommodityHot3;
    private boolean mHasRequestedMore;


    private int page = 1;
    private int prePageNo = 10;


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

        this.screenWidth = metric.widthPixels;
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.fragment_mall, container, false);
        this.headerView = (LinearLayout)inflater.inflate(R.layout.mall_index_header, container, false);
        this.footerView = (LinearLayout)inflater.inflate(R.layout.mall_index_footer, container, false);

        this.indexCommodityHot1 = (LinearLayout)headerView.findViewById(R.id.index_commodity_hot_1);
        this.indexCommodityHot2 = (LinearLayout)headerView.findViewById(R.id.index_commodity_hot_2);
        this.indexCommodityHot3 = (LinearLayout)headerView.findViewById(R.id.index_commodity_hot_3);
        this.mallIndexPresenter = new MallIndexPresenterImpl(this);
        this.adapter = new MallIndexCommodityListAdapter(view.getContext());
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
        recommendListView.addHeaderView(headerView);
        recommendListView.addFooterView(footerView);

        recommendListView.setAdapter(adapter);
    }
    private void initData() {
        mallIndexPresenter.loadBannerData();
        mallIndexPresenter.loadCommodityCateData();
        mallIndexPresenter.loadHotViewData();
        mallIndexPresenter.loadRecommendPageData(prePageNo, page);
    }
    private void bindControl() {

        //类别点击事件

        //热卖点击事件
        //类别点击事件

        final Fragment thisInstance = this;
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String product_relaId = (String)v.getTag(R.id.mall_index_product_real_id);
                Map<String, String> exParams = new HashMap<String, String>();
                exParams.put(TradeConstants.ITEM_DETAIL_VIEW_TYPE, TradeConstants.BAICHUAN_H5_VIEW);
                exParams.put(TradeConstants.ISV_CODE, Constants.ISV_CODE);
                ItemService itemService = AlibabaSDK.getService(ItemService.class);
                UiSettings uiSetting = new UiSettings();
                itemService.showItemDetailByItemId(thisInstance.getActivity(), new TradeProcessCallback() {
                    @Override
                    public void onPaySuccess(TradeResult tradeResult) {
                        //弹出框，提示购买成功，去淘宝查看订单信息
                        final OrderFinishDialog successDialog = new OrderFinishDialog(MallIndexFragment.this.getContext());
                        successDialog.show();
                        final Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                successDialog.dismiss();
                                timer.cancel();
                            }
                        };
                        //3秒后消失
                        timer.schedule(task, 3000);
                    }

                    @Override
                    public void onFailure(int code, String msg) {

                    }
                }, uiSetting, Long.valueOf(product_relaId), ItemType.TAOBAO, exParams);
            }
        };

        indexCommodityHot1.setOnClickListener(listener);
        indexCommodityHot2.setOnClickListener(listener);
        indexCommodityHot3.setOnClickListener(listener);

        //滚动加载更多事件
        recommendListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!mHasRequestedMore) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if (lastInScreen >= totalItemCount) {
                        mHasRequestedMore = true;
                        //展示正在加载
                        onLoadMoreItems();
                    }
                }

            }
        });

        //推荐商品点击事件
        recommendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String product_relaId = (String)view.getTag(R.id.mall_index_product_real_id);
                Map<String, String> exParams = new HashMap<String, String>();
                exParams.put(TradeConstants.ITEM_DETAIL_VIEW_TYPE, TradeConstants.BAICHUAN_H5_VIEW);
                exParams.put(TradeConstants.ISV_CODE, Constants.ISV_CODE);
                ItemService itemService = AlibabaSDK.getService(ItemService.class);
                UiSettings uiSetting = new UiSettings();
                itemService.showItemDetailByItemId(thisInstance.getActivity(), new TradeProcessCallback() {
                    @Override
                    public void onPaySuccess(TradeResult tradeResult) {
                        //弹出框，提示购买成功，去淘宝查看订单信息
                        final OrderFinishDialog successDialog = new OrderFinishDialog(thisInstance.getContext());
                        successDialog.show();
                        final Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                successDialog.dismiss();
                                timer.cancel();
                            }
                        };
                        //3秒后消失
                        timer.schedule(task, 3000);
                    }

                    @Override
                    public void onFailure(int code, String msg) {

                    }
                }, uiSetting, Long.valueOf(product_relaId), ItemType.TAOBAO, exParams);
            }
        });

    }

    private void onLoadMoreItems() {
        mallIndexPresenter.loadRecommendPageData(prePageNo, page++);
        // notify the adapter that we can update now
        adapter.notifyDataSetChanged();
        mHasRequestedMore = false;
    }

    public MallIndexCommodityListAdapter getMallIndexCommodityListAdapter(){
        return adapter;
    }

    public LinearLayout getIndexCommodityHot1() {
        return indexCommodityHot1;
    }

    public LinearLayout getIndexCommodityHot2() {
        return indexCommodityHot2;
    }

    public LinearLayout getIndexCommodityHot3() {
        return indexCommodityHot3;
    }

    @Override
    public View getRootView() {
        return view;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

}
