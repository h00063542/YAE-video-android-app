package com.yilos.nailstar.index.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.presenter.IndexPresenter;
import com.yilos.nailstar.util.ActivityUtil;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.widget.banner.Banner;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.pageindicator.CirclePageIndicator;
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
    private Banner posterBanner;

    private Banner categoryBanner;

    private IndexPresenter indexPresenter;

    private LayoutInflater inflater;

    private View view;

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
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_index, container, false);
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
        posterBanner = (Banner) view.findViewById(R.id.posterBanner);
        categoryBanner = (Banner) view.findViewById(R.id.categoryBanner);

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

        if(indexContent != null && indexContent.getCategories() != null) {
            initCategoriesMenu(indexContent.getCategories());
        } else {
            indexPresenter.refreshCategory(true);
        }
    }

    @Override
    public void initPosters(final List<Poster> posters) {
        posterBanner.setViewCreator(new Banner.ViewCreator() {
            @Override
            public List<View> createViews() {
                final List<View> views = new ArrayList<>(8);

                if (!CollectionUtil.isEmpty(posters)) {
                    for (final Poster poster : posters) {
                        ImageCacheView imageCacheView = new ImageCacheView(getActivity());

                        Banner.LayoutParams layoutParams = new Banner.LayoutParams();
                        layoutParams.width = Banner.LayoutParams.MATCH_PARENT;
                        layoutParams.height = Banner.LayoutParams.WRAP_CONTENT;
                        imageCacheView.setLayoutParams(layoutParams);
                        imageCacheView.setAdjustViewBounds(true);
                        imageCacheView.setScaleType(ImageView.ScaleType.FIT_START);

                        imageCacheView.setImageSrc(poster.getPicUrl());
                        imageCacheView.setClickable(true);
                        imageCacheView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityUtil.toVideoPlayerPage(getActivity(), poster.getTopicId());
                            }
                        });
                        views.add(imageCacheView);
                    }
                }

                return views;
            }
        });

        ((CirclePageIndicator)view.findViewById(R.id.indicator)).setViewPager(posterBanner);
        posterBanner.play();
    }

    @Override
    public void initCategoriesMenu(final List<Category> categories) {
        if(CollectionUtil.isEmpty(categories)){
            return;
        }

        categoryBanner.setViewCreator(new Banner.ViewCreator() {
            @Override
            public List<View> createViews() {
                final List<View> gridViews = new ArrayList<>(4);
                for(int i = 0, count = categories.size(); i < count; i++){
                    if(i % 8 == 0) {
                        TableLayout tableLayout = (TableLayout)inflater.inflate(R.layout.category_gridview, null);
                        TableRow upLayout = (TableRow)tableLayout.findViewById(R.id.upLayout);
                        TableRow downLayout = (TableRow)tableLayout.findViewById(R.id.downLayout);
                        gridViews.add(tableLayout);

                        for(int j = i; j < i + 8; j++) {
                            View menuView = inflater.inflate(R.layout.category_menu_item, null);
                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                            layoutParams.weight = 1;
                            layoutParams.gravity = Gravity.CENTER;
                            menuView.setLayoutParams(layoutParams);
                            CircleImageView circleImageView = ((CircleImageView)menuView.findViewById(R.id.category_circle_image_view));
                            if(j < count) {
                                circleImageView.setImageSrc(categories.get(j).getPicUrl());
                                ((TextView)menuView.findViewById(R.id.category_text_view)).setText(categories.get(j).getName());
                                circleImageView.setClickable(true);
                            }

                            if(j - i < 4) {
                                upLayout.addView(menuView);
                            }
                            else {
                                downLayout.addView(menuView);
                            }

                            circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }
                }

                return gridViews;
            }
        });

        //((CirclePageIndicator)view.findViewById(R.id.indicator)).setViewPager(banner);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        indexPresenter.saveIndexContentCache();
    }
}
