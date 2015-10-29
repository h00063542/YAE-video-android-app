package com.yilos.nailstar.util;

import android.os.Environment;

/**
 * Created by yilos on 2015-10-28.
 */
public class Constants {
    public static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";
    public static final String YILOS_NAILSTAR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos/nailstar";
    public static final String YILOS_NAILSTAR_LOGS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos/nailstar/logs";

    public static final String TOPIC_ID = "topicId";
    public static final String CODE = "code";

    public static final String RESULT = "result";
    public static final String TAGS = "tags";
    public static final String VIDEOS = "videos";
    public static final String VIDEO_ID = "videoId";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String AUTHOR_PHOTO = "authorPhoto";
    public static final String THUMB_URL = "thumbUrl";
    public static final String CREATE_DATE = "createDate";
    public static final String AUTHOR_ID = "authorId";
    public static final String AUTHOR = "author";
    public static final String PICTURES = "pictures";
    public static final String ARTICLES = "articles";
    public static final String RELATED = "related";
    public static final String COMMENTS = "comments";
    public static final String USER_ID_LOWER = "userid";
    public static final String CONTENT = "content";
    public static final String CONTENT_PIC = "contentPic";
    public static final String IS_HOME_WORK = "isHomework";
    public static final String IS_MINE = "isMine";
    public static final String STATUS = "status";
    public static final String AT = "at";
    public static final String USER_ID = "userId";
    public static final String NICKNAME = "nickname";
    public static final String REPLIES = "replys";

    public static final long ONE_MB_SIZE = 1024L * 1024;
}
