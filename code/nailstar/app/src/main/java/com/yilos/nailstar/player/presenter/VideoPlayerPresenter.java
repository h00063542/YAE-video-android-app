package com.yilos.nailstar.player.presenter;

import android.os.Handler;
import android.os.Message;

import com.yilos.nailstar.player.entity.VideoEntity;
import com.yilos.nailstar.player.entity.VideoImageTextInfoEntity;
import com.yilos.nailstar.player.entity.VideoInfoEntity;
import com.yilos.nailstar.player.view.IVideoPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by yilos on 2015-10-22.
 */
public class VideoPlayerPresenter {
    private IVideoPlayerView videoPlayerView;

    public VideoPlayerPresenter(IVideoPlayerView videoPlayerView) {
        this.videoPlayerView = videoPlayerView;
    }

    public void playerVideo(String videoId) throws JSONException {
        getVideoInfo(videoId, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                videoPlayerView.playVideo((VideoInfoEntity) msg.obj);
            }
        });
    }

    public void getVideoInfo(String id, Handler handler) throws JSONException {
        // TODO 调用后台接口，获取视频信息
        String data = "{\"code\":0,\"result\":{\"id\":\"364d47a0-7871-11e5-976b-35813a43b6ad\",\"title\":\"第428期：七彩流云\",\"thumbUrl\":\"http://pic.yilos.com/757ab962db7882ee2c4d144258e122c3\",\"author\":\"胡小老师\",\"authorId\":\"cebd22b0-02c6-11e5-8ef3-eb13cbbd5518\",\"authorPhoto\":\"http://pic.yilos.com/aba71fe570196c1efcacce09f57454b0\",\"createDate\":1445486334234,\"type\":\"video\",\"tags\":[\"晕染\",\"多彩色\",\"公主范\",\"日韩\",\"饰品\",\"线条\",\"甲油胶\"],\"videos\":[{\"videoId\":\"04688510-77db-11e5-976b-35813a43b6ad\",\"ccUrl\":\"\",\"ossUrl\":\"http://v.yilos.com/540f4f1abb417b1eb541c2dc53ddde08.mp4\",\"playTimes\":762}]}}";

        JSONObject jsonObject = new JSONObject(data);
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


        VideoInfoEntity videoInfoEntity = new VideoInfoEntity();
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
        Message message = handler.obtainMessage(0, videoInfoEntity);
        handler.sendMessage(message);
    }

    public void getVideoImageTextInfo(String id, Handler handler) throws JSONException {
        // TODO 调用后台接口，获取视频图文信息
        String data = "{\"code\":0,\"result\":{\"id\":\"364d47a0-7871-11e5-976b-35813a43b6ad\",\"pictures\":[\"http://pic.yilos.com/7b7fb15f80bc26fcc3ab75a2b62bbbe8\",\"http://pic.yilos.com/668da125e3efe541a3177c479b68b400\",\"http://pic.yilos.com/c94d95e4e37894cc435dd83bcb69b6e4\",\"http://pic.yilos.com/fb10140bc2addb93d712886077f178e0\",\"http://pic.yilos.com/ac95e799b3a0602b831c039bd4982812\",\"http://pic.yilos.com/8bf280e5ecfd5ab2ad594edcb224eed8\",\"http://pic.yilos.com/fc5b214d01b3119e126bcb69fcfa7125\",\"http://pic.yilos.com/137d049cf27cc9bc3ac2f54dd208ed68\",\"http://pic.yilos.com/5f9a912401f79dcfb592d022ae48982b\",\"http://pic.yilos.com/a3ab278cc961a94154aee3c797317213\",\"http://pic.yilos.com/a639f74f8b7bb521895f18abce10bbae\",\"http://pic.yilos.com/e9f976b36454868bc9c3b58e98c75c89\",\"http://pic.yilos.com/50410bb02d7eecad016cb6b00bb0d010\",\"http://pic.yilos.com/913a7a094c81e9545a7d3ea75b1a6994\",\"http://pic.yilos.com/7e083817451646d65d3a8581a9661505\",\"http://pic.yilos.com/e756e9c30ee1d263cd0693b698806a96\",\"http://pic.yilos.com/788a3cac1068befd8eaf9154740da4d3\",\"http://pic.yilos.com/2f1deb290584d9ccf064b7813b79c313\"],\"articles\":[\"\",\"1.\\t整甲均匀涂抹两遍乳白色作为底色，分别照灯\",\"2.\\t整甲均匀涂抹封层，无需照灯\",\"3.\\t取蓝色在甲面做出不规则的色块晕染\",\"4.\\t取粉色在甲面做出不规则色块晕染\",\"5.\\t取绿色在甲面做出不规则色块晕染\",\"6.\\t取黄色在甲面做出不规则色块晕染\",\"7.\\t取紫色在甲面做出不规则色块晕染\",\"8.\\t将颜色与颜色边缘进行晕染，照灯\",\"9.\\t整甲均匀涂抹封层，无需照灯\",\"10.\\t重复做第二遍不规则色块晕染，照灯\",\"11.\\t整甲均匀涂抹封层，照灯\",\"12.\\t清洁甲面浮胶\",\"13.\\t线条笔取白色在甲面上画出少量纵向线条进行装饰，照灯\",\"14.\\t涂抹透明胶镶嵌饰品，照灯\",\"15.\\t整甲均匀涂抹封层，照灯\",\"16.\\t清洁甲面浮胶，完成\",\"17.\\t完成，展示\"]}}";
        JSONObject jsonObject = new JSONObject(data);
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

        VideoImageTextInfoEntity videoImageTextInfoEntity = new VideoImageTextInfoEntity();
        videoImageTextInfoEntity.setId(jsonResult.optString("id", null));
        videoImageTextInfoEntity.setArticles(articles);
        videoImageTextInfoEntity.setPictures(pictures);
        Message message = handler.obtainMessage(0, videoImageTextInfoEntity);
        handler.sendMessage(message);
    }
}
