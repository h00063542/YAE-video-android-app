package com.yilos.widget.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by yangdan on 15/10/20.
 */
public class ImageCacheView extends ImageView {
    public ImageCacheView(Context context) {
        super(context);

        int bg = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64);
        setBackgroundColor(bg);
    }

    public void setImageSrc(String src){
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(src, this);
    }
}
