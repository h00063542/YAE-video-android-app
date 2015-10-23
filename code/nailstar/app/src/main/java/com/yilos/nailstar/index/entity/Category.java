package com.yilos.nailstar.index.entity;

import java.io.Serializable;

/**
 * Created by yangdan on 15/10/16.
 * 主题分类
 */
public class Category implements Serializable {
    /**
     * ID
     */
    private String id;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 分类名称
     */
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!id.equals(category.id)) return false;
        if (picUrl != null ? !picUrl.equals(category.picUrl) : category.picUrl != null)
            return false;
        return !(name != null ? !name.equals(category.name) : category.name != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (picUrl != null ? picUrl.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
