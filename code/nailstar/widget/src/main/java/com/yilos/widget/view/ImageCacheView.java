package com.yilos.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * Created by yangdan on 15/10/20.
 */
public class ImageCacheView extends ImageView implements ImageLoadingListener, ImageLoadingProgressListener, View.OnClickListener {

    private String imageSrc = null;

    private boolean loadSuccess = false;

    private boolean loading = false;

    public ImageCacheView(Context context) {
        super(context);

        int bg = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64);
        setBackgroundColor(bg);

        setOnClickListener(this);
    }

    public ImageCacheView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);

        int bg = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64);
        setBackgroundColor(bg);

        setOnClickListener(this);

    }

    public ImageCacheView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        int bg = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64);
        setBackgroundColor(bg);

        setOnClickListener(this);

    }

    public void setImageSrc(String src){
        imageSrc = src;
        loading = false;
        loadSuccess = false;

        loadImage();
    }

    public void reloadImage(){
        if(loading) {
            return;
        }

        loadImage();
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        loading = true;
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        loadSuccess = false;
        loading = false;
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        loadSuccess = true;
        loading = false;
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        loadSuccess = false;
        loading = false;
    }

    @Override
    public void onClick(View v) {
        //如果当前没有加载，并且之前的加载失败了，点击可以重新加载图片
        if(!loading && !loadSuccess) {
            reloadImage();
        }
    }

    @Override
    public void onProgressUpdate(String imageUri, View view, int current, int total) {

    }

    private void loadImage(){
        if(null == imageSrc){
            return;
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageSrc, this, null, this, this);
    }
}
