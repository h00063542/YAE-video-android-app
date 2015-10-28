package com.yilos.nailstar.index.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.presenter.IndexPresenter;
import com.yilos.nailstar.player.VideoPlayerActivity;
import com.yilos.nailstar.util.CollectionUtil;
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
public class IndexFragment extends Fragment implements IIndexView {
    /**
     * 轮播图
     */
    private Banner banner;

    private IndexPresenter indexPresenter;

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        indexPresenter = IndexPresenter.getInstance(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        initViews(view);

        if(NailStarApplicationContext.getInstance().getIndexContent() != null){
            // 闪屏出现时已经加载了数据
            IndexContent indexContent = NailStarApplicationContext.getInstance().getIndexContent();
            NailStarApplicationContext.getInstance().setIndexContent(null);
            indexPresenter.setIndexContentCache(indexContent);

            init(indexContent);
        } else {
            init(null);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view) {
        banner = (Banner) view.findViewById(R.id.convenientBanner);

        // 初始化下拉刷新功能
        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.fragment_home_ptr_frame);
        pullRefreshLayout.setDurationToCloseHeader(1000);
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
                        indexPresenter.refreshIndexContent(false);
                        pullRefreshLayout.refreshComplete();
                    }
                }, 50);
            }
        });
    }

    private void init(IndexContent indexContent){
        if(indexContent != null && indexContent.getPosters() != null) {
            initPosters(indexContent.getPosters());
        } else {
            indexPresenter.refreshPosters(true);
        }


    }

    @Override
    public void initPosters(List<Poster> posters) {

        final List<ImageCacheView> views = new ArrayList<>(8);

        if(!CollectionUtil.isEmpty(posters)){
            for(Poster poster : posters){
                ImageCacheView imageCacheView = new ImageCacheView(getActivity().getApplicationContext());
                imageCacheView.setImageSrc(poster.getPicUrl());
                imageCacheView.setClickable(true);
                imageCacheView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                        startActivity(intent);
//                        SplashActivity.this.finish();
//                        SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                });
                views.add(imageCacheView);
            }
        }

        banner.setViews(new Banner.ViewCreator<Poster>() {

            @Override
            public View createView(Context context, int position, Poster data) {
                ImageCacheView imageCacheView = views.get(position);

                return imageCacheView;
            }
        }, posters);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        indexPresenter.saveIndexContentCache();
    }
}
