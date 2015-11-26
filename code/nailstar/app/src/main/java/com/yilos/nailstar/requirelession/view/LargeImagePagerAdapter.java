package com.yilos.nailstar.requirelession.view;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.widget.view.ImageCacheView;

import java.util.List;

/**
 * Created by yilos on 15/11/26.
 */
public class LargeImagePagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;

    private List<CandidateLession> candidateList;

    public LargeImagePagerAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setCandidateList(List<CandidateLession> candidateList) {
        this.candidateList = candidateList;
    }

    @Override
    public int getItemPosition(Object object) {
        // 每次点击进来都刷新数据，防止下拉刷新后显示大图用的是缓存中的旧图片
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (candidateList != null) {
            count = candidateList.size();
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (position >= getCount()) {
            return null;
        }

        CandidateLession candidateLession = candidateList.get(position);

        View lessionImageItem = layoutInflater.inflate(R.layout.lession_image_item, null);
        ImageCacheView lessionLargeImage = (ImageCacheView) lessionImageItem.findViewById(R.id.lessionLargeImage);
        if (candidateLession.getPicUrl() != null && !"".equals(candidateLession.getPicUrl())) {
            lessionLargeImage.setImageSrc(candidateLession.getPicUrl());
        } else {
            lessionLargeImage.setImageURI(null);
        }
        lessionLargeImage.setBackgroundColor(0x00000000);
        container.addView(lessionImageItem);

        return lessionImageItem;
    }
}
