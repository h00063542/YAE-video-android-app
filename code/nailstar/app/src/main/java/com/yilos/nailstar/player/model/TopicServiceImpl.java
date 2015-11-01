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
                tags.add(jsonTags.optString(i, null));
            }

            ArrayList videos = new ArrayList();
            JSONArray jsonVideos = jsonResult.optJSONArray(Constants.VIDEOS);
            for (int i = 0; i < jsonVideos.length(); i++) {
                JSONObject jsonVideoObj = jsonVideos.optJSONObject(i);
                videos.add(new VideoInfo(jsonVideoObj.optString(Constants.VIDEO_ID, null), jsonVideoObj.optInt("playTimes", 0), jsonVideoObj.optString("ccUrl", null), jsonVideoObj.optString("ossUrl", null)));
            }

            topicInfo.setId(jsonResult.optString(Constants.ID, null));
            topicInfo.setTitle(jsonResult.optString(Constants.TITLE, null));
            topicInfo.setType(jsonResult.optString(Constants.TYPE, null));
            topicInfo.setAuthorPhoto(jsonResult.optString(Constants.AUTHOR_PHOTO, null));
            topicInfo.setThumbUrl(jsonResult.optString(Constants.THUMB_URL, null));
            topicInfo.setCreateDate(jsonResult.optLong(Constants.CREATE_DATE, 0));
            topicInfo.setTags(tags);
            topicInfo.setVideos(videos);
            topicInfo.setAuthorId(jsonResult.optString(Constants.AUTHOR_ID, null));
            topicInfo.setAuthor(jsonResult.optString(Constants.AUTHOR, null));
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
                pictures.add(jsonPictures.optString(i, null));
            }

            ArrayList articles = new ArrayList();
            JSONArray jsonArticles = jsonResult.optJSONArray(Constants.ARTICLES);
            for (int i = 0; i < jsonArticles.length(); i++) {
                articles.add(jsonArticles.optString(i, null));
            }

            topicImageTextInfo.setId(jsonResult.optString(Constants.ID, null));
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
                result.add(new TopicRelatedInfo(jsonRelated.optJSONObject(i).optString(Constants.TOPIC_ID, null)
                        , jsonRelated.optJSONObject(i).optString(Constants.THUMB_URL, null)));
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

                topicCommentInfo.setId(jsonComment.optString(Constants.ID, null));
                topicCommentInfo.setUserId(jsonComment.optString(Constants.USER_ID_LOWER, null));
                topicCommentInfo.setAuthor(jsonComment.optString(Constants.AUTHOR, null));
                topicCommentInfo.setAuthorPhoto(jsonComment.optString(Constants.AUTHOR_PHOTO, null));
                topicCommentInfo.setContent(jsonComment.optString(Constants.CONTENT));
                topicCommentInfo.setContentPic(jsonComment.optString(Constants.CONTENT_PIC, null));

                topicCommentInfo.setCreateDate(jsonComment.optLong(Constants.CREATE_DATE, 0));
                topicCommentInfo.setIsHomework(jsonComment.optInt(Constants.IS_HOME_WORK, 0));
                topicCommentInfo.setIsMine(jsonComment.optInt(Constants.IS_MINE, 0));
                topicCommentInfo.setStatus(jsonComment.optInt(Constants.STATUS, 0));

                JSONObject jsonAt = jsonComment.optJSONObject(Constants.AT);
                if (null != jsonAt) {
                    TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                    topicCommentAtInfo.setUserId(jsonAt.optString(Constants.USER_ID, null));
                    topicCommentAtInfo.setNickName(jsonAt.optString(Constants.NICKNAME, null));
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
            topicCommentReplyInfo.setId(jsonReply.optString(Constants.ID, null));
            topicCommentReplyInfo.setUserId(jsonReply.optString(Constants.USER_ID_LOWER, null));
            topicCommentReplyInfo.setAuthor(jsonReply.optString(Constants.AUTHOR, null));
            topicCommentReplyInfo.setContent(jsonReply.optString(Constants.CONTENT, null));
            topicCommentReplyInfo.setContentPic(jsonReply.optString(Constants.CONTENT_PIC, null));
            topicCommentReplyInfo.setCreateDate(jsonReply.optLong(Constants.CREATE_DATE, 0));
            topicCommentReplyInfo.setIsHomework(jsonReply.optInt(Constants.IS_HOME_WORK, 0));
            topicCommentReplyInfo.setStatus(jsonReply.optInt(Constants.STATUS, 0));
            JSONObject jsonAt = jsonReply.optJSONObject(Constants.AT);
            TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
            topicCommentAtInfo.setUserId(jsonAt.optString(Constants.USER_ID, null));
            topicCommentAtInfo.setNickName(jsonAt.optString(Constants.NICKNAME, null));
            topicCommentReplyInfo.setAt(topicCommentAtInfo);

            topicCommentReplies.add(topicCommentReplyInfo);
        }
        return topicCommentReplies;
    }
}
