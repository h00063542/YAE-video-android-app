package com.yilos.nailstar.aboutme.view;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import com.yilos.nailstar.aboutme.entity.FansListCategory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/5.
 */
public abstract class FansListCategoryAdapter extends BaseAdapter {
    private List<FansListCategory> categories = new ArrayList<FansListCategory>();

    public void addCategory(String title, Adapter adapter) {
        categories.add(new FansListCategory(title, adapter));
    }

    @Override
    public int getCount() {
        int total = 0;

        for (FansListCategory fansListCategory : categories) {
            total += fansListCategory.getAdapter().getCount() + 1;
        }

        return total;
    }
    @Override
    public Object getItem(int position) {
        for (FansListCategory fansListCategory : categories) {
            if (position == 0) {
                return fansListCategory;
            }

            int size = fansListCategory.getAdapter().getCount() + 1;
            if (position < size) {
                return fansListCategory.getAdapter().getItem(position-1);
            }
            position -= size;
        }

        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        int total = 1;

        for (FansListCategory fansListCategory : categories) {
            total += fansListCategory.getAdapter().getViewTypeCount();
        }

        return total;
    }
    public int getItemViewType(int position) {
        int typeOffset = 1;

        for (FansListCategory fansListCategory : categories) {
            if (position == 0) {
                return 0;
            }

            int size = fansListCategory.getAdapter().getCount() + 1;
            if (position < size) {
                return typeOffset + fansListCategory.getAdapter().getItemViewType(position - 1);
            }
            position -= size;

            typeOffset += fansListCategory.getAdapter().getViewTypeCount();
        }

        return -1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int categoryIndex = 0;

        for (FansListCategory fansListCategory : categories) {
            if (position == 0) {
                return getTitleView(fansListCategory.getTitle(), categoryIndex,convertView, parent);
            }
            int size = fansListCategory.getAdapter().getCount()+1;
            if (position < size) {
                return fansListCategory.getAdapter().getView(position - 1, convertView, parent);
            }
            position -= size;

            categoryIndex++;
        }

        return null;
    }

    protected abstract View getTitleView(String caption,int index,View convertView,ViewGroup parent);

}