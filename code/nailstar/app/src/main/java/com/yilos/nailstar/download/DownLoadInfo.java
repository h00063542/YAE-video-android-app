package com.yilos.nailstar.download;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yilos on 15/11/12.
 */
public class DownLoadInfo implements Serializable {

    /**
     * 视频id
     */
    private String topicId;

    /**
     * 视频名称
     */
    private String title;

    /**
     * 视频照片
     */
    private String iamge;

    /**
     * 视频标签
     */
//    private List<String> tags;

    /**
     * 讲师照片
     */
    private String photo;

    /**
     * 讲师姓名
     */
    private String name;

    /**
     * 视频大小
     */
    private Long fileSize;

    /**
     * 是否下载完成
     */
    private boolean finished;

}
