package com.yilos.nailstar.mall.entity;

/**
 * Created by ganyue on 15/11/21.
 */
public class CommodityCategory {
    public CommodityCategory( String name,String id) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String id;
    private String name;
}
