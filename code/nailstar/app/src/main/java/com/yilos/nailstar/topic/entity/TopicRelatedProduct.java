package com.yilos.nailstar.topic.entity;

/**
 * Created by ganyue on 15/11/17.
 * 主题关联的产品信息
 */
public class TopicRelatedProduct {
    private String productName;
    private String desc;
    private String price;
    private String real_id;

    public TopicRelatedProduct( String productName, String desc, String price, String real_id) {
        this.productName = productName;
        this.desc = desc;
        this.price = price;
        this.real_id = real_id;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getReal_id() {
        return real_id;
    }

    public void setReal_id(String real_id) {
        this.real_id = real_id;
    }
}
