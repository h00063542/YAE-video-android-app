package com.yilos.nailstar.index.view;

import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.Poster;

import java.util.List;

/**
 * Created by yangdan on 15/10/23.
 */
public interface IIndexView {
    /**
     * 初始化轮播图
     * @param posters
     */
    void initPosters(List<Poster> posters);

    /**
     * 初始化分类菜单
     * @param categories
     */
    void initCategoriesMenu(List<Category> categories);
}
