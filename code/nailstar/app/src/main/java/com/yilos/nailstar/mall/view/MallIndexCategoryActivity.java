package com.yilos.nailstar.mall.view;

import android.os.Bundle;
import android.view.View;
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
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.mall.presenter.MallIndexCategoryPresenter;
import com.yilos.nailstar.mall.presenter.MallIndexCategoryPresenterImpl;
import com.yilos.nailstar.mall.presenter.MallIndexPresenterImpl;
import com.yilos.nailstar.topic.view.OrderFinishDialog;
import com.yilos.nailstar.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ganyue on 15/11/23.
 */
public class MallIndexCategoryActivity extends BaseActivity implements IMallIndexCategoryView{
    private StaggeredGridView categotyCommodityListView;
    private View view;
    private LinearLayout footerView;
    MallIndexCategoryListAdapter adapter;


    private MallIndexCategoryPresenter mallCategoryPresenter;

    private boolean mHasRequestedMore;
    private int page = 1;
    private int prePageNo = 10;
    private String category_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_category_list);

        initData();
        initView();
    }
    private void initView() {
        this.adapter = new MallIndexCategoryListAdapter(view.getContext());
        this.footerView = (LinearLayout)getLayoutInflater().inflate(R.layout.mall_index_footer, null);
        this.mallCategoryPresenter = new MallIndexCategoryPresenterImpl(this);

        categotyCommodityListView= (StaggeredGridView) view.findViewById(R.id.index_commodity_recommend_list);
        categotyCommodityListView.addFooterView(footerView);
        categotyCommodityListView.setAdapter(adapter);
    }
    private void initData() {
        Bundle data = getIntent().getExtras();
        category_id = data.getString(Constants.MALL_COMMODITY_CATE_ID, Constants.EMPTY_STRING);

        mallCategoryPresenter.loadCategoryPageData(category_id,prePageNo, page);

    }
    private void bindControl() {

        //滚动加载更多事件
        categotyCommodityListView.setOnScrollListener(new AbsListView.OnScrollListener() {

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

        //商品点击事件
        categotyCommodityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String product_relaId = (String) view.getTag(R.id.mall_index_product_real_id);
                Map<String, String> exParams = new HashMap<String, String>();
                exParams.put(TradeConstants.ITEM_DETAIL_VIEW_TYPE, TradeConstants.BAICHUAN_H5_VIEW);
                exParams.put(TradeConstants.ISV_CODE, Constants.ISV_CODE);
                ItemService itemService = AlibabaSDK.getService(ItemService.class);
                UiSettings uiSetting = new UiSettings();
                itemService.showItemDetailByItemId(MallIndexCategoryActivity.this, new TradeProcessCallback() {
                    @Override
                    public void onPaySuccess(TradeResult tradeResult) {
                        //弹出框，提示购买成功，去淘宝查看订单信息
                        final OrderFinishDialog successDialog = new OrderFinishDialog(MallIndexCategoryActivity.this.getViewContext());
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
        mallCategoryPresenter.loadCategoryPageData(category_id,prePageNo, page++);
        adapter.notifyDataSetChanged();
        mHasRequestedMore = false;
    }
    @Override
    public MallIndexCategoryListAdapter getMallIndexCommodityListAdapter() {
        return adapter;
    }

}
