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

        //String strVideoInfo = "{\"code\":0,\"result\":{\"id\":\"a2a5e610-7a28-11e5-8f05-5777b06446a2\",\"title\":\"第430期：小王子\",\"thumbUrl\":\"http://pic.yilos.com/a1e5949fdf2f126f35aea95be94c0167\",\"author\":\"胡波老师\",\"authorId\":\"f2ecae80-f943-11e4-a616-5fd981a864f9\",\"authorPhoto\":\"http://pic.yilos.com/c2dde277c7df7bbadfc4d4426d6d894f\",\"createDate\":1445675065073,\"type\":\"video\",\"tags\":[\"卡通\"],\"videos\":[{\"videoId\":\"5937a3c0-7a27-11e5-8f05-5777b06446a2\",\"ccUrl\":\"\",\"ossUrl\":\"http://v.yilos.com/a953d369ac77b1c20ff377bd13ed64db.mp4\",\"playTimes\":19}]}}";
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

//        String strVideoImageTextInfo = "{\"code\":0,\"result\":{\"id\":\"a2a5e610-7a28-11e5-8f05-5777b06446a2\",\"pictures\":[\"http://pic.yilos.com/d4f1f375d54b4eb7c12cf9e8c3de046b\",\"http://pic.yilos.com/d02bcbb76cb0d1f12bd256fc5fd1fc47\",\"http://pic.yilos.com/23c4421de2110dbb6c7a1f27a2cc5039\",\"http://pic.yilos.com/4b781a9c5ae28daf8ec7c0ead557c30d\",\"http://pic.yilos.com/01cedef231e0e4ad35f280fa13fabbe3\",\"http://pic.yilos.com/0cdb37ae4e001bb982aabe747a63b5ce\",\"http://pic.yilos.com/de76bf4578a8cd8f8fd87e058f0dd86a\",\"http://pic.yilos.com/ba9d8a431b1a42da6769a7562878eeb5\",\"http://pic.yilos.com/e1d85828863ec4b48c193040bb07cd23\",\"http://pic.yilos.com/278df0a6826e0cfb64531f5d1c39a877\",\"http://pic.yilos.com/8c9bdbbf35cd3a778cc8de2daf4f9025\",\"http://pic.yilos.com/1923e8ee8879c0e01ca7ae2d7af949dc\",\"http://pic.yilos.com/1731b6213759aeacbd3843fbea084857\",\"http://pic.yilos.com/5fa3ad362893fb76f1a1969b634d0fc0\",\"http://pic.yilos.com/e285d6a9b334b144284bc56d237db6c4\",\"http://pic.yilos.com/7490883d99a9e564ad2ff32ff39f6268\",\"http://pic.yilos.com/318bb386e81a347223a47000d8044b9f\",\"http://pic.yilos.com/43de3e2ed4a27ccda73bb6a1483c1a33\",\"http://pic.yilos.com/44428e84cb1702656a233e864c1b6be7\",\"http://pic.yilos.com/9c879b8e40e754bb9fd440516be8d72c\",\"http://pic.yilos.com/7f0edf260c88b95605bea3f53bffc565\"],\"articles\":[\"\",\"1.整甲均匀涂抹两遍白色作为底色，分别照灯\",\"2.整甲均匀涂抹封层 无需照灯\",\"3.取蓝紫色涂抹不规则色条\",\"4.取蓝色涂抹不规则色条\",\"5.取少量紫色涂抹不规则色条\",\"6.用线条笔将颜色边缘做不规则晕染，照灯\",\"7.整甲涂抹封层 无需照灯\",\"8.取蓝绿色涂抹不规则色块\",\"9.取白色涂抹不规则色条\",\"10.用线条笔将颜色边缘做不规则晕染，照灯\",\"11.整甲涂抹封层 无需照灯\",\"12.取黑色涂抹不规则色条\",\"13.取蓝色涂抹不规则色条\",\"14.用线条笔将颜色边缘做不规则晕染，照灯\",\"15.用线条笔取黑色胶绘制人物的粘整体轮廓\",\"16.用线条笔取黑色胶填充轮廓内颜色\",\"17.线条笔取黑色胶绘制铲子图案，照灯\",\"18.整甲均匀涂抹封层，照灯\",\"19.清洁甲面浮胶，完成\",\"\"]}}";
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

//        String strTopicsRelatedInfo = "{\"code\":0,\"result\":{\"related\":[{\"topicId\":\"4000e420-39cc-11e5-9eec-cba09d729d50\",\"thumbUrl\":\"http://pic.yilos.com/d3553ba76d0c652e9a2f5fbfa6ec515e\"},{\"topicId\":\"a43bcb80-6433-11e5-a2e0-45cab9e575f2\",\"thumbUrl\":\"http://pic.yilos.com/08cfba9910053543da7e5174cfa1c25c\"},{\"topicId\":\"09508660-47f5-11e5-9985-7d2603ee4296\",\"thumbUrl\":\"http://pic.yilos.com/f5d9326ea6318c57ddc87605cbb3ffe9\"},{\"topicId\":\"103c0d80-5f58-11e5-a2e0-45cab9e575f2\",\"thumbUrl\":\"http://pic.yilos.com/711f2f350e1ec9006260efb7b03bd533\"}]}}";
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
                result.add(new TopicRelatedInfo(topicId, jsonRelated.optJSONObject(i).optString(Constants.THUMB_URL, null)));
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

//        String strTopicCommentsInfo = "{\"code\":0,\"result\":{\"comments\":[{\"id\":\"7a875480-7c52-11e5-aa3d-abfae2ac135f\",\"createDate\":1445912938957,\"content\":\"王红老师手有点干哦！还是有点掉皮！保护下\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[],\"author\":\"手机用户8050\",\"authorPhoto\":null,\"userid\":\"ddf37d70-7796-11e5-aa3d-abfae2ac135f\",\"isMine\":0},{\"id\":\"586e2990-7be5-11e5-8e66-81018f73d078\",\"createDate\":1445866066606,\"content\":\"\uD83D\uDC4D\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"85e765d0-7be5-11e5-8e66-81018f73d078\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445866142908,\"content\":\"\uD83D\uDE0A\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"384d04e0-7624-11e5-976b-35813a43b6ad\",\"nickname\":\"进巍美甲萱萱\"}}],\"author\":\"进巍美甲萱萱\",\"authorPhoto\":\"http://pic.yilos.com/d85026b14d6e45e397c6049c35fe1466\",\"userid\":\"384d04e0-7624-11e5-976b-35813a43b6ad\",\"isMine\":0},{\"id\":\"9f116c60-7be3-11e5-aa3d-abfae2ac135f\",\"createDate\":1445865326124,\"content\":\"这个有点简单，太难的都做不好我\uD83D\uDE2D\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"e45d4aa0-7be3-11e5-8e66-81018f73d078\",\"userid\":\"18d8f4c0-7624-11e5-976b-35813a43b6ad\",\"author\":\"蔻丹美甲\",\"createDate\":1445865442392,\"content\":\"是有点简单，大家都懒得交作业了\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"fd9612d0-6e2e-11e5-a2e0-45cab9e575f2\",\"nickname\":\"阿喔额咦唔吁\"}},{\"id\":\"51c3a920-7be6-11e5-aa3d-abfae2ac135f\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445866484928,\"content\":\"款式只是教大家一个技法，你可以根据简单的款式技法演变出复杂的款式，好的美甲师要学会变通\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"18d8f4c0-7624-11e5-976b-35813a43b6ad\",\"nickname\":\"蔻丹美甲\"}},{\"id\":\"83805b70-7be6-11e5-8e66-81018f73d078\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445866568372,\"content\":\"难的款式也是从最基础的款式做起要有信心，加油\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"fd9612d0-6e2e-11e5-a2e0-45cab9e575f2\",\"nickname\":\"阿喔额咦唔吁\"}}],\"author\":\"阿喔额咦唔吁\",\"authorPhoto\":\"http://pic.yilos.com/120fa89e121d7502d2bdd873ef0b5fce\",\"userid\":\"fd9612d0-6e2e-11e5-a2e0-45cab9e575f2\",\"isMine\":0},{\"id\":\"23bb0580-7be3-11e5-aa3d-abfae2ac135f\",\"createDate\":1445865119199,\"content\":\"几天没来，来看看\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"744da140-7be5-11e5-aa3d-abfae2ac135f\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445866113377,\"content\":\"谢谢关注\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"f7d87fe0-6e2e-11e5-a2e0-45cab9e575f2\",\"nickname\":\"ε光着尐脚丫\"}}],\"author\":\"ε光着尐脚丫\",\"authorPhoto\":\"http://pic.yilos.com/7d3991732cf0d21bad938e1f997bd8c3\",\"userid\":\"f7d87fe0-6e2e-11e5-a2e0-45cab9e575f2\",\"isMine\":0},{\"id\":\"ac021a10-7bc9-11e5-aa3d-abfae2ac135f\",\"createDate\":1445854180919,\"content\":\"好看，可以变做年轮，好像看到大咖老师做年轮款的\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"e756f5e0-7bd3-11e5-8e66-81018f73d078\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445858575436,\"content\":\"是的应有两款\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"2acd5220-6e31-11e5-a2e0-45cab9e575f2\",\"nickname\":\"滺VSAMA\"}}],\"author\":\"滺VSAMA\",\"authorPhoto\":null,\"userid\":\"2acd5220-6e31-11e5-a2e0-45cab9e575f2\",\"isMine\":0},{\"id\":\"0a3497d0-7bc9-11e5-8e66-81018f73d078\",\"createDate\":1445853909458,\"content\":\"简单款式，很常用\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"fc257fa0-7bd3-11e5-8e66-81018f73d078\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445858610359,\"content\":\"店面做的多些\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"0a433e20-6e31-11e5-a2e0-45cab9e575f2\",\"nickname\":\"发光的草莓糖\"}}],\"author\":\"发光的草莓糖\",\"authorPhoto\":null,\"userid\":\"0a433e20-6e31-11e5-a2e0-45cab9e575f2\",\"isMine\":0},{\"id\":\"883a5cb0-7bc8-11e5-8e66-81018f73d078\",\"createDate\":1445853691393,\"content\":\"我做不像这个木纹，老师我私下发你吧\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"05c30be0-7bd4-11e5-aa3d-abfae2ac135f\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445858626476,\"content\":\"好的\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"f99c6510-6e30-11e5-a2e0-45cab9e575f2\",\"nickname\":\"shi宝宝呢\"}}],\"author\":\"shi宝宝呢\",\"authorPhoto\":null,\"userid\":\"f99c6510-6e30-11e5-a2e0-45cab9e575f2\",\"isMine\":0},{\"id\":\"34e114f0-7bc8-11e5-aa3d-abfae2ac135f\",\"createDate\":1445853551556,\"content\":\"木纹甲 正在找这款呢\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"0d2b4190-7bd4-11e5-8e66-81018f73d078\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445858638901,\"content\":\"是吗\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"e6143e00-6e30-11e5-a2e0-45cab9e575f2\",\"nickname\":\"浅夏~流年\"}}],\"author\":\"浅夏~流年\",\"authorPhoto\":\"http://pic.yilos.com/1b91f637510fd632ff6941cb9abfea37\",\"userid\":\"e6143e00-6e30-11e5-a2e0-45cab9e575f2\",\"isMine\":0},{\"id\":\"290383c0-7bc8-11e5-aa3d-abfae2ac135f\",\"createDate\":1445853531649,\"content\":\"老师看看 是不是哪错了 不太对\",\"contentPic\":\"http://pic.yilos.com/7babc58b5b0d58524015bd7a2abef7af\",\"isHomework\":1,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"165f0f80-7bd4-11e5-aa3d-abfae2ac135f\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445858654345,\"content\":\"纹理少了\",\"contentPic\":null,\"isHomework\":1,\"status\":1,\"at\":{\"userId\":\"206ee630-4f8d-11e5-a1be-5fb073697963\",\"nickname\":\"小花\"}}],\"author\":\"小花\",\"authorPhoto\":null,\"userid\":\"206ee630-4f8d-11e5-a1be-5fb073697963\",\"isMine\":0},{\"id\":\"f84b2210-7bc7-11e5-aa3d-abfae2ac135f\",\"createDate\":1445853449911,\"content\":\"王红老师美甲，必需看\uD83C\uDF39\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\"},\"replys\":[{\"id\":\"aa7c58e0-7bd3-11e5-aa3d-abfae2ac135f\",\"userid\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"author\":\"王红老师\",\"createDate\":1445858473340,\"content\":\"谢谢支持\",\"contentPic\":null,\"isHomework\":0,\"status\":1,\"at\":{\"userId\":\"2b647e80-6e30-11e5-a2e0-45cab9e575f2\",\"nickname\":\"璃馨i\"}}],\"author\":\"璃馨i\",\"authorPhoto\":\"http://pic.yilos.com/b92f1d3f48aeb43b32d9037dc285d46f\",\"userid\":\"2b647e80-6e30-11e5-a2e0-45cab9e575f2\",\"isMine\":0}]}}";
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
                TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                topicCommentAtInfo.setUserId(jsonAt.optString(Constants.USER_ID, null));
                topicCommentAtInfo.setNickName(jsonAt.optString(Constants.NICKNAME, null));
                topicCommentInfo.setAt(topicCommentAtInfo);

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
