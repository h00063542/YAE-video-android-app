package com.yilos.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.yilos.widget.R;

/**
 * Created by yangdan on 15/10/20.
 */
public class ImageCacheView extends ImageView implements ImageLoadingListener, ImageLoadingProgressListener, View.OnClickListener {
    /**
     * 网络图片路径
     */
    private String imageSrc = null;

    private boolean loadSuccess = false;

    private boolean loading = false;

    private OnClickListener clickListener;



    public ImageCacheView(Context context) {
        this(context, null);
    }

    public ImageCacheView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCacheView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.xk2));
        boolean isClickable = true;
        if(attrs != null){
            isClickable = attrs.getAttributeValue("http://schemas.android.com/apk/res/android","clickable")!=null?Boolean.getBoolean(attrs.getAttributeValue("http://schemas.android.com/apk/res/android","clickable")):true;
        }

        if (isClickable){
            super.setOnClickListener(this);
        }
    }

    public void setImageSrc(String src) {
        imageSrc = src;
        loading = false;
        loadSuccess = false;

        loadImage();
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void reloadImage() {
        if (loading) {
            return;
        }

        loadImage();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        clickListener = l;
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
        if (!loading && !loadSuccess) {
            reloadImage();
        }

        if(clickListener != null) {
            clickListener.onClick(v);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(loading) {
            //canvas.drawBitmap();
        } else if(!loadSuccess) {

        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public void onProgressUpdate(String imageUri, View view, int current, int total) {

    }

    private void loadImage() {
        if (null == imageSrc) {
            return;
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageSrc, this, null, this, this);
    }
}
