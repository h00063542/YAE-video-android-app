package com.yilos.nailstar.mall.presenter;

import android.app.Fragment;
import android.net.http.SslError;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.mall.entity.Commodity;
import com.yilos.nailstar.mall.entity.CommodityCategory;
import com.yilos.nailstar.mall.entity.MallIndexBanner;
import com.yilos.nailstar.mall.model.CommodityService;
import com.yilos.nailstar.mall.model.CommodityServiceImpl;
import com.yilos.nailstar.mall.view.MallIndexCommodityListAdapter;
import com.yilos.nailstar.mall.view.MallIndexView;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.util.TaskManager;
import com.yilos.widget.pageindicator.CirclePageIndicator;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganyue on 15/11/20.
 */
public class MallIndexPresenterImpl implements MallIndexPresenter{
    MallIndexView view;

    CommodityService service;
    public MallIndexPresenterImpl(MallIndexView view){
        this.view = view;
        service = new CommodityServiceImpl();
    }

    @Override
        public void loadBannerData() {
        TaskManager.Task loadTopicRelatedInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return service.getMallIndexBanner();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<MallIndexBanner>> updateUi = new TaskManager.UITask<ArrayList<MallIndexBanner>>() {
            @Override
            public Object doWork(ArrayList<MallIndexBanner> bannerList) {
                LinearLayout  commodityCateListView =    (LinearLayout)((view.getRootView()).findViewById(R.id.mall_index_banner_ly));
                for(MallIndexBanner one:bannerList){
                    LinearLayout oneBanner =  (LinearLayout)view.getInflater().inflate(R.layout.mall_index_banner, null);
                    LinearLayout.LayoutParams lyparams= new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lyparams.height = one.getHeight();
                    oneBanner.setLayoutParams(lyparams);
                    WebView oneBannerView = (WebView)oneBanner.findViewById(R.id.mall_index_banner_webview);
                    oneBannerView.loadUrl(one.getActivity_url());
                    oneBannerView.getSettings().setSupportZoom(false);
                    oneBannerView.getSettings().setJavaScriptEnabled(true);
                    oneBannerView.getSettings().setDomStorageEnabled(true);
                    commodityCateListView.addView(oneBanner);

                }
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicRelatedInfo)
                .next(updateUi)
                .start();
    }

    @Override
    public void loadCommodityCateData() {

        TaskManager.Task loadTopicRelatedInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return service.getCommodityCategory();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<CommodityCategory>> updateUi = new TaskManager.UITask<ArrayList<CommodityCategory>>() {
            @Override
            public Object doWork(ArrayList<CommodityCategory> commodityCateList) {
                LinearLayout  commodityCateListView =    (LinearLayout)((view.getRootView()).findViewById(R.id.index_commodity_cateList));
                for(CommodityCategory one:commodityCateList){
                    LinearLayout oneCate =  (LinearLayout)view.getInflater().inflate(R.layout.mall_index_category, null);
                    LinearLayout.LayoutParams lyparams= new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lyparams.width = view.getScreenWidth()/5;
                    lyparams.gravity = Gravity.CENTER_HORIZONTAL;
                    oneCate.setLayoutParams(lyparams);

                    TextView oneCateTextView = (TextView)oneCate.findViewById(R.id.index_commodity_catetory_name);
                    oneCateTextView.setText(one.getName());
                    oneCateTextView.setTag(R.id.mall_index_catetory_id, one.getId());
                    commodityCateListView.addView(oneCate);
                }
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicRelatedInfo)
                .next(updateUi)
                .start();

        //

    }

    @Override
    public void loadHotViewData() {
        TaskManager.Task loadTopicRelatedInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return service.getHotCommodityList();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<Commodity>> updateUi = new TaskManager.UITask<ArrayList<Commodity>>() {
            @Override
            public Object doWork(ArrayList<Commodity> commodityList) {
                if(commodityList.size()>=3){
                    ((TextView)view.getIndexCommodityHot1().findViewById(R.id.index_commodity_hot_1_title)).setText(commodityList.get(0).getName());
                    ((TextView)view.getIndexCommodityHot1().findViewById(R.id.index_commodity_hot_1_desc)).setText(commodityList.get(0).getAdvdesc());
                    ((TextView)view.getIndexCommodityHot1().findViewById(R.id.index_commodity_hot_1_price)).setText(String.valueOf(commodityList.get(0).getPrice()));
                    ((com.yilos.widget.view.ImageCacheView)view.getIndexCommodityHot1().findViewById(R.id.index_commodity_hot_1_image)).setImageSrc(commodityList.get(0).getImageUrl());

                    ((TextView)view.getIndexCommodityHot2().findViewById(R.id.index_commodity_hot_2_title)).setText(commodityList.get(1).getName());
                    ((TextView)view.getIndexCommodityHot2().findViewById(R.id.index_commodity_hot_2_desc)).setText(commodityList.get(1).getAdvdesc());
                    ((TextView)view.getIndexCommodityHot2().findViewById(R.id.index_commodity_hot_2_price)).setText(String.valueOf(commodityList.get(1).getPrice()));
                    ((com.yilos.widget.view.ImageCacheView)view.getIndexCommodityHot2().findViewById(R.id.index_commodity_hot_2_image)).setImageSrc(commodityList.get(1).getImageUrl());



                    ((TextView)view.getIndexCommodityHot3().findViewById(R.id.index_commodity_hot_3_title)).setText(commodityList.get(0).getName());
                    ((TextView)view.getIndexCommodityHot3().findViewById(R.id.index_commodity_hot_3_desc)).setText(commodityList.get(0).getAdvdesc());
                    ((TextView)view.getIndexCommodityHot3().findViewById(R.id.index_commodity_hot_3_price)).setText(String.valueOf(commodityList.get(0).getPrice()));
                    ((com.yilos.widget.view.ImageCacheView)view.getIndexCommodityHot3().findViewById(R.id.index_commodity_hot_3_image)).setImageSrc(commodityList.get(0).getImageUrl());

                }


                return null;
            }
        };

        new TaskManager()
                .next(loadTopicRelatedInfo)
                .next(updateUi)
                .start();
    }

    @Override
    public void loadRecommendPageData(final int pageNo, final int prePageNo) {

        TaskManager.Task loadTopicRelatedInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    Thread.currentThread().sleep(3000);
                    return service.getRecommendCommodityList(pageNo, prePageNo);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<Commodity>> updateUi = new TaskManager.UITask<ArrayList<Commodity>>() {
            @Override
            public Object doWork(ArrayList<Commodity> commodityList) {
                MallIndexCommodityListAdapter adapter = view.getMallIndexCommodityListAdapter();
                for(Commodity one:commodityList){
                    adapter.add(one);
                }
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicRelatedInfo)
                .next(updateUi)
                .start();
    }
}