package com.yilos.nailstar.aboutme.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.view.LevelActivity;
import com.yilos.nailstar.util.ImageUtil;
import com.yilos.nailstar.util.TaskManager;
import com.yilos.widget.circleimageview.CircleImageView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sisilai on 15/11/9.
 */
public class LevelPresenter {
    private static LevelPresenter levelPresenter = new LevelPresenter();
    private LevelActivity levelActivity;
    public static LevelPresenter getInstance(LevelActivity levelActivity) {
        levelPresenter.levelActivity = levelActivity;
        return levelPresenter;
    }
    //获取等级页面头像
    public void getImage(final String photoUrl) {
        TaskManager.Task loadImage = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                byte[] imageData;
                Bitmap bitmap;
                try {
                    imageData = ImageUtil.getBytes(new URL(photoUrl).openStream());
                    bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<Bitmap> imageUITask = new TaskManager.UITask<Bitmap>() {
            @Override
            public Bitmap doWork(Bitmap data) {
                levelActivity.getImage(data);
                return null;
            }
        };

        new TaskManager().next(loadImage).next(imageUITask).start();
    }
}
