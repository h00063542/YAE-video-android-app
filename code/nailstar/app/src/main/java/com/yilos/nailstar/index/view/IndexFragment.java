package com.yilos.nailstar.index.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.model.IndexServiceImpl;
import com.yilos.widget.banner.Banner;
import com.yilos.widget.pullrefresh.PullRefreshLayout;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {
    /**
     * 轮播图
     */
    private Banner convenientBanner;

    private IndexContent indexContent;

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        if(NailStarApplicationContext.getInstance().getIndexContent() != null){
            indexContent = NailStarApplicationContext.getInstance().getIndexContent();
        } else {
            indexContent = new IndexServiceImpl().getIndexContent();
        }

        initViews(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //convenientBanner.startFlipping();
    }

    private void initViews(View view){
        convenientBanner = (Banner) view.findViewById(R.id.convenientBanner);
        final List<ImageCacheView> views = new ArrayList<>(8);
        for(Poster poster : indexContent.getPosters()){
            ImageCacheView imageCacheView = new ImageCacheView(getActivity().getApplicationContext());
            imageCacheView.setImageSrc(poster.getPicUrl());
            views.add(imageCacheView);
        }

        convenientBanner.setViews(new Banner.ViewCreator<Poster>(){

            @Override
            public View createView(Context context, int position, Poster data) {
//                ImageCacheView imageCacheView = new ImageCacheView(context);
//                imageCacheView.setImageSrc(data.getPicUrl());
                ImageCacheView imageCacheView = views.get(position);

                return imageCacheView;
            }
        }, indexContent.getPosters());

        // 初始化下拉刷新功能
        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.fragment_home_ptr_frame);
        pullRefreshLayout.setDurationToCloseHeader(1500);
        pullRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefreshLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

}
