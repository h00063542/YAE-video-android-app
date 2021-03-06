package com.yilos.nailstar.mall.entity;

/**
 * Created by ganyue on 15/11/20.
 */
public class Commodity {
    private String name;
    private long price;
    private String goodsRealId;
    private String desc;
    private String advdesc;
    private String imageUrl;

    public Commodity() {
    }
    public Commodity(String name, long price, String advdesc,String imageUrl,String goodsRealId) {
        this.name = name;
        this.price = price;
        this.advdesc = advdesc;
        this.imageUrl = imageUrl;
        this.goodsRealId = goodsRealId;

    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setGoodsRealId(String goodsRealId) {
        this.goodsRealId = goodsRealId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setAdvdesc(String advdesc) {
        this.advdesc = advdesc;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {

        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getGoodsRealId() {
        return goodsRealId;
    }

    public String getDesc() {
        return desc;
    }

    public String getAdvdesc() {
        return advdesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }



}


