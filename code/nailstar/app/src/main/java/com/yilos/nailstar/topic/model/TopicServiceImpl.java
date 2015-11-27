package com.yilos.nailstar.topic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.AddCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentAtInfo;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedProduct;
import com.yilos.nailstar.topic.entity.TopicStatusInfo;
import com.yilos.nailstar.topic.entity.TopicVideoInfo;
import com.yilos.nailstar.topic.entity.UpdateReadyInfo;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.JsonUtil;
import com.yilos.nailstar.util.OSSUtil;
import com.yilos.nailstar.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-24.
 */
public class TopicServiceImpl implements ITopicService {
    private final String URL_PREFIX = "/vapi/nailstar/";


    /**
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public TopicInfo getTopicInfo(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            String strResult = getLocalJsonResult(topicId, Constants.FILE_NAME_TOPIC_INFO);
            return buildTopicInfo(topicId, strResult);
        }
        String url = URL_PREFIX + "topics/" + topicId;
        try {
            String strResult = HttpClient.getJson(url);
            TopicInfo topicInfo = buildTopicInfo(topicId, strResult);
            if (null != topicInfo) {
                writeLocalJsonResult(topicId, strResult, Constants.FILE_NAME_TOPIC_INFO);
            }
            return topicInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private TopicInfo buildTopicInfo(String topicId, String strResult) {
        TopicInfo topicInfo = null;
        try {
            JSONObject jsonObject = buildJSONObject(strResult);
            if (null == jsonObject) {
                return topicInfo;
            }
            topicInfo = new TopicInfo();

            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            ArrayList tags = new ArrayList();
            JSONArray jsonTags = jsonResult.optJSONArray(Constants.TAGS);
            if (null != jsonTags) {
                for (int i = 0; i < jsonTags.length(); i++) {
                    tags.add(JsonUtil.optString(jsonTags, i));
                }
            }

            ArrayList videos = new ArrayList();
            JSONArray jsonVideos = jsonResult.optJSONArray(Constants.VIDEOS);
            if (null != jsonVideos) {
                for (int i = 0; i < jsonVideos.length(); i++) {
                    JSONObject jsonVideoObj = jsonVideos.optJSONObject(i);
                    videos.add(new TopicVideoInfo(JsonUtil.optString(jsonVideoObj, Constants.VIDEO_ID)
                            , jsonVideoObj.optInt(Constants.PLAY_TIMES, 0)
                            , JsonUtil.optString(jsonVideoObj, Constants.CC_URL)
                            , JsonUtil.optString(jsonVideoObj, Constants.OSS_URL)));
                }
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
            topicInfo.setCommentCount(jsonResult.optInt(Constants.COMMENT_COUNT, 0));
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("获取topic信息失败，topicId:{0}，strResult:{1}", topicId, strResult), e);
        }
        return topicInfo;
    }

    /**
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public TopicImageTextInfo getTopicImageTextInfo(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            String strResult = getLocalJsonResult(topicId, Constants.FILE_NAME_TOPIC_IMAGE_TEXT_INFO);
            return buildTopicImageTextInfo(topicId, strResult);
        }
        try {
            String url = URL_PREFIX + "topics/article/" + topicId;
            String strResult = HttpClient.getJson(url);
            TopicImageTextInfo topicImageTextInfo = buildTopicImageTextInfo(topicId, strResult);
            if (null != topicImageTextInfo) {
                writeLocalJsonResult(topicId, strResult, Constants.FILE_NAME_TOPIC_IMAGE_TEXT_INFO);
            }
            return topicImageTextInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private TopicImageTextInfo buildTopicImageTextInfo(String topicId, String strResult) {
        TopicImageTextInfo topicImageTextInfo = null;
        try {
            JSONObject jsonObject = buildJSONObject(strResult);
            if (null == jsonObject) {
                return topicImageTextInfo;
            }
            topicImageTextInfo = new TopicImageTextInfo();
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            ArrayList<String> pictures = new ArrayList<String>();
            JSONArray jsonPictures = jsonResult.optJSONArray(Constants.PICTURES);
            if (null != jsonPictures) {
                for (int i = 0; i < jsonPictures.length(); i++) {
                    pictures.add(JsonUtil.optString(jsonPictures, i));
                }
            }

            ArrayList<String> articles = new ArrayList<String>();
            JSONArray jsonArticles = jsonResult.optJSONArray(Constants.ARTICLES);
            if (null != jsonArticles) {
                for (int i = 0; i < jsonArticles.length(); i++) {
                    articles.add(JsonUtil.optString(jsonArticles, i));
                }
            }

            topicImageTextInfo.setId(JsonUtil.optString(jsonResult, Constants.ID));
            topicImageTextInfo.setArticles(articles);
            topicImageTextInfo.setPictures(pictures);
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("获取topic图文信息失败，topicId:{0}，strResult:{1}", topicId, strResult), e);
        }
        return topicImageTextInfo;
    }

    /**
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public ArrayList<TopicRelatedInfo> getTopicRelatedInfoList(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            String strResult = getLocalJsonResult(topicId, Constants.FILE_NAME_TOPIC_RELATE_INFO);
            return buildTopicRelatedInfo(topicId, strResult);
        }
        String url = URL_PREFIX + "topics/" + topicId + "/related";
        try {
            String strResult = HttpClient.getJson(url);
            ArrayList<TopicRelatedInfo> result = buildTopicRelatedInfo(topicId, strResult);
            if (!CollectionUtil.isEmpty(result)) {
                writeLocalJsonResult(topicId, strResult, Constants.FILE_NAME_TOPIC_RELATE_INFO);
            }
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<TopicRelatedInfo>();
    }

    @NonNull
    private ArrayList<TopicRelatedInfo> buildTopicRelatedInfo(String topicId, String strResult) {
        ArrayList<TopicRelatedInfo> result = new ArrayList<TopicRelatedInfo>();

        try {
            JSONObject jsonObject = buildJSONObject(strResult);
            if (null == jsonObject) {
                return result;
            }
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            JSONArray jsonRelated = jsonResult.optJSONArray(Constants.RELATED);
            for (int i = 0; i < jsonRelated.length(); i++) {
                result.add(new TopicRelatedInfo(JsonUtil.optString(jsonRelated.optJSONObject(i), Constants.TOPIC_ID)
                        , JsonUtil.optString(jsonRelated.optJSONObject(i), Constants.THUMB_URL)));
            }
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("获取topic关联信息失败，topicId:{0}，strResult:{1}", topicId, strResult), e);
        }

        return result;
    }


    public ArrayList<TopicRelatedProduct> getTopicRelatedUsedProductList(String topicId) throws NetworkDisconnectException {

        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            String strResult = getLocalJsonResult(topicId, Constants.FILE_NAME_TOPIC_RELATE_USED_PRODUCT_INFO);
            return buildTopicRelatedUsedProduct(topicId, strResult);
        }
        String url = URL_PREFIX + "topics/" + topicId + "/commodities";
        try {
            String strResult = HttpClient.getJson(url);
            ArrayList<TopicRelatedProduct> result = buildTopicRelatedUsedProduct(topicId, strResult);
            if (!CollectionUtil.isEmpty(result)) {
                writeLocalJsonResult(topicId, strResult, Constants.FILE_NAME_TOPIC_RELATE_USED_PRODUCT_INFO);
            }
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<TopicRelatedProduct>();

    }

    @NonNull
    private ArrayList<TopicRelatedProduct> buildTopicRelatedUsedProduct(String topicId, String strResult) {
        ArrayList<TopicRelatedProduct> result = new ArrayList<TopicRelatedProduct>();

        try {
            JSONObject jsonObject = buildJSONObject(strResult);
            if (null == jsonObject) {
                return result;
            }
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            JSONArray jsonRelated = jsonResult.optJSONArray(Constants.COMMODITY);
            for (int i = 0; i < jsonRelated.length(); i++) {
                result.add(new TopicRelatedProduct(JsonUtil.optString(jsonRelated.optJSONObject(i), "name")
                        , JsonUtil.optString(jsonRelated.optJSONObject(i), "description")
                        , JsonUtil.optString(jsonRelated.optJSONObject(i), "price")
                        , JsonUtil.optString(jsonRelated.optJSONObject(i), "real_id")));
            }
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("获取topic关联信息失败，topicId:{0}，strResult:{1}", topicId, strResult), e);
        }

        return result;
    }


    /**
     * @param topicId
     * @param page
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public ArrayList<TopicCommentInfo> getTopicComments(String topicId, int page) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            if (page != 1) {
                return new ArrayList<TopicCommentInfo>();
            }
            String strResult = getLocalJsonResult(topicId, Constants.FILE_NAME_TOPIC_COMMENT_INFO);
            return buildTopicCommentInfo(topicId, strResult);
        }
        String url = URL_PREFIX + "topics/" + topicId + "/comments?page=" + page;
        try {
            String strResult = HttpClient.getJson(url);
            ArrayList<TopicCommentInfo> result = buildTopicCommentInfo(topicId, strResult);
            if (page == 1 && !CollectionUtil.isEmpty(result)) {
                writeLocalJsonResult(topicId, strResult, Constants.FILE_NAME_TOPIC_COMMENT_INFO);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<TopicCommentInfo>();
    }

    @NonNull
    private ArrayList<TopicCommentInfo> buildTopicCommentInfo(String topicId, String strResult) {
        ArrayList<TopicCommentInfo> result = new ArrayList<TopicCommentInfo>();
        try {
            JSONObject jsonObject = buildJSONObject(strResult);
            if (null == jsonObject) {
                return result;
            }
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
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("获取topic评论信息失败，topicId:{0}，strResult:{1}", topicId, strResult), e);
        }
        return result;
    }

    /**
     * @param jsonReplies
     * @return
     */
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
            if (null != jsonAt) {
                TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                topicCommentAtInfo.setUserId(JsonUtil.optString(jsonAt, Constants.USER_ID));
                topicCommentAtInfo.setNickName(JsonUtil.optString(jsonAt, Constants.NICKNAME));
                topicCommentReplyInfo.setAt(topicCommentAtInfo);
            }

            topicCommentReplies.add(topicCommentReplyInfo);
        }
        return topicCommentReplies;
    }

    /**
     * 下载
     *
     * @param url
     * @param filePath
     * @throws NetworkDisconnectException
     */
    @Override
    public boolean download(String url, String filePath) throws NetworkDisconnectException {
        try {
            HttpClient.download(url, filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public TopicStatusInfo initUserTopicStatus(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        TopicStatusInfo topicStatusInfo = new TopicStatusInfo();
        String url = URL_PREFIX + "topics/" + topicId + "/actionsCount?uid=" + LoginAPI.getInstance().getLoginUserId();
        try {
            String strResult = HttpClient.getJson(url);
            JSONObject jsonObject = buildJSONObject(strResult);
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);
            int like = jsonResult.optInt(Constants.LIKE, 0);
            int collect = jsonResult.optInt(Constants.COLLECT, 0);
            topicStatusInfo.setIsLike(like != 0);
            topicStatusInfo.setIsCollect(collect != 0);
        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("获取用户topic状态失败，topicId：{1}，url：{2}", topicId, url), e);
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("获取用户topic状态失败，topicId：{1}，url：{2}", topicId, url), e);
        }
        return topicStatusInfo;
    }

    /**
     * 赞
     *
     * @param topicId
     * @param isLike
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public boolean setTopicLikeStatus(String topicId, boolean isLike) throws NetworkDisconnectException {
        return postTopicHandler(topicId, Constants.ACTION_TYPE_LIKE, isLike);
    }

    /**
     * 收藏
     *
     * @param topicId
     * @param isCollection
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public boolean setTopicCollectionStatus(String topicId, boolean isCollection) throws NetworkDisconnectException {
        return postTopicHandler(topicId, Constants.ACTION_TYPE_COLLECTION, isCollection);
    }

    /**
     * 操作视频（浏览，赞，收藏，转发）
     * POST /svc/nailstar/topics/xxxxx/actions
     * {
     * uid: “daskfjafjafd”,
     * type: 1, // 1浏览，2赞，3收藏，4转发
     * topicId: “rerjkdfkajfafdaf222”
     * }
     * <p/>
     * 取消操作（取消赞，取消收藏）
     * POST /svc/nailstar/topics/xxxxx/cancel
     * {
     * uid: “daskfjafjafd”,
     * type: 1, // 2赞，3收藏
     * topicId: “rerjkdfkajfafdaf222”
     * }
     *
     * @param topicId
     * @param type
     * @param isChecked
     * @throws NetworkDisconnectException
     */
    private boolean postTopicHandler(String topicId, int type, boolean isChecked) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String url = URL_PREFIX + "topics/" + topicId + (isChecked ? "/actions" : "/cancel");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.UID, LoginAPI.getInstance().getLoginUserId());
            jsonObject.put(Constants.TYPE, type);
            jsonObject.put(Constants.TOPIC_ID, topicId);
            String strResult = HttpClient.post(url, jsonObject.toString());
            return null != buildJSONObject(strResult);
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("topic操作失败，topicId:{0}，url:{1}", topicId, url), e);
        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("topic操作失败，topicId:{0}，url:{1}", topicId, url), e);
        }
        return false;
    }

    /**
     * {
     * author: “djfafdfalfafk”,
     * atUser: “fdafsdfafsf”,
     * content: “我也来评论一句”,
     * contentPic: “http://st.yilos.com/pic/dalfjlafjafdfrere.png”,
     * replyTo: “fdasjfkajdfkarere”,
     * lastReplyTo: "adsfafasfasfasf",
     * score: 1,
     * ready: 0 || 1
     * }
     *
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public String addComment(AddCommentInfo info) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String url = URL_PREFIX + "topics/" + info.getTopicId() + "/comments";
        try {
            JSONObject jsonObject = new JSONObject();
            // 当前登录用户Id
            jsonObject.put(Constants.AUTHOR, info.getUserId());
            // at用户Id
            jsonObject.put(Constants.AT_USER, info.getAtUserId());
            // 内容
            jsonObject.put(Constants.CONTENT, info.getContent());
            // 图片
            jsonObject.put(Constants.CONTENT_PIC, info.getContentPic());
            // commentId
            jsonObject.put(Constants.REPLY_TO, info.getReplayTo());
            // replyId
            jsonObject.put(Constants.LAST_REPLY_TO, info.getLastReplayTO());
            //
            jsonObject.put(Constants.READY, info.getReady());
            String strResult = HttpClient.post(url, jsonObject.toString());
            JSONObject jsonObj = buildJSONObject(strResult);
            return JsonUtil.optString(jsonObj.optJSONObject(Constants.RESULT), Constants.COMMENT_ID);
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("评论topic失败，topicId:{0}，url:{1}", info.getTopicId(), url), e);
        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("评论topic失败，topicId:{0}，url:{1}", info.getTopicId(), url), e);
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    public boolean addVideoPlayCount(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String url = URL_PREFIX + "videos/" + topicId + "/actions";
        try {
            String strResult = HttpClient.post(url, Constants.EMPTY_JSON_STRING);
            return null != buildJSONObject(strResult);
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("视频播放次数+1失败，topicId:{0}，url:{1}", topicId, url), e);
        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("视频播放次数+1失败，topicId:{0}，url:{1}", topicId, url), e);
        }
        return false;
    }

    @Override
    public int getSubmittedHomeworkCount(String topicId) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String url = URL_PREFIX + "qjc/topic/" + topicId + "/homeworks";
        try {
            String strResult = HttpClient.getJson(url);
            JSONObject jsonObject = buildJSONObject(strResult);
            if (null == jsonObject) {
                return 0;
            }
            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);

            return jsonResult.optInt(Constants.COUNT, 0);
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("视频播放次数+1失败，topicId:{0}，url:{1}", topicId, url), e);
        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("视频播放次数+1失败，topicId:{0}，url:{1}", topicId, url), e);
        }
        return 0;
    }

    @Override
    public void uploadFile2Oss(String localFilePath, String ossFileName, SaveCallback callback) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        OSSService ossService = OSSUtil.getDefaultOssService();
        OSSUtil.resumableUpload(ossService, ossService.getOssBucket(OSSUtil.BUCKET_YPICTURE), localFilePath, ossFileName, callback);
    }

    /**
     * {
     * picUrl: [
     * "http://pic.yilos.com/YYYYYYYY",
     * "http://pic.yilos.com/YYYYYYYY",
     * ],
     * id: "fjkhsdfjsfjds",   //id为postId, 或commentId
     * table: "posts"     //posts或homework
     * }
     *
     * @param info
     * @return
     * @throws NetworkDisconnectException
     */
    @Override
    public boolean updateReady(UpdateReadyInfo info) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String url = URL_PREFIX + "updateReady";
        try {
            JSONObject jsonObject = new JSONObject();
            // 当前登录用户Id
            jsonObject.put(Constants.ID, info.getId());
            JSONArray picUrls = new JSONArray();
            if (!CollectionUtil.isEmpty(info.getPicUrls())) {
                for (String picUrl : info.getPicUrls()) {
                    picUrls.put(picUrl);
                }
            }
            jsonObject.put(Constants.PIC_URL, picUrls);
            jsonObject.put(Constants.TABLE, info.getTable());
            String strResult = HttpClient.post(url, jsonObject.toString());
            return null != buildJSONObject(strResult);
        } catch (JSONException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("修改ready状态失败，id:{0}，url:{1}", info.getId(), url), e);
        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.error(MessageFormat.format("修改ready状态失败，id:{0}，url:{1}", info.getId(), url), e);
        }
        return false;
    }

    /**
     * 根据接口返回的结果构造JSONObject对象，如果接口返回的字符串为空，或者接口返回的code不为0（表示接口调用失败），则方法返回null
     *
     * @param jsonResultStr
     * @return
     * @throws JSONException
     */
    private JSONObject buildJSONObject(String jsonResultStr) throws JSONException {
        if (StringUtil.isEmpty(jsonResultStr)) {
            return null;
        }
        JSONObject jsonResult = new JSONObject(jsonResultStr);
        if (null == jsonResult || Constants.CODE_VALUE_SUCCESS != jsonResult.optInt(Constants.CODE, Constants.CODE_VALUE_FAIL)) {
            return null;
        }
        return jsonResult;
    }


    private boolean writeLocalJsonResult(String topicId, String result, String fileType) {
        File cacheDir = NailStarApplicationContext.getInstance().getCacheDir();
        String fileName = fileType + Constants.UNDERLINE + topicId + Constants.JSON_SUFFIX;
        File topicInfoFile = new File(cacheDir.getAbsolutePath() + "/" + fileName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(topicInfoFile);
            writer.write(result);
            return true;
        } catch (FileNotFoundException e) {
//            LOGGER.error(MessageFormat.format("写缓存文件失败，topicId：{0}，result：{1}，fileType：{2}", topicId, result, fileType), e);
//            e.printStackTrace();
        } catch (IOException e) {
//            LOGGER.error(MessageFormat.format("写缓存文件失败，topicId：{0}，result：{1}，fileType：{2}", topicId, result, fileType), e);
//            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
//                    LOGGER.error(MessageFormat.format("写缓存文件关闭流失败，topicId：{0}，result：{1}，fileType：{2}", topicId, result, fileType), e);
//                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private String getLocalJsonResult(String topicId, String fileType) {
        File cacheDir = NailStarApplicationContext.getInstance().getCacheDir();
        String fileName = fileType + Constants.UNDERLINE + topicId + Constants.JSON_SUFFIX;
        File topicInfoFile = new File(cacheDir.getAbsolutePath() + "/" + fileName);
        if (!topicInfoFile.exists()) {
            return Constants.EMPTY_JSON_STRING;
        }
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        FileInputStream fileInputStream = null;
        try {
            StringBuilder result = new StringBuilder();
            fileInputStream = new FileInputStream(topicInfoFile);
            inputStreamReader = new InputStreamReader(fileInputStream);
            reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return StringUtil.isEmpty(result.toString()) ? Constants.EMPTY_JSON_STRING : result.toString();
        } catch (FileNotFoundException e) {
//            LOGGER.error(MessageFormat.format("读缓存文件失败，topicId：{0}，fileType：{1}", topicId, fileType), e);
//            e.printStackTrace();
        } catch (IOException e) {
//            LOGGER.error(MessageFormat.format("读缓存文件失败，topicId：{0}，fileType：{1}", topicId, fileType), e);
//            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
//                    LOGGER.error(MessageFormat.format("写缓存文件关闭流失败，topicId：{0}，fileType：{1}", topicId, fileType), e);
//                    e.printStackTrace();
                }
            }
            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
//                    LOGGER.error(MessageFormat.format("写缓存文件关闭流失败，topicId：{0}，fileType：{1}", topicId, fileType), e);
//                    e.printStackTrace();
                }
            }
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
//                    LOGGER.error(MessageFormat.format("写缓存文件关闭流失败，topicId：{0}，fileType：{1}", topicId, fileType), e);
//                    e.printStackTrace();
                }
            }
        }
        return Constants.EMPTY_JSON_STRING;
    }
}
