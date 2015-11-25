package com.yilos.nailstar.index.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.framework.exception.NoWatchException;
import com.yilos.nailstar.framework.exception.NotLoginException;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.index.presenter.IndexPresenter;
import com.yilos.widget.banner.Banner;
import com.yilos.widget.pageindicator.CirclePageIndicator;
import com.yilos.widget.pageindicator.TabPageIndicator;
import com.yilos.widget.pageindicator.UnderlinePageIndicator;
import com.yilos.widget.pullrefresh.PullRefreshLayout;
import com.yilos.widget.titlebar.TitleBar;
import com.yilos.widget.view.MViewPager;

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
    private VideoVeiwPagerAdapter videoVeiwPagerAdapter;
    // 下拉刷新控件
    private PullRefreshLayout pullRefreshLayout;

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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseActivity)getActivity()).hideLoading();
        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        posterBanner.play();
        ((BaseActivity)getActivity()).hideLoading();
        indexPresenter.refreshCategory();
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        posterBanner.stop();
    }

    private void initViews() {
        posterBanner = (Banner) view.findViewById(R.id.posterBanner);
        categoryBanner = (Banner) view.findViewById(R.id.categoryBanner);
        videoListPager = (MViewPager)view.findViewById(R.id.videoListPager);

        videoVeiwPagerAdapter = new VideoVeiwPagerAdapter(getActivity());
        videoListPager.setAdapter(videoVeiwPagerAdapter);

        //title
        TitleBar titleBar = (TitleBar)view.findViewById(R.id.titleBar);
        ImageView logoImage = titleBar.getImageTitleView();
        logoImage.setImageResource(R.mipmap.daka_word_logo);
        ImageView searchButton = titleBar.getRightImageButtonOne();
        searchButton.setImageResource(R.mipmap.ic_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in_center, R.anim.fade_out);
            }
        });

        // 初始化tab页
        final TabPageIndicator tabPageIndicator = (TabPageIndicator)view.findViewById(R.id.videoListPagerIndicator);
        tabPageIndicator.setViewPager(videoListPager);
        final UnderlinePageIndicator underlinePageIndicator = (UnderlinePageIndicator)view.findViewById(R.id.videoListPagerLineIndicator);
        underlinePageIndicator.setViewPager(videoListPager);

        // 初始化下拉刷新功能
        pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.fragment_home_ptr_frame);
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
                        indexPresenter.refreshIndexContent(true);
                    }
                }, 50);
            }
        });
    }

    private void refreshData() {
        indexPresenter.refreshIndexContent(false);
    }

    @Override
    public void finishRefresh() {
        pullRefreshLayout.refreshComplete();
    }

    @Override
    public void initPosters(final List<Poster> posters) {
        if(null == posters) {
            return;
        }

        posterBanner.setViewCreator(new PosterViewCreator((BaseActivity)getActivity(), posters));
        ((CirclePageIndicator)view.findViewById(R.id.indicator)).setViewPager(posterBanner);
        posterBanner.play();
    }

    @Override
    public void initCategoriesMenu(final List<Category> categories) {
        if(null == categories) {
            return;
        }
        categoryBanner.setViewCreator(new CategoriesMenuViewCreator((BaseActivity) getActivity(), categories));
        if(categories.size() <= 8) {
            view.findViewById(R.id.categoryBannerIndicator).setVisibility(View.GONE);
        }
        ((CirclePageIndicator)view.findViewById(R.id.categoryBannerIndicator)).setViewPager(categoryBanner);
    }

    @Override
    public void initLatestTopics(final List<Topic> topics) {
        VideoListAdapter adapter = new VideoListAdapter(getActivity(), topics, new VideoListAdapter.OnLoadPagedDataListener() {
            @Override
            public List<Topic> loadPagedData(int page) throws CommonException{
                try {
                    return indexPresenter.getLatestTopicByPage(page);
                } catch (NetworkDisconnectException e) {
                    throw new CommonException("分页获取最新视频失败", e);
                } catch (JSONParseException e) {
                    throw new CommonException("分页获取最新视频失败", e);
                }
            }
        });
        videoVeiwPagerAdapter.getLatestListView().setAdapter(adapter);
    }

    @Override
    public void initHotestTopics(List<Topic> topics) {
        VideoListAdapter adapter = new VideoListAdapter(getActivity(), topics, new VideoListAdapter.OnLoadPagedDataListener() {
            @Override
            public List<Topic> loadPagedData(int page) throws CommonException{
                try {
                    return indexPresenter.getHotestTopicByPage(page);
                } catch (NetworkDisconnectException e) {
                    throw new CommonException("分页获取最热视频失败", e);
                } catch (JSONParseException e) {
                    throw new CommonException("分页获取最热视频失败", e);
                }
            }
        });
        videoVeiwPagerAdapter.getHotestListView().setAdapter(adapter);
    }

    @Override
    public void initWatchTopics(List<Topic> topics) {
        VideoListAdapter adapter = new VideoListAdapter(getActivity(), topics, new VideoListAdapter.OnLoadPagedDataListener() {
            @Override
            public List<Topic> loadPagedData(int page) throws CommonException {
                try {
                    List<Topic> watchList = indexPresenter.getWatchTopicByPage(page);
                    return watchList;
                } catch (NetworkDisconnectException e) {
                    throw new CommonException("分页获取关注视频失败", e);
                } catch (JSONParseException e) {
                    throw new CommonException("分页获取关注视频失败", e);
                } catch(NotLoginException e) {
                    showNotLoginView();
                } catch(NoWatchException e) {
                    showNotWatchView();
                }

                return null;
            }
        });
        videoVeiwPagerAdapter.getWatchListView().setAdapter(adapter);
    }

    @Override
    public void showNotLoginView() {
        videoVeiwPagerAdapter.showNotLoginView();
    }

    @Override
    public void showNotWatchView() {
        videoVeiwPagerAdapter.showNotWatchView();
    }

    @Override
    public void onStop() {
        indexPresenter.saveIndexContentCache();
        super.onStop();
    }
}
