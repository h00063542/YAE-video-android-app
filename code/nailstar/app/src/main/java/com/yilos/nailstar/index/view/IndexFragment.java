package com.yilos.nailstar.index.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.index.presenter.IndexPresenter;
import com.yilos.nailstar.topic.view.TopicVideoPlayerActivity;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.banner.Banner;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.pageindicator.CirclePageIndicator;
import com.yilos.widget.pageindicator.TabPageIndicator;
import com.yilos.widget.pullrefresh.PullRefreshLayout;
import com.yilos.widget.view.ImageCacheView;
import com.yilos.widget.view.MaxHeightGridLayoutManager;

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

    private CustomRecyclerView latestListView;
    private CustomRecyclerView hotestListView;
    private CustomRecyclerView watchListView;

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

    private void initViews(final View view) {
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

        // 初始化最热、最新、关注视频Tab页
        latestListView = initVideoRecycleView();
        hotestListView = initVideoRecycleView();
        watchListView = initVideoRecycleView();
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.videoListPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                RecyclerView newView = null;
                switch (position) {
                    case 0:
                        newView = latestListView;
                        break;
                    case 1:
                        newView = hotestListView;
                        break;
                    case 2:
                        newView = watchListView;
                        break;
                    default:
                        newView = new RecyclerView(getActivity());
                        break;
                }

                if (newView.getParent() != null) {
                    container.removeView(newView);
                }

                container.addView(newView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return newView;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "最新";
                    case 1:
                        return "热播";
                    case 2:
                        return "关注";
                    default:
                        return "";
                }
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //container.removeViewAt(position);
            }
        });

        final TabPageIndicator tabPageIndicator = (TabPageIndicator)view.findViewById(R.id.videoListPagerIndicator);
        tabPageIndicator.setViewPager(viewPager);

        NestedScrollingScrollView scrollView = (NestedScrollingScrollView)this.view.findViewById(R.id.indexScrollView);
        scrollView.setOverScrollMode(NestedScrollingScrollView.OVER_SCROLL_NEVER);
        scrollView.setFillViewport(true);

        scrollView.setScrollViewListener(new NestedScrollingScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(NestedScrollingScrollView scrollView, int x, int y, int oldx, int oldy, int xRange, int yRange) {
                scrollView.setMaxScrollY((int) tabPageIndicator.getY());
            }
        });

//        this.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect rect = new Rect();
//                view.getDrawingRect(rect);
//                int height = rect.height() - getResources().getDimensionPixelSize(R.dimen.index_tab_height) - getResources().getDimensionPixelSize(R.dimen.common_title_bar_height);
//                ((MaxHeightGridLayoutManager) latestListView.getLayoutManager()).setMaxHeight(height);
//                ((MaxHeightGridLayoutManager) hotestListView.getLayoutManager()).setMaxHeight(height);
//                ((MaxHeightGridLayoutManager) watchListView.getLayoutManager()).setMaxHeight(height);
//            }
//        });
    }

    private CustomRecyclerView initVideoRecycleView() {
        final CustomRecyclerView view = new CustomRecyclerView(getActivity());

        MaxHeightGridLayoutManager maxHeightGridLayoutManager = new MaxHeightGridLayoutManager(getActivity(), 3, 1500);
        maxHeightGridLayoutManager.setOrientation(MaxHeightGridLayoutManager.VERTICAL);
        maxHeightGridLayoutManager.setSmoothScrollbarEnabled(true);

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        maxHeightGridLayoutManager.setMaxHeight(1800 - getResources().getDimensionPixelSize(R.dimen.index_tab_height) - getResources().getDimensionPixelSize(R.dimen.common_title_bar_height) - getResources().getDimensionPixelSize(R.dimen.common_main_tab_height) - result);
        view.setLayoutManager(maxHeightGridLayoutManager);

        return view;
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

        if (indexContent != null && indexContent.getLatestTopics() != null) {
            initLatestTopics(indexContent.getLatestTopics());
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
                                Intent intent = new Intent(getActivity(), TopicVideoPlayerActivity.class);
                                intent.putExtra(Constants.TOPIC_ID, poster.getTopicId());
                                startActivityForResult(intent, 1);
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
                for (int i = 0, count = categories.size(); i < count; i++) {
                    if (i % 8 == 0) {
                        TableLayout tableLayout = (TableLayout) inflater.inflate(R.layout.category_gridview, null);
                        TableRow upLayout = (TableRow) tableLayout.findViewById(R.id.upLayout);
                        TableRow downLayout = (TableRow) tableLayout.findViewById(R.id.downLayout);
                        gridViews.add(tableLayout);

                        for (int j = i; j < i + 8; j++) {
                            View menuView = inflater.inflate(R.layout.category_menu_item, null);
                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                            layoutParams.weight = 1;
                            layoutParams.gravity = Gravity.CENTER;
                            menuView.setLayoutParams(layoutParams);
                            CircleImageView circleImageView = ((CircleImageView) menuView.findViewById(R.id.category_circle_image_view));
                            if (j < count) {
                                circleImageView.setImageSrc(categories.get(j).getPicUrl());
                                ((TextView) menuView.findViewById(R.id.category_text_view)).setText(categories.get(j).getName());
                                circleImageView.setClickable(true);
                            }

                            if (j - i < 4) {
                                upLayout.addView(menuView);
                            } else {
                                downLayout.addView(menuView);
                            }

                            circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }
                }

                return gridViews;
            }
        });

        ((CirclePageIndicator)view.findViewById(R.id.categoryBannerIndicator)).setViewPager(categoryBanner);
    }

    public void initLatestTopics(final List<Topic> topics) {
        latestListView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ImageCacheView imageCacheView = new ImageCacheView(getActivity());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(300, 300);
                imageCacheView.setLayoutParams(layoutParams);
                //imageCacheView.setImageSrc(topics.get(0).getPhotoUrl());
                parent.addView(imageCacheView);

                return new RecyclerView.ViewHolder(imageCacheView) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ImageCacheView imageCacheView = (ImageCacheView)holder.itemView;
                imageCacheView.setImageSrc(topics.get(position % topics.size()).getPhotoUrl());
            }

            @Override
            public int getItemCount() {
                return 60;
            }
        });
    }

    @Override
    public void onStop() {
        indexPresenter.saveIndexContentCache();
        super.onStop();
    }
}
