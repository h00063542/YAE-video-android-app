package com.yilos.nailstar.util;

import android.os.Environment;

/**
 * Created by yilos on 2015-10-28.
 */
public class Constants {

    public static final String YILOS_API_SERVER = "http://api3.naildaka.com";
    public static final String YILOS_STATIC_SERVER = "http://s.naildaka.com";


    public static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos/";
    public static final String YILOS_NAILSTAR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos/nailstar/";
    public static final String YILOS_NAILSTAR_VIDEOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos/nailstar/videos/";
    public static final String YILOS_NAILSTAR_PICTURE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos/nailstar/picture/";

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
    public static final String COMMODITY = "commodities";

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
    public static final String PHOTO = "photo";
    public static final String REPLIES = "replys";

    public static final String PLAY_TIMES = "playTimes";
    public static final String CC_URL = "ccUrl";
    public static final String OSS_URL = "ossUrl";
    public static final String PIC_URL = "picUrl";

    public static final String UID = "uid";
    public static final int ACTION_TYPE_LIKE = 2;
    public static final int ACTION_TYPE_COLLECTION = 3;
    public static final String AT_USER = "atUser";
    public static final String REPLY_TO = "replyTo";
    public static final String LAST_REPLY_TO = "lastReplyTo";
    public static final String READY = "ready";
    public static final String LIKE = "like";
    public static final String COLLECT = "collect";
    public static final String COMMENT_ID = "commentId";
    public static final int READY_HOMEWORK = 0;
    public static final int READY_COMMENT = 1;
    public static final String TABLE = "table";
    public static final String HOMEWORK = "homework";
    public static final String POSTS = "posts";

    public static final int IS_HOME_WORK_VALUE = 1;
    public static final int NOT_HOME_WORK_VALUE = 0;

    public static final long ONE_MB_SIZE = 1024L * 1024;

    public static final String EMPTY_STRING = "";
    public static final String EMPTY_JSON_STRING = "{}";


    public static final int CODE_VALUE_SUCCESS = 0;
    public static final int CODE_VALUE_FAIL = 1;

    /**
     * 视频宽高比例
     */
    public static final double VIDEO_ASPECT_RATIO = 1.78d;


    /**
     * 图文分解宽高比例
     */
    public static final double IMAGE_TEXT_ASPECT_RATIO = 1.75d;

    /**
     * 评论
     */
    public static final int TOPIC_COMMENT_TYPE_COMMENT = 1;

    /**
     * 回复
     */
    public static final int TOPIC_COMMENT_TYPE_REPLY = 2;

    /**
     * 回复评论中的回复
     */
    public static final int TOPIC_COMMENT_TYPE_REPLY_AGAIN = 3;

    public static final int TOPIC_COMMENT_REQUEST_CODE = 3;
    public static final int TOPIC_HOMEWORK_REQUEST_CODE = 4;

    public static final String TOPIC_COMMENT_ID = "topicCommentId";
    public static final String TOPIC_NEW_COMMENT_ID = "topicNewCommentId";
    public static final String TOPIC_COMMENT_USER_ID = "topicCommentUserId";
    public static final String TOPIC_COMMENT_AUTHOR = "topicCommentAuthor";

    public static final String TOPIC_COMMENT_REPLY_ID = "topicCommentReplyId";
    public static final String TOPIC_COMMENT_REPLY_USER_ID = "topicCommentUserId";
    public static final String TOPIC_COMMENT_REPLY_AUTHOR = "topicCommentReplyAuthor";

    public static final int TOPIC_COMMENTS_INIT_ORDER_BY_ASC = 1;
    public static final int TOPIC_COMMENTS_INIT_ORDER_BY_DESC = 2;


    public static final String COMMENT_COUNT = "commentCount";

    /**
     * 显示更多视频数量
     */
    public static final int MORE_VIDEOS_COUNT = 3;

    public static final String POINT = ".";

    public static final String FILE_NAME_TOPIC_INFO = "topic_info";
    public static final String FILE_NAME_TOPIC_IMAGE_TEXT_INFO = "topic_image_text_info";
    public static final String FILE_NAME_TOPIC_RELATE_INFO = "topic_relate_info";
    public static final String FILE_NAME_TOPIC_RELATE_USED_PRODUCT_INFO = "topic_relate_used_product_info";
    public static final String FILE_NAME_TOPIC_COMMENT_INFO = "topic_comment_info";

    public static final String UNDERLINE = "_";
    public static final String JSON_SUFFIX = ".json";
    public static final String PNG_SUFFIX = ".png";
    public static final String JPG_SUFFIX = ".jpg";
    public static final String COUNT = "count";

    /**
     * 交作业图片宽高比例
     */
    public static final int HOMEWORK_PIC_ASPECT_RATIO = 1;
    /**
     * 截取的交作业图片像素
     */
    public static final int HOMEWORK_PIC_PIXEL = 400;


    public static final String TOPIC_PRODUCT_HELPER_URL = YILOS_STATIC_SERVER + "/shop/rule.html";

    /**
     * 通用WebView展示参数
     */
    public static final String WEBVIEW_TITLE = "title";
    public static final String WEBVIEW_URL = "url";


    /**
     * 淘宝跳转ISV_CODE
     */
    public static final String ISV_CODE = "nailstar_app_android";


    public static final String YILOS_PIC_URL = "http://pic.yilos.com/";
    public static final String DEFAULT_OSS_HOST_ID = "oss-cn-hangzhou.aliyuncs.com";
    public static final String TOPIC_SHARE_URL = "http://s.naildaka.com/site/video_detail.html?topicId=%1$s&allowBack=1";


    public static final String TEACHER = "teacher";
    public static final String COMMENT = "comment";
    public static final String ATNAME = "atName";
    public static final String REPLY = "reply";
    public static final String ACCOUNTID = "accountId";
    public static final String ACCOUNTNAME = "accountName";
    public static final String ACCOUNTPHOTO = "accountPhoto";

    /**
     * 已回复：true
     * 未回复：false
     */
    public static final String HASBEENREPLY = "hasBeenReply";
    public static final String SCORE = "score";
    public static final String MESSAGES = "messages";
    public static final String OK = "ok";
    public static final String USERMESSAGE = "userMessage";
    public static final String PUBLISH_DATE = "publishDate";
    public static final String LATEST_MESSAGE_TIME = "latestMessageTime";
    public static final String LATEST_MESSAGE_COUNT_TIME = "latestMessageCountTime";
    public static final String LATEST_MESSAGE_COUNT = "latestMessageCount";
    public static final String USER_MESSAGE_ARRAY_LIST = "userMessageArrayList";
    public static final String SYSTEM_MESSAGE_LIST = "systemMessageList";
    public static final String HAS_BEEN_READ = "hasBeenRead";


    //商城相关常量
    public static final String MALL_COMMODITY_CATE_ID = "commodityCateId";

    public static final String COVER = "cover";
    public static final String APPEND = "append";
    public static final int ZERO = 0;
    public static final String CANDIDATES = "candidates";
    public static final String NO = "no";


    public static final String APPLICATION_SETTING = "application_setting";
    public static final String ALLOW_NO_WIFI = "allow_no_wifi";
    public static final String IS_FIRST_INTO_VERSION = "isFirstIntoVersion";
    public static final String SDCARD_NAME = "sdcard_name";
    public static final String SDCARD_PATH = "sdcard_path";
    public static final String VERSION_NAME = "version_name";
    public static final String PERSON_INFO = "personInfo";

    public static final int PERSON_INFO_NAME_MAX_LENGTH = 8;

    public static final String EXPERIENCE = "experience";

    public static final String MY_IMAGE_URL = "myImageUrl";
}
