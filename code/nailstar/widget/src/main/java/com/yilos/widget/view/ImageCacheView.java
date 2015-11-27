package com.yilos.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
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

    private int loadPercent;

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

    @Override
    public void setImageResource(int resId) {
        loading = false;
        loadSuccess = true;
        super.setImageResource(resId);
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
        super.onDraw(canvas);
        if(loading) {
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), R.color.xk3));
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_loading);
            if(bitmap.getWidth() > canvas.getWidth() || bitmap.getHeight() > canvas.getHeight()) {
                float widthScale = canvas.getWidth() / bitmap.getWidth();
                float heightScale = canvas.getHeight() / bitmap.getHeight();
                float scale = widthScale < heightScale ? widthScale : heightScale;
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            float left = (canvas.getWidth() - bitmap.getWidth()) / 2;
            float top = (canvas.getHeight() - bitmap.getHeight()) / 2;
            canvas.drawBitmap(bitmap, left, top, paint);
        }
        if(!loading && !loadSuccess) {
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), R.color.xk3));
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_loading);
            if(bitmap.getWidth() > canvas.getWidth() || bitmap.getHeight() > canvas.getHeight()) {
                float widthScale = canvas.getWidth() / bitmap.getWidth();
                float heightScale = canvas.getHeight() / bitmap.getHeight();
                float scale = widthScale < heightScale ? widthScale : heightScale;
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            float left = (canvas.getWidth() - bitmap.getWidth()) / 2;
            float top = (canvas.getHeight() - bitmap.getHeight()) / 2;
            canvas.drawBitmap(bitmap, left, top, paint);

            paint.setColor(Color.BLACK);
            int fontSize = getResources().getDimensionPixelSize(R.dimen.middle_text_size);
            paint.setTextSize(fontSize);
            canvas.drawText("点击重试加载", (canvas.getWidth() - 6 * fontSize) / 2, (canvas.getHeight() - fontSize) / 2 + bitmap.getHeight(), paint);
        }
    }

    @Override
    public void onProgressUpdate(String imageUri, View view, int current, int total) {
        loadPercent = (current / total) * 100;
    }

    private void loadImage() {
        if (null == imageSrc) {
            return;
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageSrc, this, null, this, this);
    }
}
