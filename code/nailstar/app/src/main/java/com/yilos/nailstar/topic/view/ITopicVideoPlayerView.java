package com.yilos.nailstar.topic.view;

import android.graphics.Bitmap;

import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.entity.TopicStatusInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedProduct;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-22.
 */
public interface ITopicVideoPlayerView {

    /**
     * 初始化topic视频
     *
     * @param topicInfo
     */
    void initTopicInfo(TopicInfo topicInfo);

    /**
     * 初始化topic图文分解信息
     *
     * @param topicImageTextInfo
     */
    void initTopicImageTextInfo(TopicImageTextInfo topicImageTextInfo);


    /**
     * 初始化topic评论信息
     *
     * @param topicComments
     */
    void initTopicCommentInfo(ArrayList<TopicCommentInfo> topicComments, int orderBy);

    /**
     * 初始化topic关联的topic信息
     *
     * @param topicRelatedList
     */
    void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelatedList);

    /**
     * 初始化topic关联的产品信息
     *
     * @param topicRelatedProductList
     */
    void initTopicRelatedUsedProductList(ArrayList<TopicRelatedProduct> topicRelatedProductList);

    void initUserTopicStatus(TopicStatusInfo topicStatusInfo);

    void setTopicLikeStatus(boolean isLike, boolean isSuccess);

    void setTopicCollectionStatus(boolean isCollection, boolean isSuccess);

    void setDownloadTopicImageStatus(boolean isSuccess, String filePath);

    void setDownloadTopicImageTextStatus(boolean isSuccess, String filePath);

    void setVideoThumbnail(Bitmap bitmap);
}