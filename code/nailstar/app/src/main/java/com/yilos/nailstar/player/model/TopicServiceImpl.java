package com.yilos.nailstar.player.model;

import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.TopicCommentAtInfo;
import com.yilos.nailstar.player.entity.TopicCommentInfo;
import com.yilos.nailstar.player.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.player.entity.TopicImageTextInfo;
import com.yilos.nailstar.player.entity.TopicInfo;
import com.yilos.nailstar.player.entity.TopicRelatedInfo;
import com.yilos.nailstar.player.entity.VideoInfo;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.JsonUtil;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.StringUtil;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-24.
 */
public class TopicServiceImpl implements ITopicService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Override
    public TopicInfo getTopicInfo(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        TopicInfo topicInfo = null;

        String url = "/vapi/nailstar/topics/" + topicId;
        try {
            String strVideoInfo = HttpClient.getJson(url);
            if (StringUtil.isEmpty(strVideoInfo)) {
                return topicInfo;
            }
            topicInfo = new TopicInfo();

            JSONObject jsonObject = new JSONObject(strVideoInfo);
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            ArrayList tags = new ArrayList();
            JSONArray jsonTags = jsonResult.optJSONArray(Constants.TAGS);
            for (int i = 0; i < jsonTags.length(); i++) {
                tags.add(JsonUtil.optString(jsonTags, i));
            }

            ArrayList videos = new ArrayList();
            JSONArray jsonVideos = jsonResult.optJSONArray(Constants.VIDEOS);
            for (int i = 0; i < jsonVideos.length(); i++) {
                JSONObject jsonVideoObj = jsonVideos.optJSONObject(i);
                videos.add(new VideoInfo(JsonUtil.optString(jsonVideoObj, Constants.VIDEO_ID), jsonVideoObj.optInt("playTimes", 0), JsonUtil.optString(jsonVideoObj, Constants.CC_URL), JsonUtil.optString(jsonVideoObj, Constants.OSS_URL)));
            }

            topicInfo.setId(JsonUtil.optString(jsonResult, Constants.ID));
            topicInfo.setTitle(JsonUtil.optString(jsonResult, Constants.TITLE));
            topicInfo.setType(JsonUtil.optString(jsonResult, Constants.TYPE));
            topicInfo.setAuthorPhoto(JsonUtil.optString(jsonResult, Constants.AUTHOR_PHOTO));
            topicInfo.setThumbUrl(JsonUtil.optString(jsonResult, Constants.THUMB_URL));
            topicInfo.setCreateDate(jsonResult.optLong(Constants.CREATE_DATE, 0));
            topicInfo.setTags(tags);
            topicInfo.setVideos(videos);
            topicInfo.setAuthorId(JsonUtil.optString(jsonResult, Constants.AUTHOR_ID));
            topicInfo.setAuthor(JsonUtil.optString(jsonResult, Constants.AUTHOR));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic信息失败，topicId:{0}，url:{1}", topicId, url), e);
        } catch (JSONException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic信息失败，topicId:{0}，url:{1}", topicId, url), e);
        }


        return topicInfo;
    }

    @Override
    public TopicImageTextInfo getTopicImageTextInfo(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        TopicImageTextInfo topicImageTextInfo = null;

        String url = "/vapi/nailstar/topics/article/" + topicId;
        try {
            String strVideoImageTextInfo = HttpClient.getJson(url);
            if (StringUtil.isEmpty(strVideoImageTextInfo)) {
                return topicImageTextInfo;
            }

            topicImageTextInfo = new TopicImageTextInfo();
            JSONObject jsonObject = new JSONObject(strVideoImageTextInfo);
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            ArrayList pictures = new ArrayList();
            JSONArray jsonPictures = jsonResult.optJSONArray(Constants.PICTURES);
            for (int i = 0; i < jsonPictures.length(); i++) {
                pictures.add(JsonUtil.optString(jsonPictures, i));
            }

            ArrayList articles = new ArrayList();
            JSONArray jsonArticles = jsonResult.optJSONArray(Constants.ARTICLES);
            for (int i = 0; i < jsonArticles.length(); i++) {
                articles.add(JsonUtil.optString(jsonArticles, i));
            }

            topicImageTextInfo.setId(JsonUtil.optString(jsonResult, Constants.ID));
            topicImageTextInfo.setArticles(articles);
            topicImageTextInfo.setPictures(pictures);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic图文信息失败，topicId:{0}，url:{1}", topicId, url), e);
        } catch (JSONException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic图文信息失败，topicId:{0}，url:{1}", topicId, url), e);
        }


        return topicImageTextInfo;
    }

    @Override
    public ArrayList<TopicRelatedInfo> getTopicRelatedInfoList(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        ArrayList<TopicRelatedInfo> result = new ArrayList<TopicRelatedInfo>();

        String url = "/vapi/nailstar/topics/" + topicId + "/related";
        try {
            String strTopicsRelatedInfo = HttpClient.getJson(url);
            if (StringUtil.isEmpty(strTopicsRelatedInfo)) {
                return result;
            }
            JSONObject jsonObject = new JSONObject(strTopicsRelatedInfo);
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            JSONArray jsonRelated = jsonResult.optJSONArray(Constants.RELATED);
            for (int i = 0; i < jsonRelated.length(); i++) {
                result.add(new TopicRelatedInfo(JsonUtil.optString(jsonRelated.optJSONObject(i), Constants.TOPIC_ID)
                        , JsonUtil.optString(jsonRelated.optJSONObject(i), Constants.THUMB_URL)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic关联信息失败，topicId:{0}，url:{1}", topicId, url), e);
        } catch (JSONException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic关联信息失败，topicId:{0}，url:{1}", topicId, url), e);
        }

        return result;
    }

    @Override
    public ArrayList<TopicCommentInfo> getTopicComments(String topicId, int page) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        ArrayList<TopicCommentInfo> result = new ArrayList<TopicCommentInfo>();

        String url = "/vapi/nailstar/topics/" + topicId + "/comments?page=" + page;
        try {
            String strTopicCommentsInfo = HttpClient.getJson(url);
            if (StringUtil.isEmpty(strTopicCommentsInfo)) {
                return result;
            }
            JSONObject jsonObject = new JSONObject(strTopicCommentsInfo);
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);
            JSONArray jsonComments = jsonResult.optJSONArray(Constants.COMMENTS);


            for (int i = 0; i < jsonComments.length(); i++) {
                JSONObject jsonComment = jsonComments.optJSONObject(i);
                TopicCommentInfo topicCommentInfo = new TopicCommentInfo();

                topicCommentInfo.setId(JsonUtil.optString(jsonComment, Constants.ID));
                topicCommentInfo.setUserId(JsonUtil.optString(jsonComment, Constants.USER_ID_LOWER));
                topicCommentInfo.setAuthor(JsonUtil.optString(jsonComment, Constants.AUTHOR));
                topicCommentInfo.setAuthorPhoto(JsonUtil.optString(jsonComment, Constants.AUTHOR_PHOTO));
                topicCommentInfo.setContent(JsonUtil.optString(jsonComment, Constants.CONTENT));
                topicCommentInfo.setContentPic(JsonUtil.optString(jsonComment, Constants.CONTENT_PIC));

                topicCommentInfo.setCreateDate(jsonComment.optLong(Constants.CREATE_DATE, 0));
                topicCommentInfo.setIsHomework(jsonComment.optInt(Constants.IS_HOME_WORK, 0));
                topicCommentInfo.setIsMine(jsonComment.optInt(Constants.IS_MINE, 0));
                topicCommentInfo.setStatus(jsonComment.optInt(Constants.STATUS, 0));

                JSONObject jsonAt = jsonComment.optJSONObject(Constants.AT);
                if (null != jsonAt) {
                    TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                    topicCommentAtInfo.setUserId(JsonUtil.optString(jsonAt, Constants.USER_ID));
                    topicCommentAtInfo.setNickName(JsonUtil.optString(jsonAt, Constants.NICKNAME));
                    topicCommentInfo.setAt(topicCommentAtInfo);
                }
                topicCommentInfo.setReplies(buildTopicCommentReplies(jsonComment.optJSONArray(Constants.REPLIES)));

                result.add(topicCommentInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic评论信息失败，topicId:{0}，url:{1}", topicId, url), e);
        } catch (JSONException e) {
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("获取topic评论信息失败，topicId:{0}，url:{1}", topicId, url), e);
        }
        return result;
    }

    private ArrayList<TopicCommentReplyInfo> buildTopicCommentReplies(JSONArray jsonReplies) {
        ArrayList<TopicCommentReplyInfo> topicCommentReplies = new ArrayList<TopicCommentReplyInfo>();
        if (null == jsonReplies) {
            return topicCommentReplies;
        }
        for (int i = 0; i < jsonReplies.length(); i++) {
            JSONObject jsonReply = jsonReplies.optJSONObject(i);
            TopicCommentReplyInfo topicCommentReplyInfo = new TopicCommentReplyInfo();
            topicCommentReplyInfo.setId(JsonUtil.optString(jsonReply, Constants.ID));
            topicCommentReplyInfo.setUserId(JsonUtil.optString(jsonReply, Constants.USER_ID_LOWER));
            topicCommentReplyInfo.setAuthor(JsonUtil.optString(jsonReply, Constants.AUTHOR));
            topicCommentReplyInfo.setContent(JsonUtil.optString(jsonReply, Constants.CONTENT));
            topicCommentReplyInfo.setContentPic(JsonUtil.optString(jsonReply, Constants.CONTENT_PIC));
            topicCommentReplyInfo.setCreateDate(jsonReply.optLong(Constants.CREATE_DATE, 0));
            topicCommentReplyInfo.setIsHomework(jsonReply.optInt(Constants.IS_HOME_WORK, 0));
            topicCommentReplyInfo.setStatus(jsonReply.optInt(Constants.STATUS, 0));
            JSONObject jsonAt = jsonReply.optJSONObject(Constants.AT);
            TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
            topicCommentAtInfo.setUserId(JsonUtil.optString(jsonReply, Constants.USER_ID));
            topicCommentAtInfo.setNickName(JsonUtil.optString(jsonReply, Constants.NICKNAME));
            topicCommentReplyInfo.setAt(topicCommentAtInfo);

            topicCommentReplies.add(topicCommentReplyInfo);
        }
        return topicCommentReplies;
    }
}
