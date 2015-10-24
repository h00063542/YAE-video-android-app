package com.yilos.nailstar.player.model;

import com.sina.sinavideo.coreplayer.util.StringUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.VideoEntity;
import com.yilos.nailstar.player.entity.VideoImageTextInfoEntity;
import com.yilos.nailstar.player.entity.VideoInfoEntity;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-24.
 */
public class VideoPlayerServiceImpl implements IVideoPlayerService {
    @Override
    public VideoInfoEntity getVideoInfo(String topicsId) throws IOException, JSONException, NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }

        String strVideoInfo = "{\"code\":0,\"result\":{\"id\":\"a2a5e610-7a28-11e5-8f05-5777b06446a2\",\"title\":\"第430期：小王子\",\"thumbUrl\":\"http://pic.yilos.com/a1e5949fdf2f126f35aea95be94c0167\",\"author\":\"胡波老师\",\"authorId\":\"f2ecae80-f943-11e4-a616-5fd981a864f9\",\"authorPhoto\":\"http://pic.yilos.com/c2dde277c7df7bbadfc4d4426d6d894f\",\"createDate\":1445675065073,\"type\":\"video\",\"tags\":[\"卡通\"],\"videos\":[{\"videoId\":\"5937a3c0-7a27-11e5-8f05-5777b06446a2\",\"ccUrl\":\"\",\"ossUrl\":\"http://v.yilos.com/a953d369ac77b1c20ff377bd13ed64db.mp4\",\"playTimes\":19}]}}";
                //HttpClient.getJson("/vapi/nailstar/topics/28a5cb30-798a-11e5-a0a9-07ce182a6f");
        VideoInfoEntity videoInfoEntity = null;

        if (null != strVideoInfo && strVideoInfo.length() > 0) {
            videoInfoEntity = new VideoInfoEntity();
            JSONObject jsonObject = new JSONObject(strVideoInfo);
            JSONObject jsonResult = jsonObject.optJSONObject("result");

            ArrayList tags = new ArrayList();
            JSONArray jsonTags = jsonResult.optJSONArray("tags");
            for (int i = 0; i < jsonTags.length(); i++) {
                tags.add(jsonTags.optString(i, null));
            }

            ArrayList videos = new ArrayList();
            JSONArray jsonVideos = jsonResult.optJSONArray("videos");
            for (int i = 0; i < jsonVideos.length(); i++) {
                JSONObject jsonVideoObj = jsonVideos.optJSONObject(i);
                videos.add(new VideoEntity(jsonVideoObj.optString("videoId", null), jsonVideoObj.optInt("playTimes", 0), jsonVideoObj.optString("ccUrl", null), jsonVideoObj.optString("ossUrl", null)));
            }


            videoInfoEntity.setId(jsonResult.optString("id", null));
            videoInfoEntity.setTitle(jsonResult.optString("title", null));
            videoInfoEntity.setType(jsonResult.optString("type", null));
            videoInfoEntity.setAuthorPhoto(jsonResult.optString("authorPhoto", null));
            videoInfoEntity.setThumbUrl(jsonResult.optString("thumbUrl", null));
            videoInfoEntity.setCreateDate(jsonResult.optLong("createDate", 0));
            videoInfoEntity.setTags(tags);
            videoInfoEntity.setVideos(videos);
            videoInfoEntity.setAuthorId(jsonResult.optString("authorId", null));
            videoInfoEntity.setAuthor(jsonResult.optString("author", null));
        }

        return videoInfoEntity;
    }

    @Override
    public VideoImageTextInfoEntity getVideoImageTextInfo(String topicsId) throws NetworkDisconnectException, IOException, JSONException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String strVideoImageTextInfo = "{\"code\":0,\"result\":{\"id\":\"a2a5e610-7a28-11e5-8f05-5777b06446a2\",\"pictures\":[\"http://pic.yilos.com/d4f1f375d54b4eb7c12cf9e8c3de046b\",\"http://pic.yilos.com/d02bcbb76cb0d1f12bd256fc5fd1fc47\",\"http://pic.yilos.com/23c4421de2110dbb6c7a1f27a2cc5039\",\"http://pic.yilos.com/4b781a9c5ae28daf8ec7c0ead557c30d\",\"http://pic.yilos.com/01cedef231e0e4ad35f280fa13fabbe3\",\"http://pic.yilos.com/0cdb37ae4e001bb982aabe747a63b5ce\",\"http://pic.yilos.com/de76bf4578a8cd8f8fd87e058f0dd86a\",\"http://pic.yilos.com/ba9d8a431b1a42da6769a7562878eeb5\",\"http://pic.yilos.com/e1d85828863ec4b48c193040bb07cd23\",\"http://pic.yilos.com/278df0a6826e0cfb64531f5d1c39a877\",\"http://pic.yilos.com/8c9bdbbf35cd3a778cc8de2daf4f9025\",\"http://pic.yilos.com/1923e8ee8879c0e01ca7ae2d7af949dc\",\"http://pic.yilos.com/1731b6213759aeacbd3843fbea084857\",\"http://pic.yilos.com/5fa3ad362893fb76f1a1969b634d0fc0\",\"http://pic.yilos.com/e285d6a9b334b144284bc56d237db6c4\",\"http://pic.yilos.com/7490883d99a9e564ad2ff32ff39f6268\",\"http://pic.yilos.com/318bb386e81a347223a47000d8044b9f\",\"http://pic.yilos.com/43de3e2ed4a27ccda73bb6a1483c1a33\",\"http://pic.yilos.com/44428e84cb1702656a233e864c1b6be7\",\"http://pic.yilos.com/9c879b8e40e754bb9fd440516be8d72c\",\"http://pic.yilos.com/7f0edf260c88b95605bea3f53bffc565\"],\"articles\":[\"\",\"1.整甲均匀涂抹两遍白色作为底色，分别照灯\",\"2.整甲均匀涂抹封层 无需照灯\",\"3.取蓝紫色涂抹不规则色条\",\"4.取蓝色涂抹不规则色条\",\"5.取少量紫色涂抹不规则色条\",\"6.用线条笔将颜色边缘做不规则晕染，照灯\",\"7.整甲涂抹封层 无需照灯\",\"8.取蓝绿色涂抹不规则色块\",\"9.取白色涂抹不规则色条\",\"10.用线条笔将颜色边缘做不规则晕染，照灯\",\"11.整甲涂抹封层 无需照灯\",\"12.取黑色涂抹不规则色条\",\"13.取蓝色涂抹不规则色条\",\"14.用线条笔将颜色边缘做不规则晕染，照灯\",\"15.用线条笔取黑色胶绘制人物的粘整体轮廓\",\"16.用线条笔取黑色胶填充轮廓内颜色\",\"17.线条笔取黑色胶绘制铲子图案，照灯\",\"18.整甲均匀涂抹封层，照灯\",\"19.清洁甲面浮胶，完成\",\"\"]}}";
        //HttpClient.getJson("/vapi/nailstar/topics/article/28a5cb30-798a-11e5-a0a9-07ce182a6f");
        VideoImageTextInfoEntity videoImageTextInfoEntity = null;

        if (null != strVideoImageTextInfo && strVideoImageTextInfo.length() > 0) {
            videoImageTextInfoEntity = new VideoImageTextInfoEntity();
            JSONObject jsonObject = new JSONObject(strVideoImageTextInfo);
            JSONObject jsonResult = jsonObject.optJSONObject("result");

            ArrayList pictures = new ArrayList();
            JSONArray jsonPictures = jsonResult.optJSONArray("pictures");
            for (int i = 0; i < jsonPictures.length(); i++) {
                pictures.add(jsonPictures.optString(i, null));
            }

            ArrayList articles = new ArrayList();
            JSONArray jsonArticles = jsonResult.optJSONArray("articles");
            for (int i = 0; i < jsonArticles.length(); i++) {
                articles.add(jsonArticles.optString(i, null));
            }

            videoImageTextInfoEntity.setId(jsonResult.optString("id", null));
            videoImageTextInfoEntity.setArticles(articles);
            videoImageTextInfoEntity.setPictures(pictures);
        }

        return videoImageTextInfoEntity;
    }
}
