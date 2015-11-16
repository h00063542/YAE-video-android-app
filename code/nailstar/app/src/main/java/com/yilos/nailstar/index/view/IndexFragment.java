package com.yilos.nailstar.index.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.yilos.nailstar.framework.application.NailStarApplication;
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
import com.yilos.widget.pageindicator.UnderlinePageIndicator;
import com.yilos.widget.pullrefresh.PullRefreshLayout;
import com.yilos.widget.titlebar.TitleBar;
import com.yilos.widget.view.ImageCacheView;
import com.yilos.widget.view.MViewPager;

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
    private LayoutInflater inflater;
    private View view;
    // 轮播图
    private Banner posterBanner;
    // 分类菜单
    private Banner categoryBanner;
    // 视频列表Pager
    private MViewPager videoListPager;
    // 视频列表适配器
    private VideoListAdapter videoListAdapter;

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
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_index, container, false);
        initViews();

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

    private void initViews() {
        posterBanner = (Banner) view.findViewById(R.id.posterBanner);
        categoryBanner = (Banner) view.findViewById(R.id.categoryBanner);
        videoListPager = (MViewPager)view.findViewById(R.id.videoListPager);
        videoListAdapter = new VideoListAdapter(getActivity());
        videoListPager.setAdapter(videoListAdapter);

        //title
        TitleBar titleBar = (TitleBar)view.findViewById(R.id.titleBar);
        ImageView logoImage = titleBar.getImageTitleView();
        logoImage.setImageResource(R.mipmap.daka_word_logo);
        ImageView searchButton = titleBar.getRightImageButtonOne();
        searchButton.setImageResource(R.mipmap.ic_search);

        // 初始化tab页
        final TabPageIndicator tabPageIndicator = (TabPageIndicator)view.findViewById(R.id.videoListPagerIndicator);
        tabPageIndicator.setViewPager(videoListPager);
        final UnderlinePageIndicator underlinePageIndicator = (UnderlinePageIndicator)view.findViewById(R.id.videoListPagerLineIndicator);
        underlinePageIndicator.setViewPager(videoListPager);

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

        if (indexContent != null && indexContent.getLatestTopics() != null) {
            initLatestTopics(indexContent.getLatestTopics());
        }
    }

    @Override
    public void initPosters(final List<Poster> posters) {
        final int height = NailStarApplication.getApplication().getHeightByScreenWidth(getActivity(), 7f/3f);
        posterBanner.setViewCreator(new Banner.ViewCreator() {
            @Override
            public List<View> createViews() {
                final List<View> views = new ArrayList<>(8);

                if (!CollectionUtil.isEmpty(posters)) {
                    for (final Poster poster : posters) {
                        ImageCacheView imageCacheView = new ImageCacheView(getActivity());

                        Banner.LayoutParams layoutParams = new Banner.LayoutParams();
                        layoutParams.width = Banner.LayoutParams.MATCH_PARENT;
                        layoutParams.height = height;
                        imageCacheView.setLayoutParams(layoutParams);
                        imageCacheView.setAdjustViewBounds(true);
                        imageCacheView.setScaleType(ImageView.ScaleType.FIT_CENTER);

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
        videoListAdapter.getLatestListView().setAdapter(new RecyclerView.Adapter() {
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
                ImageCacheView imageCacheView = (ImageCacheView) holder.itemView;
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
